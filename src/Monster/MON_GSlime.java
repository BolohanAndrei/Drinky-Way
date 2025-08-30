package Monster;

import Entity.Entity;
import Main.GamePanel;
import object.*;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MON_GSlime extends Entity {
    private int stuckCounter = 0;
    private int noMovementCounter = 0;
    private int prevX = 0;
    private int prevY = 0;
    private int invincibleResponseCounter = 0;
    private boolean patrolState = false;
    private int patrolCooldown = 0;
    private int patrolMoveCounter = 0;
    private int patrolMoveDuration = 80;
    private int stuckCooldown = 0;
    private int personalSpaceCounter = 0;
    private int lastPathRecalculation = 0;
    private int alternativePathCounter = 0;
    private boolean usingAlternativePath = false;

    public MON_GSlime(GamePanel gp) {
        super(gp);
        name = "Green Slime";
        entityType = 2;
        speed = 1;
        maxHealth = 4;
        health = maxHealth;
        attack = 3;
        defense = 0;
        exp = 2;
        projectile = new Obj_SlimeProjectile(gp);

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setup("monsters/Slime_left1");
        up2 = setup("monsters/Slime_right1");
        down1 = setup("monsters/Slime_left1");
        down2 = setup("monsters/Slime_right1");
        left1 = setup("monsters/Slime_left1");
        left2 = setup("monsters/Slime_left2");
        right1 = setup("monsters/Slime_right1");
        right2 = setup("monsters/Slime_right2");
        upLeft1 = setup("monsters/Slime_left1");
        upLeft2 = setup("monsters/Slime_left2");
        upRight1 = setup("monsters/Slime_right1");
        upRight2 = setup("monsters/Slime_right2");
        downLeft1 = setup("monsters/Slime_left1");
        downLeft2 = setup("monsters/Slime_left2");
        downRight1 = setup("monsters/Slime_right1");
        downRight2 = setup("monsters/Slime_right2");
    }

    public void update() {
        super.update();
        int xDist = Math.abs(x - gp.player.x);
        int yDist = Math.abs(y - gp.player.y);
        int dist = (xDist + yDist) / gp.tileSize;

        if (stuckCooldown > 0) {
            stuckCooldown--;
        }

        if (lastPathRecalculation > 0) {
            lastPathRecalculation--;
        }

        if (alternativePathCounter > 0) {
            alternativePathCounter--;
        } else {
            usingAlternativePath = false;
        }

        boolean canAttackPlayer = canAttackPlayer();

        if (!onPath && dist <= 6 && canAttackPlayer) {
            int i = new Random().nextInt(100) + 1;
            if (i > 75) {
                onPath = true;
                actionLockCounter = 0;
                stuckCounter = 0;
                patrolState = false;
                lastPathRecalculation = 0;
            }
        }

        if (onPath && (!canAttackPlayer || dist >= 18)) {
            int i = new Random().nextInt(100) + 1;
            if (i > 35) {
                onPath = false;
                stuckCounter = 0;
                patrolState = true;
                patrolCooldown = 100;
                usingAlternativePath = false;
            }
        }

        if (onPath) {
            checkIfBlockedByOtherMonsters();

            if (x == prevX && y == prevY) {
                stuckCounter++;
                noMovementCounter++;
            } else {
                stuckCounter = 0;
                noMovementCounter = 0;
            }
            prevX = x;
            prevY = y;

            if (stuckCounter > 60 || noMovementCounter > 20) {
                handleStuckSituation();
                stuckCounter = 0;
                noMovementCounter = 0;
            }
        }

        if (patrolCooldown > 0) {
            patrolCooldown--;
        }
    }

    private void checkIfBlockedByOtherMonsters() {
        personalSpaceCounter++;

        if (personalSpaceCounter >= 10) {
            personalSpaceCounter = 0;

            for (int i = 0; i < gp.monster.length; i++) {
                for (int j = 0; j < gp.monster[i].length; j++) {
                    Entity entity = gp.monster[i][j];
                    if (entity != this && entity != null && entity.alive) {
                        int distance = Math.abs(entity.x - x) + Math.abs(entity.y - y);

                        if (distance < gp.tileSize * 2 && entity.direction.equals(direction)) {
                            if (new Random().nextInt(100) < 30 && !usingAlternativePath) {
                                usingAlternativePath = true;
                                alternativePathCounter = 80;
                                handleCrowdSituation();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleCrowdSituation() {
        Random rand = new Random();
        int choice = rand.nextInt(100);

        if (choice < 40) {
            direction = getPerpendicularDirection();
            onPath = false;
            actionLockCounter = -40;
            stuckCooldown = 30;
        } else if (choice < 70) {
            actionLockCounter = -20;
            speed = 0;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    speed = 1;
                }
            }, 1000);
        } else {
            direction = getDirectionAroundObstacle();
            onPath = false;
            actionLockCounter = -30;
        }
    }

    public void setAction() {
        boolean canAttackPlayer = canAttackPlayer();
        int dist = (Math.abs(x - gp.player.x) + Math.abs(y - gp.player.y)) / gp.tileSize;

        if (onPath && canAttackPlayer) {
            int endCol = (gp.player.x + gp.player.solidArea.x) / gp.tileSize;
            int endRow = (gp.player.y + gp.player.solidArea.y) / gp.tileSize;

            int currentDist = (Math.abs(x - gp.player.x) + Math.abs(y - gp.player.y)) / gp.tileSize;

            boolean shouldRecalculate = lastPathRecalculation == 0 ||
                    Math.abs(gp.player.x - prevX) > gp.tileSize / 2 ||
                    Math.abs(gp.player.y - prevY) > gp.tileSize / 2;

            if (currentDist <= 1) {
                direction = getDirectionTowardPlayer();
                attemptAttack();
            } else if (currentDist <= 3) {
                direction = getDirectionTowardPlayer();
                if (shouldRecalculate && !usingAlternativePath) {
                    searchPath(endCol, endRow);
                    lastPathRecalculation = 30;
                }
                attemptAttack();
            } else {
                if (shouldRecalculate && !usingAlternativePath) {
                    if (new Random().nextInt(100) < 20) {
                        endCol += new Random().nextInt(3) - 1;
                        endRow += new Random().nextInt(3) - 1;
                    }
                    searchPath(endCol, endRow);
                    lastPathRecalculation = 50;
                }
                attemptRangedAttack();
            }

        } else if (onPath && !canAttackPlayer) {
            handlePlayerInvincible();
        } else {
            handleIdleBehavior(dist, canAttackPlayer);
        }
    }

    private boolean canAttackPlayer() {
        return !gp.player.invincible && gp.player.health > 0 && hasLineOfSight();
    }

    private boolean hasLineOfSight() {
        int dist = (Math.abs(x - gp.player.x) + Math.abs(y - gp.player.y)) / gp.tileSize;
        return dist <= 12;
    }

    private void handlePlayerInvincible() {
        invincibleResponseCounter++;

        if (invincibleResponseCounter > 50) {
            Random rand = new Random();
            int choice = rand.nextInt(100);

            if (choice < 35) {
                direction = getDirectionTowardPlayer();
                onPath = false;
                actionLockCounter = -80;

            } else if (choice < 75) {
                onPath = false;
                patrolState = true;
                direction = chooseRandomDirection();
                actionLockCounter = 0;

            } else {
                direction = getOppositeDirection();
                onPath = false;
                actionLockCounter = -100;
                patrolCooldown = 150;
            }

            invincibleResponseCounter = 0;
        }
    }

    private void handleIdleBehavior(int dist, boolean canAttackPlayer) {
        actionLockCounter++;

        if (patrolState && patrolCooldown <= 0) {
            handlePatrolBehavior(dist, canAttackPlayer);
        } else if (actionLockCounter >= 150) {
            chooseSmartDirection(dist, canAttackPlayer);
            actionLockCounter = 0;

            if (canAttackPlayer && dist <= 10 && new Random().nextInt(100) > 90) {
                attemptRangedAttack();
            }
        }
    }

    private void handlePatrolBehavior(int dist, boolean canAttackPlayer) {
        patrolMoveCounter++;

        if (patrolMoveCounter >= patrolMoveDuration) {
            choosePatrolDirection();
            patrolMoveCounter = 0;
            patrolMoveDuration = new Random().nextInt(100) + 50;
        }

        if (canAttackPlayer && dist <= 6 && new Random().nextInt(100) > 65) {
            onPath = true;
            patrolState = false;
            actionLockCounter = 0;
        }
    }

    private void chooseSmartDirection(int dist, boolean canAttackPlayer) {
        Random rand = new Random();

        if (canAttackPlayer && dist <= 12 && rand.nextInt(100) > 55) {
            direction = getDirectionTowardPlayer();
        } else {
            int i = rand.nextInt(100) + 1;
            if (i <= 25) direction = "up";
            else if (i <= 50) direction = "down";
            else if (i <= 75) direction = "left";
            else direction = "right";
        }
    }

    private void choosePatrolDirection() {
        Random rand = new Random();
        int i = rand.nextInt(100) + 1;

        if (i <= 35) {
        } else if (i <= 65) {
            direction = getSlightTurnDirection();
        } else {
            String[] directions = {"up", "down", "left", "right"};
            direction = directions[rand.nextInt(directions.length)];
        }
    }

    private String getSlightTurnDirection() {
        Random rand = new Random();
        String[] possibleDirections;

        switch (direction) {
            case "up": case "down":
                possibleDirections = new String[]{"left", "right"};
                break;
            case "left": case "right":
                possibleDirections = new String[]{"up", "down"};
                break;
            default:
                possibleDirections = new String[]{"up", "down", "left", "right"};
        }

        return possibleDirections[rand.nextInt(possibleDirections.length)];
    }

    private String getOppositeDirection() {
        switch (direction) {
            case "up": return "down";
            case "down": return "up";
            case "left": return "right";
            case "right": return "left";
            default: return "down";
        }
    }

    private void attemptAttack() {
        if (!canAttackPlayer()) return;

        int attackChance = new Random().nextInt(100) + 1;

        if (attackChance > 90 && !projectile.alive && shotAvailableCounter >= 18) {
            projectile.set(x, y, direction, true, this);
            gp.projectiles.add(projectile);
            shotAvailableCounter = 0;
        }
    }

    private void attemptRangedAttack() {
        if (!canAttackPlayer()) return;

        int attackChance = new Random().nextInt(100) + 1;

        if (attackChance > 95 && !projectile.alive && shotAvailableCounter >= 25) {
            projectile.set(x, y, direction, true, this);
            gp.projectiles.add(projectile);
            shotAvailableCounter = 0;
        }
    }

    private void handleStuckSituation() {
        Random rand = new Random();
        int choice = rand.nextInt(100);

        if (choice < 30) {
            direction = getOppositeDirectionToPlayer();
            onPath = false;
            actionLockCounter = -70;
            stuckCooldown = 50;

        } else if (choice < 60) {
            direction = getPerpendicularDirection();
            onPath = false;
            actionLockCounter = -35;
            stuckCooldown = 35;

        } else if (choice < 85) {
            direction = chooseRandomDirection();
            onPath = false;
            actionLockCounter = -25;
            stuckCooldown = 25;

        } else {
            onPath = false;
            stuckCooldown = 15;
        }

        stuckCounter = 0;
        noMovementCounter = 0;
    }

    private String getDirectionTowardPlayer() {
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? "right" : "left";
        } else {
            return dy > 0 ? "down" : "up";
        }
    }

    private String chooseRandomDirection() {
        Random rand = new Random();
        int i = rand.nextInt(100) + 1;

        if (i <= 25) return "up";
        else if (i <= 50) return "down";
        else if (i <= 75) return "left";
        else return "right";
    }

    private String getOppositeDirectionToPlayer() {
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? "left" : "right";
        } else {
            return dy > 0 ? "up" : "down";
        }
    }

    private String getPerpendicularDirection() {
        Random rand = new Random();
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return rand.nextBoolean() ? "up" : "down";
        } else {
            return rand.nextBoolean() ? "left" : "right";
        }
    }

    private String getDirectionAroundObstacle() {
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dy > 0 ? "down" : "up";
        } else {
            return dx > 0 ? "right" : "left";
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
        usingAlternativePath = false;
        actionLockCounter -= new Random().nextInt(20);
    }

    @Override
    public void checkDrop() {
        int roll = new java.util.Random().nextInt(100);

        if (roll <= 20) {
            dropItem(new Obj_Coin(gp));
        } else if (roll <= 40) {
            dropItem(new Obj_Heal_Potion(gp));
        } else if (roll <= 50) {
            dropItem(new Obj_Cup(gp));
        } else if (roll <= 52) {
            dropItem(new Obj_Amber(gp));
        } else if (roll <= 54) {
            dropItem(new Obj_Amethyst(gp));
        } else if (roll <= 56) {
            dropItem(new Obj_Diamond(gp));
        } else if (roll <= 58) {
            dropItem(new Obj_Emerald(gp));
        } else if (roll <= 60) {
            dropItem(new Obj_Quartz(gp));
        } else if (roll <= 62) {
            dropItem(new Obj_Ruby(gp));
        } else if (roll <= 64) {
            dropItem(new Obj_Sapphire(gp));
        } else if (roll <= 70) {
            dropItem(new Obj_Beer(gp));
        } else if (roll <= 75) {
            dropItem(new Obj_Cigarette(gp));
        } else if (roll <= 78) {
            dropItem(new Obj_Rum(gp));
        } else if (roll <= 81) {
            dropItem(new Obj_Drugs(gp));
        } else if (roll <= 84) {
            dropItem(new Obj_Tequila(gp));
        } else if (roll <= 87) {
            dropItem(new Obj_Whiskey(gp));
        } else if (roll <= 90) {
            dropItem(new Obj_Hook(gp));
        } else if (roll <= 93) {
            dropItem(new Obj_Armour_Helmet_Crusty(gp));
        } else if (roll <= 96) {
            dropItem(new Obj_Armour_Chest_Crusty(gp));
        } else {
            dropItem(new Obj_Armour_Boots_Crusty(gp));
        }
    }
}