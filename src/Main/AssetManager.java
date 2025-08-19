package Main;

import Entity.DrunkNPC;
//import Entity.DrunkNPC1;
import Entity.DrunkNPC1;
import Monster.MON_GSlime;
import object.*;

public class AssetManager {
    GamePanel gp;
    public AssetManager(GamePanel gp) {
        this.gp = gp;
    }
    //initializes the objects in the game
    public void setObj(){
    int i=0;
        gp.obj[i]=new Obj_Gold_Key(gp);
        gp.obj[i].x = 25 * gp.tileSize;
        gp.obj[i].y = 23 * gp.tileSize;
        i++;
        gp.obj[i]=new Obj_Gold_Key(gp);
        gp.obj[i].x = 21 * gp.tileSize;
        gp.obj[i].y = 19 * gp.tileSize;
        i++;
        gp.obj[i]=new Obj_Gold_Key(gp);
        gp.obj[i].x = 26 * gp.tileSize;
        gp.obj[i].y = 21 * gp.tileSize;
        i++;
        gp.obj[i]=new Obj_Iron_Sword(gp);
        gp.obj[i].x = 33 * gp.tileSize;
        gp.obj[i].y = 21 * gp.tileSize;
        i++;
        gp.obj[i]=new Obj_Hook(gp);
        gp.obj[i].x = 35 * gp.tileSize;
        gp.obj[i].y = 21 * gp.tileSize;
        i++;
        gp.obj[i]=new Obj_Heal_Potion(gp);
        gp.obj[i].x = 22 * gp.tileSize;
        gp.obj[i].y = 27 * gp.tileSize;
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
