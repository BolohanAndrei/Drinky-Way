package object;

import Main.GamePanel;
import Main.Utility;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    Utility utility = new Utility();

    public void draw(Graphics g, GamePanel gp){
        int x = worldX - gp.player.x + gp.player.screenX;
        int y = worldY - gp.player.y + gp.player.screenY;

        if(worldX+gp.tileSize>gp.player.x-gp.player.screenX &&
                worldX-gp.tileSize<gp.player.x+gp.player.screenX &&
                worldY+gp.tileSize>gp.player.y-gp.player.screenY &&
                worldY-gp.tileSize<gp.player.y+gp.player.screenY) {
            g.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }

    }

}
