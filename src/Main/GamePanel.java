package Main;

import Entity.Entity;
import Entity.Player;
import tiles.tileManager;
import tiles_interactive.InteractiveTiles;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16;
    final int scale = 3;
    public int tileSize = originalTileSize * scale;
    public int maxScreenCol = 21;
    public int maxScreenRow = 13;
    public int screenWidth  = tileSize * maxScreenCol;
    public int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    private BufferedImage tempScreen;

    int FPS = 60;
    private long tick;

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;

    public KeyHandler keyHandler = new KeyHandler(this);
    tileManager tileManager = new tileManager(this);
    public CollisionCheck collisionCheck = new CollisionCheck(this);
    public AssetManager assetManager = new AssetManager(this);
    public DeadCheck deadCheck = new DeadCheck(this);
    public EventHandler eventHandler = new EventHandler(this);
    public DrinkSystem drinkSystem = new DrinkSystem(this);
    public Sound sound = new Sound();
    public UI ui = new UI(this);

    public Entity[] obj = new Entity[100];
    public Player player = new Player(this, keyHandler);
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[100];
    public InteractiveTiles[] iTile = new InteractiveTiles[50];
    public ArrayList<Entity> projectiles = new ArrayList<>();
    public ArrayList<Entity> particles = new ArrayList<>();
    private final ArrayList<Entity> entities = new ArrayList<>();

    private Thread gameThread;
    private final Object renderLock = new Object();

    private volatile BufferStrategy bufferStrategy;
    private volatile boolean useStrategy=false;

    private int fps;
    private int frameCounter;
    private long fpsTimer = System.nanoTime();

    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);
        addKeyListener(keyHandler);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public void attachBufferStrategy(BufferStrategy bs) {
        if (bs != null) {
            setDoubleBuffered(false);
            setIgnoreRepaint(true);
            bufferStrategy = bs;
            useStrategy = true;
        }
    }

    public void setupGame() {
        assetManager.setObj();
        sound.playMusic(0);
        assetManager.setNPC();
        assetManager.setMonster();
        assetManager.setInteractiveTile();
        gameState = titleState;
        if (tempScreen == null) {
            tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        }
        drawToTempScreen();
        if (useStrategy) renderFrameStrategy();
    }

    public void startGameThread() {
        if (gameThread != null) return;
        gameThread = new Thread(this, "GameLoop");
        gameThread.start();
    }

    @Override
    public void run() {
        final double drawInterval = 1_000_000_000.0 / FPS;
        double delta = 0;
        long last = System.nanoTime();
        while (gameThread != null) {
            long loopStart = System.nanoTime();
            delta += (loopStart - last) / drawInterval;
            last = loopStart;
            boolean didWork = false;
            while (delta >= 1) {
                update();
                drawToTempScreen();
                didWork = true;
                delta--;
            }
            if (didWork) {
                if (useStrategy) {
                    renderFrameStrategy();
                } else {
                    repaint();
                }
            }
            long frameTime = System.nanoTime() - loopStart;
            long sleepNanos = (long) drawInterval - frameTime;
            if (sleepNanos > 0) {
                try { Thread.sleep(sleepNanos / 1_000_000L, (int) (sleepNanos % 1_000_000L)); } catch (InterruptedException ignored) {}
            } else Thread.yield();
        }
    }

    public void update() {
        if (gameState == playState) {
            tick++;
            player.update();
            drinkSystem.update(player);
            for (Entity e : npc) if (e != null) e.update();
            for (int i = 0; i < monster.length; i++) {
                Entity m = monster[i];
                if (m == null) continue;
                if (m.alive && !m.dying) m.update();
                if (!m.alive) { m.checkDrop(); monster[i] = null; }
            }
            for (int i = projectiles.size() - 1; i >= 0; i--) {
                Entity p = projectiles.get(i);
                if (p == null || !p.alive) projectiles.remove(i); else p.update();
            }
            for (int i = particles.size() - 1; i >= 0; i--) {
                Entity p = particles.get(i);
                if (p == null || !p.alive) particles.remove(i); else p.update();
            }
            for (InteractiveTiles t : iTile) if (t != null) t.update();
        }
    }

    public long playerTick() { return tick; }

    public void drawToTempScreen() {
        if (tempScreen == null) return;
        synchronized (renderLock) {
            Graphics2D g2d = tempScreen.createGraphics();
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, screenWidth, screenHeight);
            if (gameState == titleState) { ui.draw(g2d); drawFps(g2d); g2d.dispose(); return; }
            boolean useBuffer = player.drinkPercent >= 40;
            Graphics2D worldG = useBuffer ? drinkSystem.beginWorldBuffer() : g2d;
            drinkSystem.preWorldTransform(worldG);
            tileManager.draw(worldG);
            for (InteractiveTiles t : iTile) if (t != null) t.draw(worldG);
            entities.add(player);
            for (Entity e : npc) if (e != null) entities.add(e);
            for (Entity e : obj) if (e != null) entities.add(e);
            for (Entity e : monster) if (e != null) entities.add(e);
            entities.addAll(projectiles);
            entities.addAll(particles);
            entities.sort((a, b) -> Integer.compare(a.x, b.x));
            for (Entity e : entities) e.draw(worldG);
            entities.clear();
            drinkSystem.postWorldTransform(worldG);
            if (useBuffer) { worldG.dispose(); drinkSystem.flushWorldBuffer(g2d); }
            drinkSystem.drawAfterImages(g2d);
            drinkSystem.overlay(g2d);
            ui.draw(g2d);
            drawFps(g2d);
            g2d.dispose();
        }
    }

    public void renderFrameStrategy() {
        BufferStrategy bs = bufferStrategy;
        if (!useStrategy || bs == null) return;

        do {
            if (!isDisplayable()) return;
            Graphics g = null;
            try {
                try { g = bs.getDrawGraphics(); }
                catch (NullPointerException npe) { break; }
                drawScaledFrame(g);
            } catch (IllegalStateException ex) {
                break;
            } finally { if (g != null) g.dispose(); }
            try { bs.show(); }
            catch (NullPointerException | IllegalStateException ex) { break; }
            Toolkit.getDefaultToolkit().sync();
            recordFrame();
        } while (bs.contentsLost());
    }

    public void drawScaledFrame(Graphics g) {
        BufferedImage frame;
        synchronized (renderLock) { frame = tempScreen; }
        if (frame == null) return;

        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        double scaleX = (double) w / screenWidth;
        double scaleY = (double) h / screenHeight;
        double scale = Math.min(scaleX, scaleY);

        int scaledWidth = (int) Math.round(screenWidth * scale);
        int scaledHeight = (int) Math.round(screenHeight * scale);
        int x = (w - scaledWidth) / 2;
        int y = (h - scaledHeight) / 2;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(frame, x, y, scaledWidth, scaledHeight, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (useStrategy) return;
        super.paintComponent(g);
        drawScaledFrame(g);
        recordFrame();
    }

    private void recordFrame() {
        frameCounter++;
        long now = System.nanoTime();
        if (now - fpsTimer >= 1_000_000_000L) {
            fps = frameCounter;
            frameCounter = 0;
            fpsTimer = now;
        }
    }

    private void drawFps(Graphics2D g) {
        String text = "FPS: " + fps;
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 12f));
        FontMetrics fm = g.getFontMetrics();
        int textW = fm.stringWidth(text);
        int textH = fm.getAscent();
        int pad = 4;
        int boxW = textW + pad * 2;
        int boxH = textH + pad * 2;
        int x = screenWidth - boxW - 6;
        int y = screenHeight - boxH - 6;

        Composite old = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
        g.setColor(Color.BLACK);
        g.fillRoundRect(x, y, boxW, boxH, 8, 8);
        g.setComposite(old);

        g.setColor(new Color(255,255,255,170));
        g.drawString(text, x + pad, y + pad + textH - fm.getDescent());
    }

}
