package object;

import Entity.Projectile;
import Main.GamePanel;

import java.awt.*;

public class Obj_SlimeProjectile extends Projectile {
    GamePanel gp;
    public Obj_SlimeProjectile(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name="Slime Projectile";
        speed=5;
        maxHealth=60;
        health=maxHealth;
        attackValue=2;
        alive=false;
        getImage();
    }

    public void getImage(){
        up1=setup("projectiles/slime_projectile_up1");
        up2=setup("projectiles/slime_projectile_up2");
        down1=setup("projectiles/slime_projectile_down1");
        down2=setup("projectiles/slime_projectile_down2");
        left1=setup("projectiles/slime_projectile_left1");
        left2=setup("projectiles/slime_projectile_left2");
        right1=setup("projectiles/slime_projectile_right1");
        right2=setup("projectiles/slime_projectile_right2");
    }

    public Color getParticleColor(){
        return new Color(18, 112, 20);
    }

    public int getParticleSize(){
        return 6;
    }

    public int getParticleSpeed()
    {
        return 1;
    }

    public int getParticleMaxHealth(){
        return 20;
    }
}
