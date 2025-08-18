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
        int i=0;
        gp.npc[i]=new DrunkNPC(gp);
        gp.npc[i].x = 21 * gp.tileSize;
        gp.npc[i].y = 21 * gp.tileSize;
        i++;
        gp.npc[i]=new DrunkNPC1(gp);
        gp.npc[i].x = 23 * gp.tileSize;
        gp.npc[i].y = 21 * gp.tileSize;

    }

    public void setMonster(){
        int i=0;
        gp.monster[i]=new MON_GSlime(gp);
        gp.monster[i].x = 22 * gp.tileSize;
        gp.monster[i].y = 30 * gp.tileSize;
        i++;
        gp.monster[i]=new MON_GSlime(gp);
        gp.monster[i].x = 22 * gp.tileSize;
        gp.monster[i].y = 31 * gp.tileSize;
        i++;
        gp.monster[i]=new MON_GSlime(gp);
        gp.monster[i].x = 22 * gp.tileSize;
        gp.monster[i].y = 32 * gp.tileSize;
        i++;
        gp.monster[i]=new MON_GSlime(gp);
        gp.monster[i].x = 22 * gp.tileSize;
        gp.monster[i].y = 33 * gp.tileSize;
        i++;
        gp.monster[i]=new MON_GSlime(gp);
        gp.monster[i].x = 22 * gp.tileSize;
        gp.monster[i].y = 34 * gp.tileSize;
        i++;
        gp.monster[i]=new MON_GSlime(gp);
        gp.monster[i].x = 22 * gp.tileSize;
        gp.monster[i].y = 35 * gp.tileSize;
        i++;
        gp.monster[i]=new MON_GSlime(gp);
        gp.monster[i].x = 22 * gp.tileSize;
        gp.monster[i].y = 36 * gp.tileSize;

    }
}
