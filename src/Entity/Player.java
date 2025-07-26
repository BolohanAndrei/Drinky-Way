
        package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import Main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    private double currentSpeed = 0;
    private final double acceleration = 0.2;
    private final double maxSpeed = 4.0;

    public int maxDrunk;
    public int drunk;


    //CONSTRUCTOR
    public Player(GamePanel gp, KeyHandler kh) {
        super(gp);
        this.keyHandler = kh;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 16, 16);
        solidAreaDefaultX= solidArea.x;
        solidAreaDefaultY= solidArea.y;
        setDefaultValues();
        getPlayerImage();
    }

    //DEFAULT VALUES
    public void setDefaultValues() {
        x = gp.tileSize * 23;
        y = gp.tileSize * 23;
        speed = 4;
        direction = "down";

        //player status
        maxHealth=10;
        health=maxHealth;

        maxDrunk=6;
        drunk=0;
    }


    //LOAD PLAYER IMAGES
    public void getPlayerImage() {
        try {
            up1=setup("player/pirate_up_1");
            up2 = setup("player/pirate_up_2");
            down1 = setup("player/pirate_down_1");
            down2 = setup("player/pirate_down_2");
            left1 = setup("player/pirate_left_1");
            left2 = setup("player/pirate_left_2");
            right1 = setup("player/pirate_right_1");
            right2 = setup("player/pirate_right_2");

            // Idle animations
            idle_up = setup("player/pirate_up_idle");
            idle_down = setup("player/pirate_down_idle");
            idle_left = setup("player/pirate_left_idle");
            idle_right = setup("player/pirate_right_idle");
            die1 = setup("player/die1");
            die2 = setup("player/die2");
            die3 = setup("player/die3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void update() {
        boolean moved = false;
        double dx = 0, dy = 0;

        // Diagonal and straight movement
        if (keyHandler.upPressed && keyHandler.leftPressed) {
            dx = -1; dy = -1; moved = true; direction = "up_left";
        } else if (keyHandler.upPressed && keyHandler.rightPressed) {
            dx = 1; dy = -1; moved = true; direction = "up_right";
        } else if (keyHandler.downPressed && keyHandler.leftPressed) {
            dx = -1; dy = 1; moved = true; direction = "down_left";
        } else if (keyHandler.downPressed && keyHandler.rightPressed) {
            dx = 1; dy = 1; moved = true; direction = "down_right";
        } else if (keyHandler.upPressed) {
            dy = -1; moved = true; direction = "up";
        } else if (keyHandler.downPressed) {
            dy = 1; moved = true; direction = "down";
        } else if (keyHandler.leftPressed) {
            dx = -1; moved = true; direction = "left";
        } else if (keyHandler.rightPressed) {
            dx = 1; moved = true; direction = "right";
        }


        // Idle state
        if (moved) {
            currentSpeed = Math.min(currentSpeed + acceleration, maxSpeed);
        } else {
            currentSpeed = Math.max(currentSpeed - acceleration, 0);
        }

        // Normalize diagonal movement
        if (dx != 0 && dy != 0) {
            dx /= Math.sqrt(2);
            dy /= Math.sqrt(2);
        }

        // Calculate future position based on current speed
        double futureX = x + dx * currentSpeed;
        double futureY = y + dy * currentSpeed;
        boolean canMoveX = true;
        boolean canMoveY = true;

        // Check for collisions with tiles
        if (dx!=0) {
            double tempX=x;
            x = (int) futureX;
            collisionOn=false;
            gp.collisionCheck.checkTile(this);
            canMoveX=!collisionOn;
            x= (int) tempX;
        }

        if (dy != 0) {
            double tempY = y;
            y = (int) futureY;
            collisionOn = false;
            gp.collisionCheck.checkTile(this);
            canMoveY = !collisionOn;
            y = (int) tempY;
        }

        // Check for collisions with objects
        int objIndex= gp.collisionCheck.checkObj(this,true);
        pickUpObj(objIndex);

        // Check for collisions with NPCs
        int npcIndex = gp.collisionCheck.checkEntity(this, gp.npc);
        interactNPC(npcIndex);

        gp.keyHandler.enterPressed = false;

        //Check event
        gp.eventHandler.checkEvent();

        if(collisionOn){
            canMoveX=false;
            canMoveY=false;
        }

        // Update position based on movement
        if(canMoveX){
            x += dx * currentSpeed;
        }
        if(canMoveY){
            y += dy * currentSpeed;
        }


        //Dead check - not working
        if(gp.deadCheck.check(this)){
            dead = true;
            dieFrame=0;

        }

        //IDLE
        if (!moved) {
            switch (direction) {
                case "up": direction = "idle_up"; break;
                case "up_left": direction = "idle_up"; break;
                case "up_right": direction = "idle_up"; break;
                case "down": direction = "idle_down"; break;
                case "down_left": direction = "idle_down"; break;
                case "down_right": direction = "idle_down"; break;
                case "left": direction = "idle_left"; break;
                case "right": direction = "idle_right"; break;
            }
        }

        //Dead animation
        if (dead) {
            if (dieFrame == 0) { up1 = die1; down1 = die1; left1 = die1; right1 = die1; }
            else if (dieFrame == 1) { up1 = die2; down1 = die2; left1 = die2; right1 = die2; }
            else if (dieFrame == 2) { up1 = die3; down1 = die3; left1 = die3; right1 = die3; }
            dieFrame++;
            if (dieFrame > 2) { dead = false; }
        }

        //Sprite animation
        spriteCounter++;
        //System.out.println("Speed"+ speed);
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

    }

    //PICK UP OBJECTS
    public void pickUpObj(int i) {
        if (i != 999) {
            ;
        }
    }

    //INTERACT WITH NPC
    public void interactNPC(int i) {
        if (i != 999) {
            if(gp.keyHandler.enterPressed) {
                gp.npc[i].speak();
                gp.gameState=gp.dialogueState;
            }

        }
    }


    //DRAW PLAYER
    public void paint(Graphics2D g2d) {
        BufferedImage image = null;
        if (dead) {
            if (dieFrame == 0) image = die1;
            else if (dieFrame == 1) image = die2;
            else if (dieFrame == 2) image = die3;
            g2d.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
            return;
        }

        switch (direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "up_left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "up_right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "down_left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "down_right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
            case "idle_up":
                image = idle_up;
                break;
                case "idle_up_left":
                image = idle_up;
                break;
            case "idle_up_right":
                image = idle_up;
                break;
            case "idle_down":
                image = idle_down;
                break;
            case "idle_down_left":
                image = idle_down;
                break;
            case "idle_down_right":
                image = idle_down;
                break;
            case "idle_left":
                image = idle_left;
                break;
            case "idle_right":
                image = idle_right;
                break;
        }
        g2d.drawImage(image, screenX, screenY, null);
    }
}