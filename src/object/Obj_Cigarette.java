package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Cigarette extends Entity {
    public Obj_Cigarette(GamePanel gp) {
        super(gp);
        name="cigarette";
        down1=setup("objects/cigarette");
        alcohol=5;
        itemDescription = "["+name+"] Alc "+alcohol+"%"+"\nA smoky stick of false bravery. Warning: May summon coughing fits and cool poses.";

    }
}
