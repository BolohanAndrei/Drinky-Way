package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Hook extends Entity {
    public static final String objName="Hook";
    public Obj_Hook(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=0;
        down1=setup("objects/hook");
        attackValue=1;
        dexterityBonus=1;
        value=100;
        itemDescription = "["+name+"] Atk +"+attackValue+" Dxt +"+dexterity+"\nStylish hand replacement. Useful for fighting, scratching backs, and opening beer bottles.";

    }
}
