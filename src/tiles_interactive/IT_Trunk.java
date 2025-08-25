package tiles_interactive;

import Main.GamePanel;

import java.awt.*;

public class IT_Trunk extends InteractiveTiles{
    GamePanel gp;
    public IT_Trunk(GamePanel gp,int col,int row) {
        super(gp,col,row);
        this.gp=gp;
        solidArea = new Rectangle(0, 0, 0, 0);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        this.x=gp.tileSize*col;
        this.y=gp.tileSize*row;
        down1=setup("/tiles_interactive/trunk");
    }
}
