package tiles;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends tileManager{
 GamePanel gp;
 BufferedImage[] worldMap;
 public boolean miniMapOn=false;
    public Map(GamePanel gp) {
        super(gp);
        this.gp=gp;
        createWorldMap();
    }
    public void createWorldMap(){

        worldMap=new BufferedImage[gp.maxMap];
        int mapWidth=gp.tileSize*gp.maxWorldCol;
        int mapHeight=gp.tileSize*gp.maxWorldRow;
        for(int i=0;i<gp.maxMap;i++){
            worldMap[i]=new BufferedImage(mapWidth,mapHeight,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2= worldMap[i].createGraphics();
            int col=0;
            int row=0;
            while(col<gp.maxWorldCol && row<gp.maxWorldRow){
                int tileNum=mapTileNum[i][col][row];
                int x=gp.tileSize*col;
                int y=gp.tileSize*row;
                g2.drawImage(tiles[tileNum].image,x,y,null);
                col++;
                if(col==gp.maxWorldCol){
                    row++;
                    col=0;
                }
            }
        }
    }

    // File: src/tiles/Map.java
    public void drawFullMapOverlay(Graphics2D g2) {
        // 1. Dim / patterned background
        BufferedImage bg = tiles[12].image;
        if (bg != null) {
            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
            for (int ty = 0; ty < gp.screenHeight; ty += gp.tileSize) {
                for (int tx = 0; tx < gp.screenWidth; tx += gp.tileSize) {
                    g2.drawImage(bg, tx, ty, gp.tileSize, gp.tileSize, null);
                }
            }
            g2.setComposite(old);
        } else {
            g2.setColor(new Color(0, 0, 0, 140));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        int worldWidth  = gp.tileSize * gp.maxWorldCol;
        int worldHeight = gp.tileSize * gp.maxWorldRow;
        int width = 500;
        int height = (int) Math.round((double) worldHeight / worldWidth * width);
        int x = gp.screenWidth / 2 - width / 2;
        int y = gp.screenHeight / 2 - height / 2;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(x - 12, y - 12, width + 24, height + 24, 16, 16);

        g2.drawImage(worldMap[gp.currentMap], x, y, width, height, null);

        int playerCenterWorldX = gp.player.x + gp.tileSize / 2;
        int playerCenterWorldY = gp.player.y + gp.tileSize / 2;

        double scaleX = (double) width / worldWidth;
        double scaleY = (double) height / worldHeight;
        double scale=(double)(gp.tileSize*gp.maxWorldCol)/width;

        int playerMapX = x + (int) Math.round(playerCenterWorldX * scaleX);
        int playerMapY = y + (int) Math.round(playerCenterWorldY * scaleY);

        int baseSize = (int) (gp.tileSize / scale);
        int playerSize = Math.max(6, (int) Math.round(baseSize * 1.8));

        playerMapX = Math.max(x, Math.min(x + width, playerMapX));
        playerMapY = Math.max(y, Math.min(y + height, playerMapY));

        int r = playerSize / 2;
        g2.drawImage(gp.player.downImages[0],playerMapX - r, playerMapY - r, playerSize, playerSize,null);


        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawRoundRect(x - 12, y - 12, width + 24, height + 24, 16, 16);

        String hint = "Press M to close";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
        FontMetrics fm = g2.getFontMetrics();
        int hw = fm.stringWidth(hint);
        g2.setColor(new Color(255, 255, 255, 210));
        g2.drawString(hint, gp.screenWidth / 2 - hw / 2, y + height + 36);
    }
    public void drawMiniMap(Graphics2D g2) {
        if(miniMapOn){
            int worldWidth  = gp.tileSize * gp.maxWorldCol;
            int worldHeight = gp.tileSize * gp.maxWorldRow;
            int width = 200;
            int height = (int) Math.round((double) worldHeight / worldWidth * width);
            int x = gp.screenWidth -width-gp.tileSize;
            int y = gp.tileSize;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(x - 12, y - 12, width + 24, height + 24, 16, 16);
            g2.drawImage(worldMap[gp.currentMap],x,y,width,height,null);

            int col = gp.player.x / gp.tileSize;
            int row = gp.player.y / gp.tileSize;
            double tileW = (double) width / gp.maxWorldCol;
            double tileH = (double) height / gp.maxWorldRow;

            int playerMapX = x + (int) Math.round((col + 0.5) * tileW);
            int playerMapY = y + (int) Math.round((row + 0.5) * tileH);

            int baseSize = (int) Math.round(tileW);
            int playerSize = Math.max(6, (int) Math.round(baseSize * 1.8));

            int r = playerSize / 2;
            g2.drawImage(gp.player.downImages[0], playerMapX - r, playerMapY - r, playerSize, playerSize, null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));


        }

    }
}
