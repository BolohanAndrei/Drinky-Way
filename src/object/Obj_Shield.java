package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Shield extends Entity{

    public Obj_Shield(GamePanel gp) {
        super(gp);
        name="Wooden Shield";
        down1=setup("objects/shield");
        defenseValue=1;
        itemDescription="["+name+"] Arm +"+defenseValue+"\nStrong like soggy bread, but at least it blocks insults.";
    }
}
