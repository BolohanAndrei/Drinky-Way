package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int x,y;
    public int speed;

    public BufferedImage up1,up2,down1,down2,left1,left2,right1,right2,idle_up, idle_down, idle_left, idle_right,die1,die2,die3;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    public boolean dead = false;
    public int dieFrame = 0;
}
