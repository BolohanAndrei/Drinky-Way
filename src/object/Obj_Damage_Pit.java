package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Damage_Pit extends Entity {
    public Obj_Damage_Pit(GamePanel gp) {
        super(gp);
        name="Damage Pit";
        down1=setup("objects/pit");
        pickable=false;

        solidArea.x=0;
        solidArea.y=16;
        solidArea.width=48;
        solidArea.height=32;
        solidAreaDefaultX=solidArea.x;
        solidAreaDefaultY=solidArea.y;


    }
}
