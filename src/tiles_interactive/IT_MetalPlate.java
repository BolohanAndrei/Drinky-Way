package tiles_interactive;

import Main.GamePanel;

import java.awt.*;

public class IT_MetalPlate extends InteractiveTiles{
    GamePanel gp;
    public static final String itName="Metal Plate";
    public IT_MetalPlate(GamePanel gp,int col,int row) {
        super(gp,col,row);
        this.gp=gp;
        name=itName;
        solidArea = new Rectangle(0, 0, 0, 0);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        this.x=gp.tileSize*col;
        this.y=gp.tileSize*row;
        down1=setup("/tiles_interactive/metalplate");
    }
}
