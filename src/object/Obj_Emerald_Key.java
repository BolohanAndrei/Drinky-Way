package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Emerald_Key extends Entity {
    public Obj_Emerald_Key(GamePanel gp) {
        super(gp);
        name = "Emerald_Key";
        down1=setup("objects/emerald_key");
    }
}
