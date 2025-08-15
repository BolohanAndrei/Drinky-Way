package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Skull_Door extends Entity {
    public Obj_Skull_Door(GamePanel gp) {
        super(gp);
        name = "Skull_Door";
        collision = true;
       down1=setup("objects/skull_door");
    }
}
