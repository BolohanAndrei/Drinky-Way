package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Fountain extends Entity {
    public Obj_Fountain(GamePanel gp) {
        super(gp);
        name="Heal Fountain";
        down1=setup("objects/heal_pit2");
        collision=true;
        pickable=false;

        solidArea.x=0;
        solidArea.y=16;
        solidArea.width=48;
        solidArea.height=32;
        solidAreaDefaultX=solidArea.x;
        solidAreaDefaultY=solidArea.y;


    }
}
