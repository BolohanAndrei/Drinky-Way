package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Shield extends Entity{
    public static final String objName="Wooden Shield";
    public Obj_Shield(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=1;
        down1=setup("objects/shield");
        defenseValue=1;
        value=100;
        itemDescription="["+name+"] Arm +"+defenseValue+"\nStrong like soggy bread,\nbut at least it blocks insults.";
    }
}
