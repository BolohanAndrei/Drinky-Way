package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Whiskey extends Entity {
    public static final String objName="Whiskey";
    public Obj_Whiskey(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=2;
        down1=setup("objects/whiskey");
        alcohol=40;
        value=50;
        stackable=true;
        itemDescription = "["+name+"] Alc "+alcohol+"%"+"\nGolden pirate wisdom in a bottle. May cause deep philosophy and bar fights.";
    }
}
