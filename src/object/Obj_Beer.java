package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Beer extends Entity {
    public Obj_Beer(GamePanel gp) {
        super(gp);
        name="beer";
        alcohol=5;
        down1=setup("objects/beer");
        itemDescription="["+name+"] Alc "+alcohol+"%\n"+" Ocean water’s tastier cousin. Keeps pirates hydrated and slightly stupid.\n";
    }
}
