package object;

import Main.GamePanel;
import Entity.Entity;

public class Obj_Armour_Boots_Crusty extends Entity{
    public Obj_Armour_Boots_Crusty(GamePanel gp) {
        super(gp);
        name = "Crusty Boots";
        armourType=2;
       down1=setup("objects/armour_boots_crusty");
       defenseValue=1;
        itemDescription = "["+name+"] Arm +"+defenseValue+"\nBoots older than the captain’s liver. Smell strong enough to stun enemies.";
    }}
