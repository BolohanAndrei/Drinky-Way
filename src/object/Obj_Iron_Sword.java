package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Iron_Sword extends Entity {

    public Obj_Iron_Sword(GamePanel gp) {
        super(gp);
        name="Iron Sword";
        gearType=0;
        down1=setup("objects/iron_sword");
        attackValue=2;
        itemDescription="["+name+"] Atk +"+attackValue+"\nHeavy, sharp, and definitely compensating\nfor something. Makes a\nfine bottle opener too.";
    }
}
