package object;

import Entity.Entity;
import Main.GamePanel;


public class Obj_Rum extends Entity {
    public Obj_Rum(GamePanel gp) {
        super(gp);
        name="Rum";
        gearType=2;
        down1=setup("objects/Rom");
        alcohol=40;
        itemDescription="["+name+"] Alc "+alcohol+"%"+"\nLiquid courage of pirates. Turns cowards into champions.";
    }
}
