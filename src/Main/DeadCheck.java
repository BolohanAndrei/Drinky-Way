// src/Main/DeadCheck.java
package Main;

import Entity.Entity;

public class DeadCheck {
    GamePanel gp;

    //Dead check constructor - not working
    public DeadCheck(GamePanel gp) {
        this.gp = gp;
    }

    public boolean check(Entity entity) {
        // Calculate the tile the entity is on
        int entityCol = (entity.x + entity.solidArea.x) / gp.tileSize;
        int entityRow = (entity.y + entity.solidArea.y) / gp.tileSize;

        // Check bounds
        if (entityCol >= 0 && entityCol < gp.maxScreenCol && entityRow >= 0 && entityRow < gp.maxScreenRow) {
            int tileNum = gp.tileManager.mapTileNum[entityCol][entityRow];
            if (gp.tileManager.tiles[tileNum].deadly) {
                entity.dead = true;
            }
        }

        // Handle death animation
        if (entity.dead) {
            entity.dieFrame++;
            if (entity.dieFrame == 1) {
                entity.x = 100;
                entity.y = 100;
            }
            if (entity.dieFrame > 30) {
                entity.dead = false;
                entity.dieFrame = 0;
            }
            return true;
        }
        return false;
    }
}