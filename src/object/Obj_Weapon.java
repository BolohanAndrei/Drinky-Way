package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Weapon extends Entity{

    public Obj_Weapon(GamePanel gp) {
        super(gp);
        name="Wooden Sword";
        down1=setup("objects/wooden_sword");
        attackValue=1;
        itemDescription="["+name+"] Atk +"+attackValue+"\n A mighty stick of doom, sharp enough to tickle\nyour enemies into regret.";
    }
}
