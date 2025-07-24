package Entity;

import Main.GamePanel;
import Main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    GamePanel gp;

    public int x, y;
    public int speed;
    public int actionLockCounter = 0;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, idle_up, idle_down, idle_left, idle_right, die1, die2, die3, idle_1, idle_2, idle_3, idle_4;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    public boolean dead = false;
    public int dieFrame = 0;

    public Entity(GamePanel gp) {
        this.gp = gp;

    }

    public void setAction(){

    }

    public void update(){

        collisionOn = false;
        gp.collisionCheck.checkTile(this);
        gp.collisionCheck.checkObj(this, false);
        gp.collisionCheck.checkPlayer(this);
        setAction();

        if(collisionOn==false){
            switch (direction) {
                case "up": y -= speed; break;
                case "down": y += speed; break;
                case "left": x -= speed; break;
                case "right": x += speed; break;
            }
        }
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g) {
        BufferedImage image = null;
        int NPCx = x - gp.player.x + gp.player.screenX;
        int NPCy = y - gp.player.y + gp.player.screenY;

        if (x + gp.tileSize > gp.player.x - gp.player.screenX &&
                x - gp.tileSize < gp.player.x + gp.player.screenX &&
                y + gp.tileSize > gp.player.y - gp.player.screenY &&
                y - gp.tileSize < gp.player.y + gp.player.screenY) {

            switch (direction) {
                case "up": image = (spriteNum == 1) ? up1 : up2; break;
                case "up_left": case "left": case "down_left": image = (spriteNum == 1) ? left1 : left2; break;
                case "up_right": case "right": case "down_right": image = (spriteNum == 1) ? right1 : right2; break;
                case "down": image = (spriteNum == 1) ? down1 : down2; break;
                case "idle_up": case "idle_up_left": case "idle_up_right": image = idle_up; break;
                case "idle_down": case "idle_down_left": case "idle_down_right": image = idle_down; break;
                case "idle_left": image = idle_left; break;
                case "idle_right": image = idle_right; break;
                case "idle_1": image = idle_1; break;
                case "idle_2": image = idle_2; break;
                case "idle_3": image = idle_3; break;
                case "idle_4": image = idle_4; break;
            }
            g.drawImage(image, NPCx, NPCy, gp.tileSize, gp.tileSize, null);
        }
    }

    public BufferedImage setup(String name) {
        Utility u = new Utility();
        BufferedImage scale = null;
        try {
            scale = ImageIO.read(getClass().getResourceAsStream("/res/" + name + ".png"));
            scale = u.scaleImage(scale, gp.tileSize, gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scale;
    }
}
