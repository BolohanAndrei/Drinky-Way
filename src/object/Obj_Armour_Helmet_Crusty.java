package object;

import Main.GamePanel;
import Entity.Entity;

public class Obj_Armour_Helmet_Crusty extends Entity{
    public Obj_Armour_Helmet_Crusty(GamePanel gp) {
        super(gp);
        name = "Crusty Helmet";
        armourType=0;
        down1=setup("objects/armour_helmet_crusty");
        defenseValue=1;
        itemDescription = "["+name+"] Arm +"+defenseValue+"\nA rusty bucket for your head. Protects against swords, not against bad ideas.";
    }}
