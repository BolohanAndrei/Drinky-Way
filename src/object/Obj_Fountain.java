package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Fountain extends Entity {
    public static final String objName="Heal Fountain";
    public Obj_Fountain(GamePanel gp) {
        super(gp);
        name=objName;
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
