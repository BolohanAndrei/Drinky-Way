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

        // Move first
        switch (direction) {
            case "up" -> y -= speed;
            case "down" -> y += speed;
            case "left" -> x -= speed;
            case "right" -> x += speed;
        }

        // Collision with monsters only if fired by player
        if (user == gp.player) {
            int cx = x + gp.tileSize / 2;
            int cy = y + gp.tileSize / 2;
            int hitRadius = (int) (gp.tileSize * 0.75);
            Rectangle hitBox = new Rectangle(cx - hitRadius, cy - hitRadius, hitRadius * 2, hitRadius * 2);

            // Single pass: apply damage once if any monster intersects
            boolean hit = false;
            for (int i = 0; i < gp.monster.length; i++) {
                Entity mon = gp.monster[i];
                if (mon == null || !mon.alive || mon.dying) continue;
                Rectangle monBox = new Rectangle(mon.x + mon.solidArea.x,
                        mon.y + mon.solidArea.y,
                        mon.solidArea.width,
                        mon.solidArea.height);
                if (hitBox.intersects(monBox)) {
                    gp.player.checkAttackHit(hitBox, attackValue, true);
                    hit = true;
                    break;
                }
            }
            if (hit) alive = false;
        }

        // Lifetime countdown
        health--;
        if (health <= 0) alive = false;

        // Animation
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }
}
