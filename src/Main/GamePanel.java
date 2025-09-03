package Main;

import AI.PathFind;
import Entity.Entity;
import Entity.Player;
import Envire.EnvManager;
import tiles.Map;
import tiles.tileManager;
import tiles_interactive.InteractiveTiles;
import Monster.BaseMonster;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    //Debug hitbox
    public boolean debugHitboxes = false;
    public void toggleDebugHitboxes(){ debugHitboxes = !debugHitboxes; }

    final int originalTileSize = 16;
    final int scale = 3;
    public int tileSize = originalTileSize * scale;
    public int maxScreenCol = 21;
    public int maxScreenRow = 13;
    public int screenWidth  = tileSize * maxScreenCol;
    public int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public int maxMap=10;
    public int currentMap=0;

    private BufferedImage tempScreen;

    int FPS = 60;
    private long tick;

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int optionState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int gameOverState = 5;
    public final int transitionState = 6;
    public final int tradeState = 7;
    public final int chestState=8;
    public final int sleepState=9;
    public final int mapState=10;

    public KeyHandler keyHandler = new KeyHandler(this);
    public tileManager tileManager = new tileManager(this);
    public CollisionCheck collisionCheck = new CollisionCheck(this);
    public AssetManager assetManager = new AssetManager(this);
    public EventHandler eventHandler = new EventHandler(this);
    public DrinkSystem drinkSystem = new DrinkSystem(this);
    public Sound music = new Sound();
    public Sound se=new Sound();
    public UI ui = new UI(this);
    Config config=new Config(this);
    public PathFind pathFind = new PathFind(this);
    public EnvManager envManager = new EnvManager(this);
    public Map map = new Map(this);

    public Entity[][] obj = new Entity[maxMap][100];
    public Player player = new Player(this, keyHandler);
    public Entity[][] npc = new Entity[maxMap][10];
    public Entity[][] monster = new Entity[maxMap][100];
    public InteractiveTiles[][] iTile = new InteractiveTiles[maxMap][50];
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
        music.playMusic(0);
        assetManager.setNPC();
        assetManager.setMonster();
        assetManager.setInteractiveTile();
        envManager.setup();
        gameState = titleState;
        if (tempScreen == null) {
            tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        }
        drawToTempScreen();
        if (useStrategy) renderFrameStrategy();
    }

    public void retry(){
        player.dieFrame=0;
        player.setDefaultPosition();
        player.setDefaultLife();
        assetManager.setNPC();
        assetManager.setMonster();
    }

    public void startGameThread() {
        if (gameThread != null) return;
        gameThread = new Thread(this, "GameLoop");
        gameThread.start();
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        final long frameDuration = (long)(1_000_000_000.0 / FPS);
        long nextFrameTime = System.nanoTime();
        double delta = 0;

        while (gameThread != null) {
            long now = System.nanoTime();

            if (now >= nextFrameTime) {
                long framesBehind = (now - nextFrameTime) / frameDuration;
                nextFrameTime += (framesBehind + 1) * frameDuration;
                delta += framesBehind + 1;
            }

            if (delta >= 1) {
                update();
                drawToTempScreen();
                if (useStrategy) renderFrameStrategy(); else repaint();
                delta -= 1;
            }

            long remaining = nextFrameTime - System.nanoTime();
            if (remaining > 1_000_000) {
                try {
                    Thread.sleep(remaining / 1_000_000);
                } catch (InterruptedException ignored) {}
            } else if (remaining > 100_000) {
                Thread.yield();
            } else {
                while ((remaining = nextFrameTime - System.nanoTime()) > 0) {
                    if (remaining > 50_000) Thread.onSpinWait();
                }
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            tick++;
            player.update();
            // Reveal current tile (fog of war)
            map.updateExploration();
            drinkSystem.update(player);
            for(int i=0;i<npc[currentMap].length;i++){
                if(npc[currentMap][i] == null) continue;
                npc[currentMap][i].update();
            }
            for (int i = 0; i < monster[currentMap].length; i++) {
                Entity m = monster[currentMap][i];
                if (m == null) continue;
                if (m.alive) m.update();
                if (!m.alive) { m.checkDrop(); monster[currentMap][i] = null; }
            }
            for (int i = projectiles.size() - 1; i >= 0; i--) {
                Entity p = projectiles.get(i);
                if (p == null || !p.alive) projectiles.remove(i); else p.update();
            }
            for (int i = particles.size() - 1; i >= 0; i--) {
                Entity p = particles.get(i);
                if (p == null || !p.alive) particles.remove(i); else p.update();
            }
            for(int i=0;i<iTile[currentMap].length;i++){
                if(iTile[currentMap][i] == null) continue;
                iTile[currentMap][i].update();
            }
            envManager.update();
        }
    }

    public long playerTick() { return tick; }

    private boolean isOnScreen(Entity e){
        int tile = tileSize;
        int px = player.x;
        int py = player.y;
        int sx = player.screenX;
        int sy = player.screenY;
        return ((e.x + tile) > (px - sx)) &&
               ((e.x - tile) < (px + sx)) &&
               ((e.y + tile) > (py - sy)) &&
               ((e.y - tile) < (py + sy));
    }

    public void drawToTempScreen() {
        if (tempScreen == null) return;
        synchronized (renderLock) {
            Graphics2D g2d = tempScreen.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, screenWidth, screenHeight);
            if (gameState == titleState || gameState==optionState) {
                ui.draw(g2d);
                drawFps(g2d);
                g2d.dispose();
                return;
            }

            boolean showMapOverlay=(gameState==mapState);

            boolean useBuffer = player.drinkPercent >= 40;
            Graphics2D worldG = useBuffer ? drinkSystem.beginWorldBuffer() : g2d;
            drinkSystem.preWorldTransform(worldG);
            tileManager.draw(worldG);

            entities.add(player);

            for(int i=0;i<iTile[currentMap].length;i++){
                if(iTile[currentMap][i] == null) continue;
                if(!isOnScreen(iTile[currentMap][i])) continue;
                iTile[currentMap][i].draw(worldG);
            }

            for(int i=0;i<npc[currentMap].length;i++){
                Entity e = npc[currentMap][i];
                if(e == null) continue;
                if(!isOnScreen(e)) continue;
                entities.add(e);
            }
            for(int i=0;i<obj[currentMap].length;i++){
                Entity e = obj[currentMap][i];
                if(e == null) continue;
                if(!isOnScreen(e)) continue;
                entities.add(e);
            }
            for(int i=0;i<monster[currentMap].length;i++){
                Entity e = monster[currentMap][i];
                if(e == null) continue;
                if(!isOnScreen(e)) continue;
                entities.add(e);
            }

            if (!projectiles.isEmpty()) {
                for(Entity p : projectiles){ if(isOnScreen(p)) entities.add(p); }
            }
            if (!particles.isEmpty()) {
                for(Entity p : particles){ if(isOnScreen(p)) entities.add(p); }
            }

            if (entities.size() > 1) {
                entities.sort((a, b) -> Integer.compare(a.x, b.x));
            }

            for (Entity e : entities) e.draw(worldG);

            //Debug
            if(debugHitboxes){
                drawDebugHitboxes(worldG);
            }

            entities.clear();
            drinkSystem.postWorldTransform(worldG);
            if (useBuffer) { worldG.dispose(); drinkSystem.flushWorldBuffer(g2d); }

            if (player.drinkPercent >= 70) {
                drinkSystem.drawAfterImages(g2d);
            }
            if (player.drinkPercent >= 30) {
                drinkSystem.overlay(g2d);
            }

            envManager.draw(g2d);

            if(showMapOverlay) {
                map.drawFullMapOverlay(g2d);
            }
            map.drawMiniMap(g2d);

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
        synchronized (renderLock) {
            BufferedImage frame = tempScreen;
            if (frame == null) return;

            int w = getWidth();
            int h = getHeight();
            if (w <= 0 || h <= 0) return;

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            double scaleX = (double) w / screenWidth;
            double scaleY = (double) h / screenHeight;

                double scale = Math.max(scaleX, scaleY);
                int scaledWidth = (int)Math.round(screenWidth * scale);
                int scaledHeight = (int)Math.round(screenHeight * scale);
                int x = (w - scaledWidth)/2;
                int y = (h - scaledHeight)/2;
                g.setColor(Color.BLACK); g.fillRect(0,0,w,h);
                g.drawImage(frame, x, y, scaledWidth, scaledHeight, null);

        }
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


    //Debug
    private void drawDebugHitboxes(Graphics2D g){
        g.setStroke(new BasicStroke(2f));
        int pSX = player.screenX + player.solidArea.x;
        int pSY = player.screenY + player.solidArea.y;
        g.setColor(new Color(0,255,0,120));
        g.drawRect(pSX, pSY, player.solidArea.width, player.solidArea.height);
        if(player.attacking){
            Rectangle area;
            try {
                java.lang.reflect.Method m = player.getClass().getDeclaredMethod("buildAttackArea");
                m.setAccessible(true);
                area = (Rectangle) m.invoke(player);
            } catch (Exception ex){ area = null; }
            if(area!=null){
                int screenX = area.x - player.x + player.screenX;
                int screenY = area.y - player.y + player.screenY;
                g.setColor(new Color(0,200,255,90));
                g.fillRect(screenX, screenY, area.width, area.height);
                g.setColor(new Color(0,255,255,170));
                g.drawRect(screenX, screenY, area.width, area.height);
            }
        }
        for(Entity mon : monster[currentMap]){
            if(mon==null || !(mon instanceof BaseMonster bm) || !mon.alive) continue;
            Rectangle r = bm.debugMeleeRect();
            int sx = r.x - player.x + player.screenX;
            int sy = r.y - player.y + player.screenY;
            Color fill = bm.isMeleeActivePhase()? new Color(255,0,0,80) : (bm.isMeleeWindupPhase()? new Color(255,140,0,60): new Color(255,255,0,40));
            Color outline = bm.isMeleeActivePhase()? new Color(255,0,0,180) : (bm.isMeleeWindupPhase()? new Color(255,140,0,180): new Color(255,255,0,160));
            g.setColor(fill); g.fillRect(sx, sy, r.width, r.height);
            g.setColor(outline); g.drawRect(sx, sy, r.width, r.height);
            int monSX = mon.x - player.x + player.screenX + mon.solidArea.x;
            int monSY = mon.y - player.y + player.screenY + mon.solidArea.y;
            g.setColor(new Color(255,0,255,150));
            g.drawRect(monSX, monSY, mon.solidArea.width, mon.solidArea.height);
        }
        g.setFont(g.getFont().deriveFont(12f));
        int y = 14;
        g.setColor(new Color(0,0,0,130));
        g.fillRoundRect(6,6,210,90,8,8);
        g.setColor(Color.WHITE);
        g.drawString("DEBUG HITBOXES (F3)", 12, y); y+=14;
        g.setColor(new Color(0,255,0)); g.drawString("Player Solid",12,y); y+=14;
        g.setColor(new Color(0,255,255)); g.drawString("Player Attack",12,y); y+=14;
        g.setColor(new Color(255,255,0)); g.drawString("Melee Range (idle)",12,y); y+=14;
        g.setColor(new Color(255,140,0)); g.drawString("Melee Windup",12,y); y+=14;
        g.setColor(new Color(255,0,0)); g.drawString("Melee Active",12,y);
    }

}
