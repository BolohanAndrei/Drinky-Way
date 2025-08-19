package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Drugs extends Entity {
    public Obj_Drugs(GamePanel gp) {
        super(gp);
        name="drugs";
        gearType=2;
        down1=setup("objects/drugs");
        alcohol=80;
        itemDescription = "["+name+"] Alc "+alcohol+"%"+"\nMagic dust. Every time is\na new mistery";
    }
}
