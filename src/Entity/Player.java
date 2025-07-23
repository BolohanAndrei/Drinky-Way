
        package Entity;

import Main.GamePanel;
import Main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    public int hasGoldKey=0;
    public int hasEmeraldKey=0;
    public int hasSilverKey=0;

    private double currentSpeed = 0;
    private final double acceleration = 0.2;
    private final double maxSpeed = 4.0;


    //CONSTRUCTOR
    public Player(GamePanel gp, KeyHandler kh) {
        this.gp = gp;
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
    }


    //LOAD PLAYER IMAGES
    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_right_2.png"));
            idle_up = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_up_idle.png"));
            idle_down = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_down_idle.png"));
            idle_left = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_left_idle.png"));
            idle_right = ImageIO.read(getClass().getResourceAsStream("/res/player/pirate_right_idle.png"));
            die1 = ImageIO.read(getClass().getResourceAsStream("/res/player/die1.png"));
            die2 = ImageIO.read(getClass().getResourceAsStream("/res/player/die2.png"));
            die3 = ImageIO.read(getClass().getResourceAsStream("/res/player/die3.png"));
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
    public void pickUpObj(int i){
        if(i!=999) {
            String objName = gp.obj[i].name;
            switch (objName) {
                case "Gold_Key":
                    gp.playSE(2);
                    hasGoldKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Ahoy Captain, you got a gold key!");
                    break;
                case "Silver_Key":
                    gp.playSE(2);
                    hasSilverKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Ahoy Captain, you got a silver key!");
                    break;
                case "Emerald_Key":
                    gp.playSE(2);
                    hasEmeraldKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Ahoy Captain, you got an emerald key!");
                    break;
                case "Door":
                    gp.playSE(3);
                    if (hasGoldKey > 0) {
                        gp.obj[i] = null;
                        hasGoldKey--;
                        gp.ui.showMessage("You opened a door, Captain!");
                    }else{
                        gp.ui.showMessage("You need a gold key to open this door, Captain!");
                    }
                    break;
                case "Skull_Door":
                    gp.playSE(3);
                    if (hasEmeraldKey > 0) {
                        gp.obj[i] = null;
                        hasEmeraldKey--;
                        gp.ui.showMessage("You opened a skull door, Captain!");
                    }else{
                        gp.ui.showMessage("You need an emerald key to open this skull door, Captain!");
                    }
                    break;
                case "Crusty Boots":
                    gp.playSE(5);
                    gp.obj[i] = null;
                    speed += 1;
                    gp.ui.showMessage("You found Crusty Boots, Captain! Your speed increased by 1!");
                    break;
                case "Chest":
                    gp.playSE(4);
                    if (hasSilverKey > 0) {
                        gp.obj[i] = null;
                        hasSilverKey--;
                        //gp.ui.showMessage("You opened a chest, Captain!");
                        gp.ui.gameFinished = true;
                        gp.stopMusic();
                        gp.playSE(8);
                       // gp.ui.showMessage("Congratulations, Captain! You found the treasure and completed the game!");
                    } else {
                        gp.ui.showMessage("You need a silver key to open this chest, Captain!");
                    }
                    break;
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
        g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}