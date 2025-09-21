package Main;

import Entity.*;
import Monster.MON_GSlime;
import Monster.MON_BlueSlime;
import Monster.MON_Orc;
import object.*;
import tiles_interactive.IT_DryTree;
import tiles_interactive.IT_MetalPlate;
import tiles_interactive.IT_Wall;

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


        mapNum=2;
        i=0;
        gp.obj[mapNum][i]=new Obj_Chest(gp);
        gp.obj[mapNum][i].inventory.clear();
        gp.obj[mapNum][i].inventory.add(new Obj_Pickaxe(gp));
        gp.obj[mapNum][i].x = 40 * gp.tileSize;
        gp.obj[mapNum][i].y = 41 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Chest(gp);
        gp.obj[mapNum][i].inventory.clear();
        gp.obj[mapNum][i].inventory.add(new Obj_Heal_Potion(gp));
        gp.obj[mapNum][i].x = 13 * gp.tileSize;
        gp.obj[mapNum][i].y = 16 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Chest(gp);
        gp.obj[mapNum][i].inventory.clear();
        gp.obj[mapNum][i].inventory.add(new Obj_Iron_Sword(gp));
        gp.obj[mapNum][i].x = 26 * gp.tileSize;
        gp.obj[mapNum][i].y = 34 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Chest(gp);
        gp.obj[mapNum][i].inventory.clear();
        gp.obj[mapNum][i].inventory.add(new Obj_Armour_Boots_Crusty(gp));
        gp.obj[mapNum][i].x = 27 * gp.tileSize;
        gp.obj[mapNum][i].y = 15 * gp.tileSize;
        i++;
        gp.obj[mapNum][i]=new Obj_Door_Iron(gp);
        gp.obj[mapNum][i].x = 18 * gp.tileSize;
        gp.obj[mapNum][i].y = 23 * gp.tileSize;
    }

    public void setNPC(){
        int mapNum=0;
        int i=0;
        gp.npc[mapNum][i]=new DrunkNPC1(gp);
        gp.npc[mapNum][i].x = 21 * gp.tileSize;
        gp.npc[mapNum][i].y = 23 * gp.tileSize;
        i++;
        gp.npc[mapNum][i]=new DrunkNPC(gp);
        gp.npc[mapNum][i].x = 25 * gp.tileSize;
        gp.npc[mapNum][i].y = 23 * gp.tileSize;

        mapNum++;
        i=0;
        gp.npc[mapNum][i]=new MerchantNPC(gp);
        gp.npc[mapNum][i].x = 12 * gp.tileSize;
        gp.npc[mapNum][i].y = 7 * gp.tileSize;

        mapNum=2;
        i=0;

        gp.npc[mapNum][i]=new RockNPC(gp);
        gp.npc[mapNum][i].x = 20 * gp.tileSize;
        gp.npc[mapNum][i].y = 25 * gp.tileSize;
        i++;
        gp.npc[mapNum][i]=new RockNPC(gp);
        gp.npc[mapNum][i].x = 11 * gp.tileSize;
        gp.npc[mapNum][i].y = 18 * gp.tileSize;
        i++;
        gp.npc[mapNum][i]=new RockNPC(gp);
        gp.npc[mapNum][i].x = 23 * gp.tileSize;
        gp.npc[mapNum][i].y = 14 * gp.tileSize;

        mapNum=4;
        i=0;
        gp.npc[mapNum][i]=new MerchantNPC(gp);
        gp.npc[mapNum][i].inventory.clear();
        gp.npc[mapNum][i].inventory.add(new object.Obj_Beer(gp));
        gp.npc[mapNum][i].inventory.add(new object.Obj_Cigarette(gp));
        gp.npc[mapNum][i].inventory.add(new object.Obj_Drugs(gp));
        gp.npc[mapNum][i].inventory.add(new object.Obj_Tequila(gp));
        gp.npc[mapNum][i].inventory.add(new object.Obj_Rum(gp));
        gp.npc[mapNum][i].inventory.add(new object.Obj_Whiskey(gp));
        gp.npc[mapNum][i].x = 12 * gp.tileSize;
        gp.npc[mapNum][i].y = 7 * gp.tileSize;
        i++;
        gp.npc[mapNum][i]=new DiceGamberNPC(gp);
        gp.npc[mapNum][i].x = 21 * gp.tileSize;
        gp.npc[mapNum][i].y = 4 * gp.tileSize;
        i++;
        gp.npc[mapNum][i]=new DiceGamberNPC(gp);
        gp.npc[mapNum][i].diceGame.betOptions= new int[]{100, 200, 500, 1000, 2000};
        gp.npc[mapNum][i].x = 24 * gp.tileSize;
        gp.npc[mapNum][i].y = 4 * gp.tileSize;
        i++;
        gp.npc[mapNum][i]=new DiceGamberNPC(gp);
        gp.npc[mapNum][i].x = 21 * gp.tileSize;
        gp.npc[mapNum][i].y = 14 * gp.tileSize;
        i++;
        gp.npc[mapNum][i]=new DiceGamberNPC(gp);
        gp.npc[mapNum][i].diceGame.betOptions= new int[]{100, 200, 500, 1000, 2000};
        gp.npc[mapNum][i].x = 24 * gp.tileSize;
        gp.npc[mapNum][i].y = 14 * gp.tileSize;
        i++;
        gp.npc[mapNum][i]=new BlackJackGamblerNPC(gp);
        gp.npc[mapNum][i].x = 27 * gp.tileSize;
        gp.npc[mapNum][i].y = 14 * gp.tileSize;

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
        gp.monster[mapNum][i].x = 36 * gp.tileSize;
        gp.monster[mapNum][i].y = 11 * gp.tileSize;
        i++;
        gp.monster[mapNum][i]=new MON_BlueSlime(gp);
        gp.monster[mapNum][i].x = 38 * gp.tileSize;
        gp.monster[mapNum][i].y = 9 * gp.tileSize;
        i++;
        gp.monster[mapNum][i]=new MON_Orc(gp);
        gp.monster[mapNum][i].x = 11 * gp.tileSize;
        gp.monster[mapNum][i].y = 29 * gp.tileSize;
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

        mapNum=2;
        i=0;
        gp.iTile[mapNum][i]=new IT_Wall(gp,18,30); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,17,31); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,17,32); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,17,34); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,18,34); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,18,33); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,10,22); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,10,24); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,38,18); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,38,19); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,38,20); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,38,21); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,18,13); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,18,14); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,22,28); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,30,28); i++;
        gp.iTile[mapNum][i]=new IT_Wall(gp,32,28); i++;

        gp.iTile[mapNum][i]=new IT_MetalPlate(gp,20,22); i++;
        gp.iTile[mapNum][i]=new IT_MetalPlate(gp,8,17); i++;
        gp.iTile[mapNum][i]=new IT_MetalPlate(gp,39,31); i++;

    }
}
