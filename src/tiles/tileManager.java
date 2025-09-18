package tiles;

import Main.GamePanel;
import Main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class tileManager {
    GamePanel gp;
    public tile[] tiles;
    public int[][][] mapTileNum;
    private final boolean[][][] mapCollision;

    private static final Color EDGE_COLOR = new Color(59,143,202);

    private BufferedImage terrainBuffer;
    private boolean terrainDirty = true;
    private int bufferedMap = -1;

    private BufferedImage tileAtlas;
    private int[] atlasX;
    private boolean atlasReady = false;

    private final Utility util = new Utility();

    public tileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new tile[100];
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        mapCollision = new boolean[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/res/maps/worldV3.txt",0);
        loadMap("/res/maps/interior01.txt",1);
    }

    public void getTileImage() {
        try {
            setup(0, "grass00", false); setup(1, "grass00", false); setup(2, "grass00", false); setup(3, "grass00", false);
            setup(4, "grass00", false); setup(5, "grass00", false); setup(6, "grass00", false); setup(7, "grass00", false);
            setup(9, "grass00", false); setup(10, "grass00", false); setup(11, "grass01", false);
            setup(12, "water00", true); setup(13, "water01", true); setup(14, "water02", true); setup(15, "water03", true);
            setup(16, "water04", true); setup(17, "water05", true); setup(18, "water06", true); setup(19, "water07", true);
            setup(20, "water08", true); setup(21, "water09", true); setup(22, "water10", true); setup(23, "water11", true);
            setup(24, "water12", true); setup(25, "water13", true);
            setup(26, "road00", false); setup(27, "road01", false); setup(28, "road02", false); setup(29, "road03", false);
            setup(30, "road04", false); setup(31, "road05", false); setup(32, "road06", false); setup(33, "road07", false);
            setup(34, "road08", false); setup(35, "road09", false); setup(36, "road10", false); setup(37, "road11", false);
            setup(38, "road12", false); setup(39, "earth", false); setup(40, "wall", true); setup(41, "tree", true);
            setup(42, "hut", false); setup(43, "floor01", false); setup(44, "table01", true);
        } catch (Exception ignored) {}
        buildAtlas();
    }

    private void buildAtlas() {
        int count = 0; int ts = gp.tileSize;
        for (tile tile : tiles) if (tile != null && tile.image != null) count++;
        if(count==0) return;
        tileAtlas = new BufferedImage(ts*count, ts, BufferedImage.TYPE_INT_ARGB);
        atlasX = new int[tiles.length];
        Graphics2D g = tileAtlas.createGraphics();
        int col = 0;
        for(int i=0;i<tiles.length;i++){
            if(tiles[i]==null || tiles[i].image==null) continue;
            int x = col*ts; atlasX[i]=x; g.drawImage(tiles[i].image,x,0,null); col++;
        }
        g.dispose();
        atlasReady = true;
    }

    public void setup(int index,String path,boolean collision){
        try{
            tiles[index]=new tile();
            InputStream is = getClass().getResourceAsStream("/res/tileset/"+path+".png");
            if(is!=null) tiles[index].image = ImageIO.read(is);
            if(tiles[index].image!=null){
                tiles[index].image = util.scaleImage(tiles[index].image, gp.tileSize, gp.tileSize);
            } else {
                tiles[index].image = new BufferedImage(gp.tileSize,gp.tileSize,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = tiles[index].image.createGraphics();
                g.setColor(Color.MAGENTA); g.fillRect(0,0,gp.tileSize,gp.tileSize); g.dispose();
            }
            tiles[index].collision = collision;
        }catch(Exception ignored){}
    }

    public void loadMap(String filePath,int map) {
        try(InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {
            int col=0,row=0; String line;
            while(col<gp.maxWorldCol && row<gp.maxWorldRow && (line = br.readLine())!=null){
                String[] numbers = line.split(" ");
                while(col<gp.maxWorldCol){
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[map][col][row]=num;
                    mapCollision[map][col][row]= tiles[num].collision;
                    col++;
                }
                if(col==gp.maxWorldCol){ col=0; row++; }
            }
        }catch(Exception ignored){}
        if(map==gp.currentMap){ terrainDirty = true; bufferedMap = map; }
    }

    public boolean isCollision(int map,int col,int row){
        if(map<0||map>=gp.maxMap||col<0||col>=gp.maxWorldCol||row<0||row>=gp.maxWorldRow) return true;
        return mapCollision[map][col][row];
    }
    private void rebuildTerrainIfNeeded(){
        if(!terrainDirty || gp.currentMap!=bufferedMap) return;
        int w = gp.maxWorldCol * gp.tileSize;
        int h = gp.maxWorldRow * gp.tileSize;
        if(terrainBuffer==null || terrainBuffer.getWidth()!=w || terrainBuffer.getHeight()!=h){
            terrainBuffer = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g = terrainBuffer.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setColor(new Color(0,0,0,0));
        g.fillRect(0,0,w,h);
        int ts = gp.tileSize;
        if(atlasReady){
            for(int row=0; row<gp.maxWorldRow; row++){
                int y = row*ts;
                for(int col=0; col<gp.maxWorldCol; col++){
                    int tileNum = mapTileNum[gp.currentMap][col][row];
                    if(tileNum<0 || tileNum>=tiles.length) continue;
                    int sx = atlasX[tileNum];
                    g.drawImage(tileAtlas, col*ts, y, col*ts+ts, y+ts, sx,0,sx+ts,ts,null);
                }
            }
        } else {
            for(int row=0; row<gp.maxWorldRow; row++){
                int y = row*ts;
                for(int col=0; col<gp.maxWorldCol; col++){
                    int tileNum = mapTileNum[gp.currentMap][col][row];
                    g.drawImage(tiles[tileNum].image, col*ts, y, null);
                }
            }
        }
        g.dispose();
        terrainDirty = false;
    }

    public void draw(Graphics g2){
        final int ts = gp.tileSize;
        int playerX = gp.player.x;
        int playerY = gp.player.y;
        int screenX = gp.player.screenX;
        int screenY = gp.player.screenY;
        int worldLeft = playerX - screenX;
        int worldTop = playerY - screenY;
        int worldRight = worldLeft + gp.screenWidth;
        int worldBottom = worldTop + gp.screenHeight;
        int worldPixelWidth = gp.maxWorldCol * ts;
        int worldPixelHeight = gp.maxWorldRow * ts;

        if(bufferedMap != gp.currentMap){ bufferedMap = gp.currentMap; terrainDirty = true; }


            rebuildTerrainIfNeeded();
            if(terrainBuffer!=null){
                g2.drawImage(terrainBuffer, -worldLeft, -worldTop, null);
            }

        g2.setColor(EDGE_COLOR);
        if (worldLeft < 0) { int w = -worldLeft; g2.fillRect(0, 0, w, gp.screenHeight); }
        if (worldRight > worldPixelWidth) { int w = worldRight - worldPixelWidth; g2.fillRect(gp.screenWidth - w, 0, w, gp.screenHeight); }
        if (worldTop < 0) { int h = -worldTop; g2.fillRect(0, 0, gp.screenWidth, h); }
        if (worldBottom > worldPixelHeight) { int h = worldBottom - worldPixelHeight; g2.fillRect(0, gp.screenHeight - h, gp.screenWidth, h); }
    }
}
