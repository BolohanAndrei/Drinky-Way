package Main;

import object.*;

public class ObjManager {
    GamePanel gp;
    public ObjManager(GamePanel gp) {
        this.gp = gp;
    }
    //initializes the objects in the game
    public void setObj(){
        gp.obj[0] = new Obj_Gold_Key();
        gp.obj[0].worldX=23 * gp.tileSize;
        gp.obj[0].worldY=7 * gp.tileSize;

        gp.obj[1] = new Obj_Silver_Key();
        gp.obj[1].worldX=23 * gp.tileSize;
        gp.obj[1].worldY=40 * gp.tileSize;

        gp.obj[2] = new Obj_Emerald_Key();
        gp.obj[2].worldX=38 * gp.tileSize;
        gp.obj[2].worldY=9 * gp.tileSize;

        gp.obj[3] = new Obj_Door();
        gp.obj[3].worldX=10 * gp.tileSize;
        gp.obj[3].worldY=11 * gp.tileSize;

        gp.obj[4] = new Obj_Door();
        gp.obj[4].worldX=12 * gp.tileSize;
        gp.obj[4].worldY=22 * gp.tileSize;

        gp.obj[5] = new Obj_Chest();
        gp.obj[5].worldX=10 * gp.tileSize;
        gp.obj[5].worldY=7 * gp.tileSize;

        gp.obj[6] = new Obj_Skull_Door();
        gp.obj[6].worldX=23 * gp.tileSize;
        gp.obj[6].worldY=35 * gp.tileSize;

        gp.obj[7] = new Obj_Armour_Boots_Crusty();
        gp.obj[7].worldX=37 * gp.tileSize;
        gp.obj[7].worldY=42 * gp.tileSize;

        gp.obj[8] = new Obj_Gold_Key();
        gp.obj[8].worldX=11 * gp.tileSize;
        gp.obj[8].worldY=32 * gp.tileSize;


    }
}
