package Main;

import Entity.Entity;
import Entity.Player;
import object.SuperObject;
import tiles.tileManager;

import javax.swing.*;
import java.awt.*;

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

    //World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    //Game state
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    //Other-System classes
    KeyHandler keyHandler=new KeyHandler(this);
    tileManager tileManager=new tileManager(this);
    public CollisionCheck collisionCheck = new CollisionCheck(this);
    public ObjManager objManager=new ObjManager(this);
    public DeadCheck deadCheck = new DeadCheck(this);

    Thread gameThread;

    Sound sound = new Sound();
    Sound se= new Sound();
    public UI ui = new UI(this);


    //player and objects
    public SuperObject[] obj=new SuperObject[10];
    public Player player=new Player(this, keyHandler);
    public Entity[] npc =new Entity[10];
    //Constructor
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    //Setup game
    public void setupGame(){
        objManager.setObj();
        //sound.playAlternatingLoop(0,1,13,14);
        objManager.setNPC();
        playMusic(15);
        gameState = playState;
    }

    //Start the game
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start()  ;
    }

    //Time going to run the game
    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
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
            //PlAYER
            player.update();

            //NPC
            for(int i=0;i<npc.length;i++) {
                if(npc[i] != null) {
                    npc[i].update();
                }
            }
        }
        if(gameState == pauseState) {
            //
        }
    }

    //paint the screen
    public void paint(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        //TILE
        tileManager.draw(g2d);

        //OBJECTS
        for(int i=0;i<obj.length;i++) {
            if(obj[i] != null) {
                obj[i].draw(g2d, this);
            }
        }

        //NPC
        for(int i=0;i<npc.length;i++) {
            if(npc[i] != null) {
                npc[i].draw(g2d);
            }
        }

        //PlAYER
        player.paint(g2d);

        //UI
        ui.draw(g2d);

        //DEBUG
//        long drawStart = System.nanoTime();
//        long drawEnd = System.nanoTime();
//        long passed = drawEnd - drawStart;
//        g2d.setColor(Color.white);
//        g2d.drawString("Draw Time: " + passed / 1000000.0 + " ms", 10, 550);
//        System.out.println("Draw Time: " + passed / 1000000.0 + " ms");

        g2d.dispose();
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        if(sound.clip != null) {
            sound.stop();
        }
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
