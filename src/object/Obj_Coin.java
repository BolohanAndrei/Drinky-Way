package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Coin extends Entity {
    GamePanel gp;
    public Obj_Coin(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name="Coin";
        down1=setup("/objects/coin");
        value=1;
    }
    public boolean use(Entity e){

        gp.se.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
        return true;
    }
}
