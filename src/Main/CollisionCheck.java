package Main;

import Entity.Entity;

import java.awt.*;


public class CollisionCheck {
    GamePanel gp;

    public CollisionCheck(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        entity.collisionOn = false;

        int futureX = entity.x;
        int futureY = entity.y;

        switch (entity.direction) {
            case "up":         futureY -= entity.speed; break;
            case "down":       futureY += entity.speed; break;
            case "left":       futureX -= entity.speed; break;
            case "right":      futureX += entity.speed; break;
            case "up_left":    futureX -= entity.speed; futureY -= entity.speed; break;
            case "up_right":   futureX += entity.speed; futureY -= entity.speed; break;
            case "down_left":  futureX -= entity.speed; futureY += entity.speed; break;
            case "down_right": futureX += entity.speed; futureY += entity.speed; break;
        }

        int leftWorldX   = futureX + entity.solidArea.x;
        int rightWorldX  = futureX + entity.solidArea.x + entity.solidArea.width  - 1;
        int topWorldY    = futureY + entity.solidArea.y;
        int bottomWorldY = futureY + entity.solidArea.y + entity.solidArea.height - 1;

        int maxCol = gp.tileManager.mapTileNum[gp.currentMap].length;
        int maxRow = gp.tileManager.mapTileNum[gp.currentMap][0].length;

        int leftCol   = clamp(leftWorldX  / gp.tileSize, maxCol - 1);
        int rightCol  = clamp(rightWorldX / gp.tileSize, maxCol - 1);
        int topRow    = clamp(topWorldY   / gp.tileSize, maxRow - 1);
        int bottomRow = clamp(bottomWorldY/ gp.tileSize, maxRow - 1);

        if ( gp.tileManager.isCollision(gp.currentMap,leftCol,topRow)
          || gp.tileManager.isCollision(gp.currentMap,rightCol,topRow)
          || gp.tileManager.isCollision(gp.currentMap,leftCol,bottomRow)
          || gp.tileManager.isCollision(gp.currentMap,rightCol,bottomRow)) {
            entity.collisionOn = true;
        }
    }

    private int clamp(int v, int max) {
        return v < 0 ? 0 : (Math.min(v, max));
    }

    public int checkObj(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] != null) {
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].x + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].y + gp.obj[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                            if (gp.obj[gp.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                            if (gp.obj[gp.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                            if (gp.obj[gp.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                            if (gp.obj[gp.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "up_left":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                            if (gp.obj[gp.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "up_right":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                            if (gp.obj[gp.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "down_left":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                            if (gp.obj[gp.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "down_right":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                            if (gp.obj[gp.currentMap][i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public int checkEntity(Entity entity, Entity[][] target) {
        int index = 999;

        for (int i = 0; i < target[gp.currentMap].length; i++) {
            if (target[gp.currentMap][i] != null) {
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].x + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].y + target[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                    case "up_left":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "up_right":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down_left":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y += entity.speed;
                        break;
                    case "down_right":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y += entity.speed;
                        break;
                }

                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if(target[gp.currentMap][i]!=entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer=false;
        entity.solidArea.x = entity.x + entity.solidArea.x;
        entity.solidArea.y = entity.y + entity.solidArea.y;
        gp.player.solidArea.x = gp.player.x + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.y + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
            case "up_left":
                entity.solidArea.x -= entity.speed;
                entity.solidArea.y -= entity.speed;
                break;
            case "up_right":
                entity.solidArea.x += entity.speed;
                entity.solidArea.y -= entity.speed;
                break;
            case "down_left":
                entity.solidArea.x -= entity.speed;
                entity.solidArea.y += entity.speed;
                break;
            case "down_right":
                entity.solidArea.x += entity.speed;
                entity.solidArea.y += entity.speed;
                break;
        }

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer=true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }

    public void checkObject(Entity entity, int objIndex, Entity[] objects) {
        if (objects[objIndex] == null) return;

        Entity obj = objects[objIndex];

        entity.solidArea.x = entity.x + entity.solidArea.x;
        entity.solidArea.y = entity.y + entity.solidArea.y;

        obj.solidArea.x = obj.x + obj.solidArea.x;
        obj.solidArea.y = obj.y + obj.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }

        if (entity.solidArea.intersects(obj.solidArea)) {
            if (obj.collision) {
                entity.collisionOn = true;
            }
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        obj.solidArea.x = obj.solidAreaDefaultX;
        obj.solidArea.y = obj.solidAreaDefaultY;
    }

    public int checkObjForInteraction(Entity entity) {
        int index = 999;

        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] != null) {
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;

                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].x + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].y + gp.obj[gp.currentMap][i].solidArea.y;

                Rectangle interactionArea = new Rectangle(
                        entity.solidArea.x - gp.tileSize/2,
                        entity.solidArea.y - gp.tileSize/2,
                        entity.solidArea.width + gp.tileSize,
                        entity.solidArea.height + gp.tileSize
                );

                if (interactionArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                    index = i;
                    entity.solidArea.x = entity.solidAreaDefaultX;
                    entity.solidArea.y = entity.solidAreaDefaultY;
                    gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
                    gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
                    break;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }

        return index;
    }

}