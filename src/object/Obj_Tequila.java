package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Tequila extends Entity {
    public static final String objName="Tequila";
    public Obj_Tequila(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=2;
        down1=setup("objects/Tequila");
        alcohol=38;
        value=50;
        stackable=true;
        itemDescription = "["+name+"] Alc "+alcohol+"%"+"\nA Mexican fire potion.\nFree hangover and regret.";
    }
}
