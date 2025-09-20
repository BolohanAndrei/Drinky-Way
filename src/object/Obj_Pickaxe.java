package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Pickaxe extends Entity {
    public static final String objName="Pickaxe";
    public Obj_Pickaxe(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=0;
        down1=setup("objects/pickaxe");
        attackValue=1;
        value=200;
        itemDescription="["+name+"] Atk +"+attackValue+"\n A fine pirate pickaxe,\nsharp enough to split\nskulls";
    }
}
