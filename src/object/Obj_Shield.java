package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Shield extends Entity{

    public Obj_Shield(GamePanel gp) {
        super(gp);
        name="Wooden Shield";
        gearType=1;
        down1=setup("objects/shield");
        defenseValue=1;
        itemDescription="["+name+"] Arm +"+defenseValue+"\nStrong like soggy bread,\nbut at least it blocks insults.";
    }
}
