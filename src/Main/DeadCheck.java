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
        int entityCol = (entity.x + entity.solidArea.x+entity.solidArea.width/2) / gp.tileSize;
        int entityRow = (entity.y + entity.solidArea.y+entity.solidArea.height/2) / gp.tileSize;
        if (entityCol >= 0 && entityCol < gp.maxScreenCol && entityRow >= 0 && entityRow < gp.maxScreenRow) {
            int tileNum = gp.tileManager.mapTileNum[entityCol][entityRow];
            if (gp.tileManager.tiles[tileNum].deadly) {
                entity.dead = true;
            }
        }

        return false;
    }
}