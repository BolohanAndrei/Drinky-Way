package object;

import Main.GamePanel;
import Entity.Entity;

public class Obj_Door extends Entity {

    public Obj_Door(GamePanel gp) {
        super(gp);
        name = "Door";
        collision = true;
       down1=setup("objects/pixel_door");

       solidArea.x=0;
       solidArea.y=16;
       solidArea.width=48;
       solidArea.height=32;
       solidAreaDefaultX=solidArea.x;
       solidAreaDefaultY=solidArea.y;
    }
}
