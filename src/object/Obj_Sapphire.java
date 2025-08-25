package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Sapphire extends Entity {
    GamePanel gp;
    public Obj_Sapphire(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name="Sapphire";
        down1=setup("/objects/sapphire");
        value=20;
    }
    public void use(Entity e){

        gp.sound.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
    }
}
