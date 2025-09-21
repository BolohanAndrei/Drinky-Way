package tiles;

import Main.GamePanel;
import Main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class tileManager {
    private final GamePanel gp;
    public tile[] tiles;
    public int[][][] mapTileNum;
    private final boolean[][][] mapCollision;
    private final int[] mapWidth;
    private final int[] mapHeight;

    private static final Color EDGE_COLOR = new Color(59,143,202);

    private BufferedImage terrainBuffer;
    private boolean terrainDirty = true;
    private int bufferedMap = -1;

    private final Utility util = new Utility();

    private final ArrayList<String> fileName = new ArrayList<>();
    private final ArrayList<String> collisionStatus = new ArrayList<>();

    private record MapSpec(String path, int index) {
    }

    public tileManager(GamePanel gp) {
        this.gp = gp;

        loadTileMeta();
        tiles = new tile[fileName.size()];
        loadTileImages();

        List<MapSpec> mapSpecs = List.of(
                new MapSpec("/res/maps/worldV3.txt",0),
                new MapSpec("/res/maps/interior01.txt",1),
                new MapSpec("/res/maps/dungeon01.txt",2),
                new MapSpec("/res/maps/dungeon02.txt",3),
                new MapSpec("/res/maps/tavern.txt",4)
        );

        int maxW = 0, maxH = 0;
        for (MapSpec spec : mapSpecs) {
            int[] wh = readMapDimensions(spec.path);
            if (wh != null) { maxW = Math.max(maxW, wh[0]); maxH = Math.max(maxH, wh[1]); }
        }
        if (maxW == 0 || maxH == 0) {
            maxW = gp.maxWorldCol;
            maxH = gp.maxWorldRow;
        }

        mapTileNum = new int[gp.maxMap][maxW][maxH];
        mapCollision = new boolean[gp.maxMap][maxW][maxH];
        mapWidth = new int[gp.maxMap];
        mapHeight = new int[gp.maxMap];

        for (MapSpec spec : mapSpecs) {
            loadMap(spec.path, spec.index);
        }

        gp.maxWorldCol = mapWidth[gp.currentMap] > 0 ? mapWidth[gp.currentMap] : maxW;
        gp.maxWorldRow = mapHeight[gp.currentMap] > 0 ? mapHeight[gp.currentMap] : maxH;
    }

    private void loadTileMeta(){
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("maps/tiledata.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileName.add(line.trim());
                String collisionLine = br.readLine();
                if (collisionLine == null) break;
                collisionStatus.add(collisionLine.trim());
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Failed to load tile metadata: "+ e.getMessage());
        }
    }

    private void loadTileImages() {
        for(int i=0;i<fileName.size();i++){
            String fName = fileName.get(i);
            boolean collision = "true".equalsIgnoreCase(collisionStatus.get(i));
            setup(i,fName,collision);
        }
    }

    private int[] readMapDimensions(String filePath){
        try(InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {
            String first = br.readLine();
            if(first == null) return null;
            String[] parts = first.trim().split(" ");
            int width = parts.length;
            int height = 1;
            while (br.readLine() != null) height++;
            return new int[]{width,height};
        } catch (Exception e) {
            System.err.println("Failed to read dimensions for "+filePath+": "+e.getMessage());
            return null;
        }
    }

    private void setup(int index,String path,boolean collision){
        try{
            tiles[index]=new tile();
            try(InputStream is = getClass().getResourceAsStream("/res/tileset/"+path)) {
                if(is!=null) {
                    tiles[index].image = ImageIO.read(is);
                }
            }
            if(tiles[index].image!=null){
                tiles[index].image = util.scaleImage(tiles[index].image, gp.tileSize, gp.tileSize);
            } else {
                tiles[index].image = new BufferedImage(gp.tileSize,gp.tileSize,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = tiles[index].image.createGraphics();
                g.setColor(Color.MAGENTA); g.fillRect(0,0,gp.tileSize,gp.tileSize); g.dispose();
            }
            tiles[index].collision = collision;
        }catch(Exception e){
            System.err.println("Failed to load tile image: "+path+" -> "+e.getMessage());
        }
    }

    private void loadMap(String filePath,int map) {
        if(map < 0 || map >= gp.maxMap) return;
        int[] wh = readMapDimensions(filePath);
        if(wh == null) return;
        int width = wh[0];
        int height = wh[1];
        mapWidth[map] = width;
        mapHeight[map] = height;
        try(InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {
            for(int row=0; row<height; row++) {
                String line = br.readLine();
                if(line == null) break;
                String[] numbers = line.trim().split(" ");
                for(int col=0; col<width; col++) {
                    if(col >= numbers.length) break; // safety
                    int num = Integer.parseInt(numbers[col]);
                    if(num < 0 || num >= tiles.length) num = 0;
                    mapTileNum[map][col][row]=num;
                    mapCollision[map][col][row]= tiles[num].collision;
                }
            }
        }catch(Exception e){
            System.err.println("Failed to load map "+filePath+": "+e.getMessage());
        }
        if(map==gp.currentMap){
            gp.maxWorldCol = width;
            gp.maxWorldRow = height;
            terrainDirty = true;
            bufferedMap = map;
        }
    }

    public boolean isCollision(int map,int col,int row){
        if(map<0||map>=gp.maxMap) return true;
        int w = mapWidth[map];
        int h = mapHeight[map];
        if(col<0||row<0||col>=w||row>=h) return true;
        return mapCollision[map][col][row];
    }

    private void rebuildTerrainIfNeeded(){
        if(!terrainDirty || gp.currentMap!=bufferedMap) return;
        int worldCols = mapWidth[gp.currentMap];
        int worldRows = mapHeight[gp.currentMap];
        if(worldCols<=0 || worldRows<=0) return;
        int w = worldCols * gp.tileSize;
        int h = worldRows * gp.tileSize;
        if(terrainBuffer==null || terrainBuffer.getWidth()!=w || terrainBuffer.getHeight()!=h){
            terrainBuffer = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g = terrainBuffer.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setColor(new Color(0,0,0,0));
        g.fillRect(0,0,w,h);
        int ts = gp.tileSize;
        for(int row=0; row<worldRows; row++){
            int y = row*ts;
            for(int col=0; col<worldCols; col++){
                int tileNum = mapTileNum[gp.currentMap][col][row];
                if(tileNum<0 || tileNum>=tiles.length) continue;
                g.drawImage(tiles[tileNum].image, col*ts, y, null);
            }
        }
        g.dispose();
        terrainDirty = false;
    }

    public void draw(Graphics g2){
        final int ts = gp.tileSize;
        if(bufferedMap != gp.currentMap){ // map change detected
            gp.maxWorldCol = mapWidth[gp.currentMap];
            gp.maxWorldRow = mapHeight[gp.currentMap];
            bufferedMap = gp.currentMap;
            terrainDirty = true;
        }

        int worldCols = gp.maxWorldCol;
        int worldRows = gp.maxWorldRow;
        if(worldCols <=0 || worldRows <=0) return;

        int playerX = gp.player.x;
        int playerY = gp.player.y;
        int screenX = gp.player.screenX;
        int screenY = gp.player.screenY;
        int worldLeft = playerX - screenX;
        int worldTop = playerY - screenY;
        int worldRight = worldLeft + gp.screenWidth;
        int worldBottom = worldTop + gp.screenHeight;
        int worldPixelWidth = worldCols * ts;
        int worldPixelHeight = worldRows * ts;

        rebuildTerrainIfNeeded();
        if(terrainBuffer!=null){
            g2.drawImage(terrainBuffer, -worldLeft, -worldTop, null);
        }

        if(gp.currentMap!=0){
            g2.setColor(new Color(0,0,0,0));
        }else{
            g2.setColor(EDGE_COLOR);
        }
        if (worldLeft < 0) { int w = -worldLeft; g2.fillRect(0, 0, w, gp.screenHeight); }
        if (worldRight > worldPixelWidth) { int w = worldRight - worldPixelWidth; g2.fillRect(gp.screenWidth - w, 0, w, gp.screenHeight); }
        if (worldTop < 0) { int h2 = -worldTop; g2.fillRect(0, 0, gp.screenWidth, h2); }
        if (worldBottom > worldPixelHeight) { int h2 = worldBottom - worldPixelHeight; g2.fillRect(0, gp.screenHeight - h2, gp.screenWidth, h2); }
    }
}
