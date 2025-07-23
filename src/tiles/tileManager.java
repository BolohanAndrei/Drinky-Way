package tiles;

import Main.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class tileManager extends JPanel {
    GamePanel gp;
    public tile[] tiles;
    public int[][] mapTileNum;

    //constructor
    public tileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new tile[100]; // 0: grass, 1: wall, 2: water
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/res/maps/start_map.txt");
    }

    //load tile images
    public void getTileImage() {
        try {
            tiles[0] = new tile(); // grass
            tiles[1] = new tile(); // wall
            tiles[2] = new tile(); // water
            tiles[3]=new tile(); //wood
            tiles[4]=new tile(); // tree1
            tiles[5]=new tile(); //sand

            tiles[0].image = ImageIO.read(getClass().getResourceAsStream("/res/tileset/grass.png"));
            tiles[1].image = ImageIO.read(getClass().getResourceAsStream("/res/tileset/wall.png"));
            tiles[1].collision = true;
            tiles[2].image = ImageIO.read(getClass().getResourceAsStream("/res/tileset/water_wave.png"));
            tiles[2].deadly = true;
            tiles[3].image = ImageIO.read(getClass().getResourceAsStream("/res/tileset/wooden.png"));
            tiles[3].collision = false;
            tiles[4].image = ImageIO.read(getClass().getResourceAsStream("/res/tileset/tree1.png"));
            tiles[4].collision = true;
            tiles[5].image = ImageIO.read(getClass().getResourceAsStream("/res/tileset/sand.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load map from text file
    public void loadMap(String filePath) {
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col=0;
            int row=0;

            while(col<gp.maxWorldCol && row<gp.maxWorldRow) {
                String line = br.readLine();
                while(col<gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //draw the tiles on the screen
    public void draw(Graphics g2) {

        int worldCol=0;
        int worldRow =0;

        while(worldCol<gp.maxWorldCol && worldRow <gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int x = worldX - gp.player.x + gp.player.screenX;
            int y = worldY - gp.player.y + gp.player.screenY;

            if(worldX+gp.tileSize>gp.player.x-gp.player.screenX &&
                    worldX-gp.tileSize<gp.player.x+gp.player.screenX &&
                    worldY+gp.tileSize>gp.player.y-gp.player.screenY &&
                    worldY-gp.tileSize<gp.player.y+gp.player.screenY) {
                g2.drawImage(tiles[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;

                worldRow++;
            }
        }
    }

}
