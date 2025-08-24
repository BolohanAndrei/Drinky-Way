package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Axe extends Entity {
    public Obj_Axe(GamePanel gp) {
        super(gp);
        name="Axe";
        gearType=0;
        down1=setup("objects/pirate_axe");
        attackValue=1;
        itemDescription="["+name+"] Atk +"+attackValue+"\n A fine pirate axe, sharp enough to split skulls, coconuts or a bottle\nopener in emergencies.";
    }
}
