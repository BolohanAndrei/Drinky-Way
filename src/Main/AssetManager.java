package Main;

import Entity.DrunkNPC;
//import Entity.DrunkNPC1;
import Entity.DrunkNPC1;
import Monster.MON_GSlime;
import object.Obj_Door;

public class AssetManager {
    GamePanel gp;
    public AssetManager(GamePanel gp) {
        this.gp = gp;
    }
    //initializes the objects in the game
    public void setObj(){
//        gp.obj[0]=new Obj_Door(gp);
//        gp.obj[0].x=gp.tileSize*21;
//        gp.obj[0].y=gp.tileSize*22;
//
//        gp.obj[1]=new Obj_Door(gp);
//        gp.obj[1].x=gp.tileSize*23;
//        gp.obj[1].y=gp.tileSize*25;
    }

    public void setNPC(){
        gp.npc[0]=new DrunkNPC(gp);
        gp.npc[0].x = 21 * gp.tileSize;
        gp.npc[0].y = 21 * gp.tileSize;
        gp.npc[1]=new DrunkNPC1(gp);
        gp.npc[1].x = 23 * gp.tileSize;
        gp.npc[1].y = 21 * gp.tileSize;

    }

    public void setMonster(){
        gp.monster[0]=new MON_GSlime(gp);
        gp.monster[0].x = 23 * gp.tileSize;
        gp.monster[0].y = 36 * gp.tileSize;
        gp.monster[1]=new MON_GSlime(gp);
        gp.monster[1].x = 23 * gp.tileSize;
        gp.monster[1].y = 37 * gp.tileSize;
        gp.monster[2]=new MON_GSlime(gp);
        gp.monster[2].x = 24 * gp.tileSize;
        gp.monster[2].y = 35 * gp.tileSize;

    }
}
