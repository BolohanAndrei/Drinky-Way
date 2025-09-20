package Main;

import Entity.Entity;
import object.*;

public class EntityGen {
    GamePanel gp;
    public EntityGen(GamePanel gp) {
        this.gp=gp;
    }
    public Entity getObject(String itemName){
        return switch (itemName) {
            case Obj_Armour_Boots_Crusty.objName -> new Obj_Armour_Boots_Crusty(gp);
            case Obj_Armour_Chest_Crusty.objName -> new Obj_Armour_Chest_Crusty(gp);
            case Obj_Armour_Helmet_Crusty.objName -> new Obj_Armour_Helmet_Crusty(gp);
            case Obj_Alcohol.objName -> new Obj_Alcohol(gp);
            case Obj_Amethyst.objName -> new Obj_Amethyst(gp);
            case Obj_Amber.objName -> new Obj_Amber(gp);
            case Obj_Axe.objName -> new Obj_Axe(gp);
            case Obj_Beer.objName -> new Obj_Beer(gp);
            case Obj_Chest.objName ->  new Obj_Chest(gp);
            case Obj_Cigarette.objName -> new Obj_Cigarette(gp);
            case Obj_Coin.objName -> new Obj_Coin(gp);
            case Obj_Cup.objName -> new Obj_Cup(gp);
            case Obj_Dagger.objName -> new Obj_Dagger(gp);
            case Obj_Damage_Pit.objName -> new Obj_Damage_Pit(gp);
            case Obj_Diamond.objName -> new Obj_Diamond(gp);
            case Obj_Door.objName ->  new Obj_Door(gp);
            case Obj_Drugs.objName -> new Obj_Drugs(gp);
            case Obj_Emerald.objName -> new Obj_Emerald(gp);
            case Obj_Emerald_Key.objName -> new Obj_Emerald_Key(gp);
            case Obj_Fountain.objName -> new Obj_Fountain(gp);
            case Obj_Gold_Key.objName -> new Obj_Gold_Key(gp);
            case Obj_Heal_Potion.objName -> new Obj_Heal_Potion(gp);
            case Obj_Heart.objName -> new Obj_Heart(gp);
            case Obj_Hook.objName -> new Obj_Hook(gp);
            case Obj_Iron_Sword.objName -> new Obj_Iron_Sword(gp);
            case Obj_Quartz.objName -> new Obj_Quartz(gp);
            case Obj_Ruby.objName -> new Obj_Ruby(gp);
            case Obj_Rum.objName -> new Obj_Rum(gp);
            case Obj_Sapphire.objName -> new Obj_Sapphire(gp);
            case Obj_Shield.objName -> new Obj_Shield(gp);
            case Obj_Silver_Key.objName -> new Obj_Silver_Key(gp);
            case Obj_SlimeProjectile.objName -> new Obj_SlimeProjectile(gp);
            case Obj_Tequila.objName -> new Obj_Tequila(gp);
            case Obj_Teleport.objName -> new Obj_Teleport(gp);
            case Obj_Whiskey.objName -> new Obj_Whiskey(gp);
            case Obj_Wooden_Sword.objName -> new Obj_Wooden_Sword(gp);
            case Obj_Pickaxe.objName -> new Obj_Pickaxe(gp);
            case Obj_Door_Iron.objName -> new Obj_Door_Iron(gp);
            default -> null;
        };
    }
}
