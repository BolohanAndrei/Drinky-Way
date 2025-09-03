package tiles;

import Main.GamePanel;
import Main.Utility;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class tileManager extends JPanel {
    GamePanel gp;
    public tile[] tiles;
    public int[][][] mapTileNum;

    public tileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new tile[100];
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/res/maps/worldV3.txt",0);
        loadMap("/res/maps/interior01.txt",1);
    }

    public void getTileImage() {
        try {
            setup(0, "grass00", false);
            setup(1, "grass00", false);
            setup(2, "grass00", false);
            setup(3, "grass00", false);
            setup(4, "grass00", false);
            setup(5, "grass00", false);
            setup(6, "grass00", false);
            setup(7, "grass00", false);
            setup(9, "grass00", false);
            setup(10, "grass00", false);
            setup(11, "grass01", false);
            setup(12, "water00", true);
            setup(13, "water01", true);
            setup(14, "water02", true);
            setup(15, "water03", true);
            setup(16, "water04", true);
            setup(17, "water05", true);
            setup(18, "water06", true);
            setup(19, "water07", true);
            setup(20, "water08", true);
            setup(21, "water09", true);
            setup(22, "water10", true);
            setup(23, "water11", true);
            setup(24, "water12", true);
            setup(25, "water13", true);
            setup(26,"road00", false);
            setup(27,"road01", false);
            setup(28,"road02", false);
            setup(29,"road03", false);
            setup(30,"road04", false);
            setup(31,"road05", false);
            setup(32,"road06", false);
            setup(33,"road07", false);
            setup(34,"road08", false);
            setup(35,"road09", false);
            setup(36,"road10", false);
            setup(37,"road11", false);
            setup(38,"road12", false);
            setup(39,"earth", false);
            setup(40,"wall", true);
            setup(41,"tree", true);
            setup(42,"hut", false);
            setup(43,"floor01", false);
            setup(44,"table01", true);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public void setup(int index,String path,boolean collision){
        Utility u = new Utility();
        try{
            tiles[index]=new tile();
            tiles[index].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tileset/" + path + ".png")));
            tiles[index].image= u.scaleImage(tiles[index].image, gp.tileSize, gp.tileSize);
            tiles[index].collision = collision;
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    public void loadMap(String filePath,int map) {
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)));

            int col=0;
            int row=0;

            while(col<gp.maxWorldCol && row<gp.maxWorldRow) {
                String line = br.readLine();
                while(col<gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch(Exception e){
            e.getStackTrace();
        }
    }

    public void draw(Graphics g2) {
        final int tileSize = gp.tileSize;

        int playerX = gp.player.x;
        int playerY = gp.player.y;
        int screenX = gp.player.screenX;
        int screenY = gp.player.screenY;

        int worldLeft = playerX - screenX;
        int worldTop = playerY - screenY;
        int worldRight = worldLeft + gp.screenWidth;
        int worldBottom = worldTop + gp.screenHeight;
        int worldPixelWidth = gp.maxWorldCol * tileSize;
        int worldPixelHeight = gp.maxWorldRow * tileSize;

        g2.setColor(new Color(59, 143, 202));

        if (worldLeft < 0) {
            int w = -worldLeft;
            g2.fillRect(0, 0, w, gp.screenHeight);
        }
        if (worldRight > worldPixelWidth) {
            int w = worldRight - worldPixelWidth;
            g2.fillRect(gp.screenWidth - w, 0, w, gp.screenHeight);
        }
        if (worldTop < 0) {
            int h = -worldTop;
            g2.fillRect(0, 0, gp.screenWidth, h);
        }
        if (worldBottom > worldPixelHeight) {
            int h = worldBottom - worldPixelHeight;
            g2.fillRect(0, gp.screenHeight - h, gp.screenWidth, h);
        }

        int startCol = Math.max(0, worldLeft / tileSize);
        int endCol = Math.min(gp.maxWorldCol, (worldRight / tileSize) + 1);
        int startRow = Math.max(0, worldTop / tileSize);
        int endRow = Math.min(gp.maxWorldRow, (worldBottom / tileSize) + 1);

        for (int row = startRow; row < endRow; row++) {
            int baseY = row * tileSize - worldTop;
            for (int col = startCol; col < endCol; col++) {
                int baseX = col * tileSize - worldLeft;
                int tileNum = mapTileNum[gp.currentMap][col][row];
                g2.drawImage(tiles[tileNum].image, baseX, baseY, null);
            }
        }
    }


}
