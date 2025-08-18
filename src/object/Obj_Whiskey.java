package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Whiskey extends Entity {
    public Obj_Whiskey(GamePanel gp) {
        super(gp);
        name="Whiskey";
        down1=setup("objects/Whiskey");
        alcohol=40;
        itemDescription = "["+name+"] Alc "+alcohol+"%"+"\nGolden pirate wisdom in a bottle. May cause deep philosophy and bar fights.";
    }
}
