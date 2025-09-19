package object;

import Entity.Projectile;
import Main.GamePanel;

import java.awt.*;

public class Obj_Dagger extends Projectile {
    GamePanel gp;
    public static final String objName="Dagger";
    public Obj_Dagger(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name=objName;
        speed=8;
        maxHealth=30;
        health=maxHealth;
        attackValue=1;
        alive=false;
        getImage();
    }

    public void getImage(){
        up1=setup("projectiles/dagger_up1");
        up2=setup("projectiles/dagger_up2");
        down1=setup("projectiles/dagger_down1");
        down2=setup("projectiles/dagger_down2");
        left1=setup("projectiles/dagger_left1");
        left2=setup("projectiles/dagger_left2");
        right1=setup("projectiles/dagger_right1");
        right2=setup("projectiles/dagger_right2");
        upLeft1=setup("projectiles/dagger_upLeft1");
        upLeft2=setup("projectiles/dagger_upLeft2");
        upRight1=setup("projectiles/dagger_upRight1");
        upRight2=setup("projectiles/dagger_upRight2");
        downLeft1=setup("projectiles/dagger_downLeft1");
        downLeft2=setup("projectiles/dagger_downLeft2");
        downRight1=setup("projectiles/dagger_downRight1");
        downRight2=setup("projectiles/dagger_downRight2");

    }

    public Color getParticleColor(){
        return new Color(81, 79, 79, 255);
    }

    public int getParticleSize(){
        return 10;
    }

    public int getParticleSpeed()
    {
        return 1;
    }

    public int getParticleMaxHealth(){
        return 20;
    }
}
