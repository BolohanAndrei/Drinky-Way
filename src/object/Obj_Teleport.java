package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Teleport extends Entity {
    public Obj_Teleport(GamePanel gp) {
        super(gp);
        name="Teleport";
        down1=setup("objects/teleport");
        pickable=false;

    }
}
