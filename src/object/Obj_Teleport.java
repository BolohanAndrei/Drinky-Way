package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Teleport extends Entity {
    public static final String objName="Teleport";
    public Obj_Teleport(GamePanel gp) {
        super(gp);
        name=objName;
        down1=setup("objects/teleport");
        pickable=false;

    }
}
