package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Ruby extends Entity {
    GamePanel gp;
    public Obj_Ruby(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name="Ruby";
        down1=setup("/objects/ruby");
        value=25;
    }
    public void use(Entity e){

        gp.sound.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
    }
}
