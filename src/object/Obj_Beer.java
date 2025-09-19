package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Beer extends Entity {

    public static final String objName="beer";

    public Obj_Beer(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=2;
        alcohol=5;
        value=5;
        stackable=true;
        down1=setup("objects/beer");
        itemDescription="["+name+"] Alc "+alcohol+"%\n"+" Ocean water’s tastier cousin. Keeps pirates hydrated and slightly stupid.\n";
    }
}
