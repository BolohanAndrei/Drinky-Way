package Data;

import Entity.Entity;
import Main.GamePanel;
import object.*;

import java.io.*;

public class SaveLoad {
    GamePanel gp;
    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }
    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));
            DataStorage ds = (DataStorage) ois.readObject();

            gp.player.level = ds.level;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.exp = ds.exp;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.speed = ds.speed;
            gp.player.coin = ds.coin;
            gp.player.health = ds.health;
            gp.player.maxHealth = ds.maxHealth;
            gp.player.drunk = ds.drunk;
            gp.player.maxDrunk = ds.maxDrunk;
            gp.player.drinkPercent = ds.drinkPercent;

            gp.player.inventory.clear();
            for (int i = 0; i < ds.itemNames.size(); i++) {
                Entity item = getObject(ds.itemNames.get(i));
                if (item != null) {
                    item.amount = ds.itemAmounts.get(i);
                    gp.player.inventory.add(item);
                }
            }

            gp.player.currentWeapon = getItemFromInventory(ds.currentWeaponSlot);
            gp.player.currentShield = getItemFromInventory(ds.currentShieldSlot);
            gp.player.currentBoots = getItemFromInventory(ds.currentBootsSlot);
            gp.player.currentChest = getItemFromInventory(ds.currentChestSlot);
            gp.player.currentHelmet = getItemFromInventory(ds.currentHelmetSlot);

            for (int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                for (int i = 0; i < gp.obj[mapNum].length; i++) {
                    if (ds.mapObjectNames[mapNum][i] == null || ds.mapObjectNames[mapNum][i].equals("N/A")) {
                        gp.obj[mapNum][i] = null;
                    } else {
                        Entity entity = getObject(ds.mapObjectNames[mapNum][i]);
                        if (entity != null) {
                            gp.obj[mapNum][i] = entity;
                            gp.obj[mapNum][i].x = ds.mapObjectX[mapNum][i];
                            gp.obj[mapNum][i].y = ds.mapObjectY[mapNum][i];
                            if (entity instanceof Obj_Chest && ds.chestItemNames != null && !ds.chestItemNames.isEmpty()) {
                                Obj_Chest chest = (Obj_Chest) entity;
                                chest.inventory.clear();
                                for (int j = 0; j < ds.chestItemNames.size(); j++) {
                                    Entity item = getObject(ds.chestItemNames.get(j));
                                    if (item != null) {
                                        item.amount = ds.chestItemAmounts.get(j);
                                        chest.inventory.add(item);
                                    }
                                }
                            }
                        } else {
                            gp.obj[mapNum][i] = null;
                        }
                    }
                }
            }

            gp.player.reStats();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
            DataStorage ds=new DataStorage();

            ds.level=gp.player.level;
            ds.nextLevelExp=gp.player.nextLevelExp;
            ds.exp=gp.player.exp;
            ds.strength=gp.player.strength;
            ds.dexterity=gp.player.dexterity;
            ds.speed=gp.player.speed;
            ds.coin=gp.player.coin;
            ds.health=gp.player.health;
            ds.maxHealth=gp.player.maxHealth;
            ds.drunk=gp.player.drunk;
            ds.maxDrunk=gp.player.maxDrunk;
            ds.drinkPercent=gp.player.drinkPercent;

            for(int i=0;i<gp.player.inventory.size();i++)
            {
                ds.itemNames.add(gp.player.inventory.get(i).name);
                ds.itemAmounts.add(gp.player.inventory.get(i).amount);
            }

            ds.currentWeaponSlot=gp.player.getSlotForGear(gp.player.currentWeapon);
            ds.currentShieldSlot=gp.player.getSlotForGear(gp.player.currentShield);
            ds.currentBootsSlot=gp.player.getSlotForGear(gp.player.currentBoots);
            ds.currentChestSlot=gp.player.getSlotForGear(gp.player.currentChest);
            ds.currentHelmetSlot=gp.player.getSlotForGear(gp.player.currentHelmet);

            ds.mapObjectNames=new String[gp.maxMap][gp.obj[gp.currentMap].length];
            ds.mapObjectX=new int[gp.maxMap][gp.obj[gp.currentMap].length];
            ds.mapObjectY=new int[gp.maxMap][gp.obj[gp.currentMap].length];

            for(int mapNum=0;mapNum<gp.maxMap;mapNum++){
                for(int i=0;i<gp.obj[gp.currentMap].length;i++){
                    if(gp.obj[mapNum][i]==null) {
                        ds.mapObjectNames[mapNum][i]="N/A";

                    }else{
                        ds.mapObjectNames[mapNum][i]=gp.obj[mapNum][i].name;
                        ds.mapObjectX[mapNum][i]=gp.obj[mapNum][i].x;
                        ds.mapObjectY[mapNum][i]=gp.obj[mapNum][i].y;
                        if (gp.obj[mapNum][i] instanceof Obj_Chest) {
                            Obj_Chest chest = (Obj_Chest) gp.obj[mapNum][i];
                            if (!chest.inventory.isEmpty()) {
                                for (int j = 0; j < chest.inventory.size(); j++) {
                                    ds.chestItemNames.add(chest.inventory.get(j).name);
                                    ds.chestItemAmounts.add(chest.inventory.get(j).amount);
                                }
                            }
                            }
                    }
                }
            }

            oos.writeObject(ds);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Entity getObject(String itemName){
        return switch (itemName) {
            case "Crusty Boots" -> new Obj_Armour_Boots_Crusty(gp);
            case "Crusty Chest" -> new Obj_Armour_Chest_Crusty(gp);
            case "Crusty Helmet" -> new Obj_Armour_Helmet_Crusty(gp);
            case "Axe" -> new Obj_Axe(gp);
            case "beer" -> new Obj_Beer(gp);
            case "cigarette" -> new Obj_Cigarette(gp);
            case "drugs" -> new Obj_Drugs(gp);
            case "Emerald_Key" -> new Obj_Emerald_Key(gp);
            case "Gold Key" -> new Obj_Gold_Key(gp);
            case "Heal Potion" -> new Obj_Heal_Potion(gp);
            case "Hook" -> new Obj_Hook(gp);
            case "Iron Sword" -> new Obj_Iron_Sword(gp);
            case "Rum" -> new Obj_Rum(gp);
            case "Wooden Shield" -> new Obj_Shield(gp);
            case "Silver_Key" -> new Obj_Silver_Key(gp);
            case "Tequila" -> new Obj_Tequila(gp);
            case "Whiskey" -> new Obj_Whiskey(gp);
            case "Wooden Sword" -> new Obj_Wooden_Sword(gp);
            case "Door" -> new Obj_Door(gp);
            case "Chest" -> new Obj_Chest(gp);
            default -> null;
        };
    }
    private Entity getItemFromInventory(int slot) {
        return (slot >= 0 && slot < gp.player.inventory.size())
                ? gp.player.inventory.get(slot)
                : null;
    }
}
