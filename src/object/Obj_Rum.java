package object;

import Entity.Entity;
import Main.GamePanel;


public class Obj_Rum extends Entity {
    public static final String objName="Rum";
    public Obj_Rum(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=2;
        down1=setup("objects/Rom");
        alcohol=40;
        value=50;
        stackable=true;
        itemDescription="["+name+"] Alc "+alcohol+"%"+"\nLiquid courage of pirates. Turns cowards into champions.";
    }
}
