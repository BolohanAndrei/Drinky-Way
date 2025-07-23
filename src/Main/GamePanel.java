package Main;

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

    //Other classes
    KeyHandler keyHandler=new KeyHandler();
    tileManager tileManager=new tileManager(this);
    public CollisionCheck collisionCheck = new CollisionCheck(this);
    public ObjManager objManager=new ObjManager(this);
    public DeadCheck deadCheck = new DeadCheck(this);
    public SuperObject[] obj=new SuperObject[10];
    Thread gameThread;
    public Player player=new Player(this, keyHandler);
    Sound sound = new Sound();
    Sound se= new Sound();
    public UI ui = new UI(this);
    
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
        playMusic(1);
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

        player.update();
    }

    //paint the screen
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        tileManager.draw(g2d);
        for(int i=0;i<obj.length;i++) {
            if(obj[i] != null) {
                obj[i].draw(g2d, this);
            }
        }
        player.paint(g2d);
        ui.draw(g2d);
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
