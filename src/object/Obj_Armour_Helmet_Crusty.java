package object;

import Main.GamePanel;
import Entity.Entity;

public class Obj_Armour_Helmet_Crusty extends Entity{
    public static final String objName="Crusty Helmet";
    public Obj_Armour_Helmet_Crusty(GamePanel gp) {
        super(gp);
        name = objName;
        armourType=0;
        down1=setup("objects/armour_helmet_crusty");
        defenseValue=1;
        value=100;
        itemDescription = "["+name+"] Arm +"+defenseValue+"\nA rusty bucket for your head. Protects against swords, not against bad ideas.";
    }}
