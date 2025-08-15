package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Chest extends Entity {
    public Obj_Chest(GamePanel gp) {
        super(gp);
        name = "Chest";
        collision = true;
        down1=setup("objects/chest");
    }
}
