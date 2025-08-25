package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Amethyst extends Entity {
    GamePanel gp;
    public Obj_Amethyst(GamePanel gp) {
        super(gp);
        this.gp=gp;

        gearType=3;
        name="Amethyst";
        down1=setup("/objects/amethyst");
        value=15;
    }
    public void use(Entity e){

        gp.sound.playSE(25);
        gp.ui.addMessage("Coin"+" +" + value);
        gp.player.coin+=value;
    }
}
