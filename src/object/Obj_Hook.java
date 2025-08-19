package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Hook extends Entity {
    public Obj_Hook(GamePanel gp) {
        super(gp);
        name="Hook";
        gearType=0;
        down1=setup("objects/hook");
        attackValue=1;
        dexterityBonus=1;
        itemDescription = "["+name+"] Atk +"+attackValue+" Dxt +"+dexterity+"\nStylish hand replacement. Useful for fighting, scratching backs, and opening beer bottles.";

    }
}
