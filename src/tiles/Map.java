package tiles;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends tileManager{
 GamePanel gp;
 BufferedImage[] worldMap;
 public boolean miniMapOn=false;
 private final boolean[][][] explored;
 private static final int REVEAL_RADIUS = 2;

    public Map(GamePanel gp) {
        super(gp);
        this.gp=gp;
        explored = new boolean[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
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
            g2.dispose();
        }
    }

    public void updateExploration(){
        int col = gp.player.x / gp.tileSize;
        int row = gp.player.y / gp.tileSize;
        reveal(gp.currentMap,col,row,REVEAL_RADIUS);
    }

    private void reveal(int map,int centerCol,int centerRow,int radius){
        for(int dy=-radius; dy<=radius; dy++){
            for(int dx=-radius; dx<=radius; dx++){
                int c = centerCol + dx;
                int r = centerRow + dy;
                if(c>=0 && c<gp.maxWorldCol && r>=0 && r<gp.maxWorldRow){
                    explored[map][c][r]=true;
                }
            }
        }
    }

    public void drawFullMapOverlay(Graphics2D g2) {
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

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x - 12, y - 12, width + 24, height + 24, 16, 16);

        g2.setColor(Color.black);
        g2.fillRect(x, y, width, height);

        double tileW = (double) width / gp.maxWorldCol;
        double tileH = (double) height / gp.maxWorldRow;
        BufferedImage wm = worldMap[gp.currentMap];
        for(int col=0; col<gp.maxWorldCol; col++){
            for(int row=0; row<gp.maxWorldRow; row++){
                if(explored[gp.currentMap][col][row]){
                    int dx = x + (int)Math.round(col * tileW);
                    int dy = y + (int)Math.round(row * tileH);
                    int dx2 = x + (int)Math.round((col+1) * tileW);
                    int dy2 = y + (int)Math.round((row+1) * tileH);
                    int sx = col * gp.tileSize;
                    int sy = row * gp.tileSize;
                    int sx2 = sx + gp.tileSize;
                    int sy2 = sy + gp.tileSize;
                    g2.drawImage(wm, dx, dy, dx2, dy2, sx, sy, sx2, sy2, null);
                }
            }
        }

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

            g2.setColor(Color.black);
            g2.fillRect(x, y, width, height);

            double tileW = (double) width / gp.maxWorldCol;
            double tileH = (double) height / gp.maxWorldRow;
            BufferedImage wm = worldMap[gp.currentMap];
            for(int col=0; col<gp.maxWorldCol; col++){
                for(int row=0; row<gp.maxWorldRow; row++){
                    if(explored[gp.currentMap][col][row]){
                        int dx = x + (int)Math.round(col * tileW);
                        int dy = y + (int)Math.round(row * tileH);
                        int dx2 = x + (int)Math.round((col+1) * tileW);
                        int dy2 = y + (int)Math.round((row+1) * tileH);
                        int sx = col * gp.tileSize;
                        int sy = row * gp.tileSize;
                        int sx2 = sx + gp.tileSize;
                        int sy2 = sy + gp.tileSize;
                        g2.drawImage(wm, dx, dy, dx2, dy2, sx, sy, sx2, sy2, null);
                    }
                }
            }

            int col = gp.player.x / gp.tileSize;
            int row = gp.player.y / gp.tileSize;
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
