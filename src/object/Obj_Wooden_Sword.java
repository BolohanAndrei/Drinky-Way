package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Wooden_Sword extends Entity{
    public static final String objName="Wooden Sword";
    public Obj_Wooden_Sword(GamePanel gp) {
        super(gp);
        name=objName;
        gearType=0;
        down1=setup("objects/wooden_sword");
        attackValue=1;
        value=100;
        itemDescription="["+name+"] Atk +"+attackValue+"\n A mighty stick of doom, sharp enough to tickle\nyour enemies into regret.";
    }
}
