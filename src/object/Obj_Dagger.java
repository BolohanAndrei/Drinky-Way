package object;

import Entity.Projectile;
import Main.GamePanel;

import java.awt.*;

public class Obj_Dagger extends Projectile {
    GamePanel gp;
    public Obj_Dagger(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name="Dagger";
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
