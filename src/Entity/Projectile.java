package Entity;

import Main.GamePanel;

import java.awt.*;

public class Projectile extends Entity {

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.x = worldX;
        this.y = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.health = this.maxHealth;
    }

    public void update() {
        if (!alive) return;

        switch (direction) {
            case "up" -> y -= speed;
            case "down" -> y += speed;
            case "left" -> x -= speed;
            case "right" -> x += speed;
        }

        if (user == gp.player) {
            int cx = x + gp.tileSize / 2;
            int cy = y + gp.tileSize / 2;
            int hitRadius = (int) (gp.tileSize * 0.75);
            Rectangle hitBox = new Rectangle(cx - hitRadius, cy - hitRadius, hitRadius * 2, hitRadius * 2);

            boolean hit = false;
            for (int i = 0; i < gp.monster.length; i++) {
                Entity mon = gp.monster[i];
                if (mon == null || !mon.alive || mon.dying) continue;
                Rectangle monBox = new Rectangle(mon.x + mon.solidArea.x,
                        mon.y + mon.solidArea.y,
                        mon.solidArea.width,
                        mon.solidArea.height);
                if (hitBox.intersects(monBox)) {
                    generateParticle(user.projectile,mon);
                    gp.player.checkAttackHit(hitBox, attackValue, true);
                    hit = true;
                    break;
                }
            }
            if (hit) alive = false;
        }
        if(user!=gp.player) {
            boolean contactPlayer=gp.collisionCheck.checkPlayer(this);
            if(!gp.player.invincible && contactPlayer) {
                damagePlayer(attack);
                generateParticle(user.projectile,gp.player);
                alive=false;
            }
        }

        health--;
        if (health <= 0) alive = false;

        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }
}
