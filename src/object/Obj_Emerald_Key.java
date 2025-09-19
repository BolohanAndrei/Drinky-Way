package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Emerald_Key extends Entity {
    public static final String objName="Emerald_Key";
    public Obj_Emerald_Key(GamePanel gp) {
        super(gp);
        name = objName;
        down1=setup("objects/emerald_key");
        value=1000;
        stackable=true;
    }
}
