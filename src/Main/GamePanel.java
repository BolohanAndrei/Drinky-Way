package Main;

import Entity.Entity;
import Entity.Player;
import tiles.tileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    //Variables
    final int originalTileSize = 16;
    final int scale=3;
    public final int tileSize=originalTileSize*scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 48 * 16 = 768
    public final int screenHeight = tileSize * maxScreenRow; // 48 * 12 = 576


    //FPS
    int FPS = 60;

    //drink fx
    private long tick;
    public long playerTick(){return tick;}

    //World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    //Game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState=4;


    //Other-System classes
    public KeyHandler keyHandler=new KeyHandler(this);
    tileManager tileManager=new tileManager(this);
    public CollisionCheck collisionCheck = new CollisionCheck(this);
    public AssetManager assetManager =new AssetManager(this);
    public DeadCheck deadCheck = new DeadCheck(this);
    public EventHandler eventHandler = new EventHandler(this);
    public DrinkSystem drinkSystem = new DrinkSystem(this);

    Thread gameThread;

   public Sound sound = new Sound();
    public UI ui = new UI(this);


    //player and objects
    public Entity[] obj=new Entity[10];
    public Player player=new Player(this, keyHandler);
    public Entity[] npc =new Entity[10];
    ArrayList<Entity> entities=new ArrayList<>();
    public ArrayList<Entity> projectiles=new ArrayList<>();
    public Entity[] monster=new Entity[20];

    //Constructor
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setFocusTraversalKeysEnabled(false);

    }

    //Setup game
    public void setupGame(){
        assetManager.setObj();
        //sound.playAlternatingLoop(0,1,13,14);
        sound.playMusic(0);
        assetManager.setNPC();
        assetManager.setMonster();
        gameState = titleState;
    }

    //Start the game
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start()  ;
    }

    //Time going to run the game
    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer=0;
        int drawCount=0;

        while(gameThread !=null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000) {
                System.out.println("FPS"+drawCount);
                drawCount=0;
                timer=0;
            }
        }
    }

    public void update() {

        if(gameState == playState) {
            tick++;
            player.update();
            drinkSystem.update(player);

            //NPC
            for (Entity entity : npc) {
                if (entity != null) {
                    entity.update();
                }
            }
            //Monster
            for (int i = 0; i < monster.length; i++) {
                Entity m = monster[i];
                if (m == null) continue;
                if (m.alive && !m.dying) {
                    m.update();
                }
                if (!m.alive) {
                    monster[i] = null;
                }
            }
            //Projectile
            for (int i = projectiles.size() - 1; i >= 0; i--) {
                Entity p = projectiles.get(i);
                if (p == null || !p.alive) {
                    projectiles.remove(i);
                    continue;
                }
                p.update();
            }
        }
    }

    //paint the screen
    public void paint(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        //TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2d);
        } else {


            boolean useBuffer=player.drinkPercent>=40;
            Graphics2D worldG=useBuffer? drinkSystem.beginWorldBuffer() : g2d;
            drinkSystem.preWorldTransform(worldG);
            //TILE
            tileManager.draw(worldG);

            //Entities
            entities.add(player);
            for (Entity entity : npc) {
                if (entity != null) {
                    entities.add(entity);
                }
            }

            //Objects
            for (Entity objects: obj) {
                if (objects != null) {
                    entities.add(objects);
                }
            }

            //Monsters
            for (Entity monsters: monster) {
                if (monsters != null) {
                    entities.add(monsters);
                }
            }

            //Projectiles
            for (Entity projectile: projectiles) {
                if (projectile != null) {
                    entities.add(projectile);
                }
            }

            entities.sort((e1, e2) -> Integer.compare(e1.x, e2.x));

            //Draw entities
            for (Entity entity : entities) {
                entity.draw(worldG);
            }

            //Empty entities
            entities.clear();

            drinkSystem.postWorldTransform(worldG);
            if(useBuffer) {
                worldG.dispose();
                drinkSystem.flushWorldBuffer(g2d);
            }

            drinkSystem.drawAfterImages(g2d);
            drinkSystem.overlay(g2d);

            //UI
            ui.draw(g2d);

            g2d.dispose();
        }
    }

}
