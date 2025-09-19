package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Iron_Sword extends Entity {

    public static final String objName="Iron Sword";

    public Obj_Iron_Sword(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=0;
        down1=setup("objects/iron_sword");
        attackValue=2;
        value=200;
        itemDescription="["+name+"] Atk +"+attackValue+"\nHeavy, sharp, and\ndefinitely compensating\nfor something. Makes a\nfine bottle opener too.";
    }
}
