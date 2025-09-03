package Main;

import Entity.DrunkNPC1;
import Entity.MerchantNPC;
import Monster.MON_GSlime;
import Monster.MON_BlueSlime;
import Monster.MON_Orc;
import object.*;
import tiles_interactive.IT_DryTree;

public class AssetManager {
    GamePanel gp;
    public AssetManager(GamePanel gp) {
        this.gp = gp;
    }
    public void setObj(){
        int mapNum=0;
    int i=0;
        i++;
        gp.obj[mapNum][i]=new Obj_Axe(gp);
        gp.obj[mapNum][i].x = 37 * gp.tileSize;
        gp.obj[mapNum][i].y = 21 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Hook(gp);
        gp.obj[mapNum][i].x = 35 * gp.tileSize;
        gp.obj[mapNum][i].y = 21 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Damage_Pit(gp);
        gp.obj[mapNum][i].x = 27 * gp.tileSize;
        gp.obj[mapNum][i].y = 16 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Fountain(gp);
        gp.obj[mapNum][i].x = 23 * gp.tileSize;
        gp.obj[mapNum][i].y = 11 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Teleport(gp);
        gp.obj[mapNum][i].x = 25 * gp.tileSize;
        gp.obj[mapNum][i].y = 19 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Door(gp);
        gp.obj[mapNum][i].x = 14 * gp.tileSize;
        gp.obj[mapNum][i].y = 28 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Door(gp);
        gp.obj[mapNum][i].x = 12 * gp.tileSize;
        gp.obj[mapNum][i].y = 12 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Chest(gp);
        gp.obj[mapNum][i].x = 12 * gp.tileSize;
        gp.obj[mapNum][i].y = 30 * gp.tileSize;
    }

    public void setNPC(){
        int mapNum=0;
        int i=0;
        gp.npc[mapNum][i]=new DrunkNPC1(gp);
        gp.npc[mapNum][i].x = 21 * gp.tileSize;
        gp.npc[mapNum][i].y = 23 * gp.tileSize;

        mapNum++;
        i++;
        gp.npc[mapNum][i]=new MerchantNPC(gp);
        gp.npc[mapNum][i].x = 12 * gp.tileSize;
        gp.npc[mapNum][i].y = 7 * gp.tileSize;


    }

    public void setMonster(){
        int mapNum=0;
        int i=0;
        gp.monster[mapNum][i]=new MON_GSlime(gp);
        gp.monster[mapNum][i].x = 22 * gp.tileSize;
        gp.monster[mapNum][i].y = 30 * gp.tileSize;
        i++;
        gp.monster[mapNum][i]=new MON_GSlime(gp);
        gp.monster[mapNum][i].x = 23 * gp.tileSize;
        gp.monster[mapNum][i].y = 34 * gp.tileSize;
        i++;
        gp.monster[mapNum][i]=new MON_BlueSlime(gp);
        gp.monster[mapNum][i].x = 24 * gp.tileSize;
        gp.monster[mapNum][i].y = 28 * gp.tileSize;
        i++;
        gp.monster[mapNum][i]=new MON_Orc(gp);
        gp.monster[mapNum][i].x = 23 * gp.tileSize;
        gp.monster[mapNum][i].y = 28 * gp.tileSize;
    }

    public void setInteractiveTile(){
        int mapNum=0;
        int i=0;
        gp.iTile[mapNum][i]=new IT_DryTree(gp,27,12);
        i++;
        gp.iTile[mapNum][i]=new IT_DryTree(gp,28,12);
        i++;
        gp.iTile[mapNum][i]=new IT_DryTree(gp,29,12);
        i++;
        gp.iTile[mapNum][i]=new IT_DryTree(gp,30,12);
        i++;
        gp.iTile[mapNum][i]=new IT_DryTree(gp,31,12);
        i++;
        gp.iTile[mapNum][i]=new IT_DryTree(gp,32,12);
        i++;
        gp.iTile[mapNum][i]=new IT_DryTree(gp,33,12);

    }
}
