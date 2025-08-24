package object;

import Entity.Projectile;
import Main.GamePanel;

public class Obj_Dagger extends Projectile {
    GamePanel gp;
    public Obj_Dagger(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name="Dagger";
        speed=8;
        maxHealth=30;
        health=maxHealth;
        attackValue=2;
        useCost=1;
        alive=false;
        getImage();
    }

    public void getImage(){
        up1=setup("objects/dagger_up");
        up2=setup("objects/dagger_up");
        down1=setup("objects/dagger_down");
        down2=setup("objects/dagger_down");
        left1=setup("objects/dagger_left");
        left2=setup("objects/dagger_left");
        right1=setup("objects/dagger_right");
        right2=setup("objects/dagger_right");

    }
}
