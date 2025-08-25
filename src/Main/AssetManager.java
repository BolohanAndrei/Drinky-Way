package Main;

import Entity.DrunkNPC;
//import Entity.DrunkNPC1;
import Entity.DrunkNPC1;
import Monster.MON_GSlime;
import object.*;
import tiles_interactive.IT_DryTree;

public class AssetManager {
    GamePanel gp;
    public AssetManager(GamePanel gp) {
        this.gp = gp;
    }
    public void setObj(){
    int i=0;
        i++;
        gp.obj[i]=new Obj_Axe(gp);
        gp.obj[i].x = 37 * gp.tileSize;
        gp.obj[i].y = 21 * gp.tileSize;
        i++;
        gp.obj[i]=new Obj_Hook(gp);
        gp.obj[i].x = 35 * gp.tileSize;
        gp.obj[i].y = 21 * gp.tileSize;
        i++;
        gp.obj[i]=new Obj_Damage_Pit(gp);
        gp.obj[i].x = 27 * gp.tileSize;
        gp.obj[i].y = 16 * gp.tileSize;
        i++;
        gp.obj[i]=new Obj_Fountain(gp);
        gp.obj[i].x = 23 * gp.tileSize;
        gp.obj[i].y = 11 * gp.tileSize;
        i++;
        gp.obj[i]=new Obj_Teleport(gp);
        gp.obj[i].x = 25 * gp.tileSize;
        gp.obj[i].y = 19 * gp.tileSize;
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

    public void setInteractiveTile(){
        int i=0;
        gp.iTile[i]=new IT_DryTree(gp,27,12);
        i++;
        gp.iTile[i]=new IT_DryTree(gp,28,12);
        i++;
        gp.iTile[i]=new IT_DryTree(gp,29,12);
        i++;
        gp.iTile[i]=new IT_DryTree(gp,30,12);
        i++;
        gp.iTile[i]=new IT_DryTree(gp,31,12);
        i++;
        gp.iTile[i]=new IT_DryTree(gp,32,12);
        i++;
        gp.iTile[i]=new IT_DryTree(gp,33,12);
        i++;

    }
}
