package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Tequila extends Entity {
    public Obj_Tequila(GamePanel gp) {
        super(gp);
        name="Tequila";
        gearType=2;
        down1=setup("objects/Tequila");
        alcohol=38;
        itemDescription = "["+name+"] Alc "+alcohol+"%"+"\nA Mexican fire potion.\nFree hangover and regret.";
    }
}
