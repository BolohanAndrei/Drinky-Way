package object;

import Main.GamePanel;
import Entity.Entity;

public class Obj_Armour_Chest_Crusty extends Entity{
    public static final String objName="Crusty Chest";
    public Obj_Armour_Chest_Crusty(GamePanel gp) {
        super(gp);
        name = objName;
        armourType=1;
        down1=setup("objects/armour_chest_crusty");
        defenseValue=1;
        value=100;
        itemDescription = "["+name+"] Arm +"+defenseValue+"\nArmor forged from disappointment. Stops arrows, but not\nheartbreak.";
    }}
