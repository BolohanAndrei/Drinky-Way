package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Cigarette extends Entity {
    public static final String objName="cigarette";
    public Obj_Cigarette(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=2;
        down1=setup("objects/cigarette");
        value=5;
        alcohol=5;
        stackable=true;
        itemDescription = "["+name+"] Alc "+alcohol+"%"+"\nA smoky stick of false bravery. Warning: May summon coughing fits and cool poses.";

    }
}
