package Monster;

import Entity.Entity;
import Main.GamePanel;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseMonster extends Entity {

    // ========== AI State Management ==========
    protected int stuckCounter = 0;
    protected int noMovementCounter = 0;
    protected int prevX = 0;
    protected int prevY = 0;
    protected int invincibleResponseCounter = 0;

    // ========== Patrol System ==========
    protected boolean patrolState = false;
    protected int patrolCooldown = 0;
    protected int patrolMoveCounter = 0;
    protected int patrolMoveDuration = 80;

    // ========== Path Finding & Navigation ==========
    protected int stuckCooldown = 0;
    protected int personalSpaceCounter = 0;
    protected int lastPathRecalculation = 0;
    protected int alternativePathCounter = 0;
    protected boolean usingAlternativePath = false;

    // ========== AI Configuration ==========
    protected AIConfig aiConfig;

    protected static class AIConfig {
        // Detection and aggro ranges
        public int aggroRange = 6;
        public int disengageRange = 18;
        public int lineOfSightRange = 12;

        // Behavior probabilities
        public int aggroChance = 75;
        public int disengageChance = 35;
        public int meleeAttackChance = 90;
        public int rangedAttackChance = 95;
        public int idleRangedAttackChance = 90;
        public int patrolAggroChance = 65;
        public int smartDirectionChance = 55;

        // Timing configurations
        public int stuckThreshold = 60;
        public int noMovementThreshold = 20;
        public int invincibleResponseTime = 50;
        public int idleActionTime = 150;
        public int personalSpaceCheckInterval = 10;
        public int crowdAvoidanceChance = 30;

        // Combat timing
        public int meleeAttackCooldown = 18;
        public int rangedAttackCooldown = 25;

        // Path recalculation
        public int pathRecalculationInterval = 30;
        public int longRangePathInterval = 50;
    }

    public BaseMonster(GamePanel gp) {
        super(gp);
        this.aiConfig = createAIConfig();
        entityType = 2;
    }

    // ========== Abstract Methods - Must be implemented by subclasses ==========

    protected abstract AIConfig createAIConfig();

    public abstract void getImage();

    public abstract void checkDrop();

    // ========== Core Update Loop ==========

    @Override
    public void update() {
        super.update();
        updateCounters();

        int distance = calculateDistanceToPlayer();
        boolean canAttackPlayer = canAttackPlayer();

        handleAggroSystem(distance, canAttackPlayer);
        handleStuckDetection();
        handlePatrolCooldown();
    }

    // ========== AI System Methods ==========

    protected void updateCounters() {
        if (stuckCooldown > 0) stuckCooldown--;
        if (lastPathRecalculation > 0) lastPathRecalculation--;
        if (alternativePathCounter > 0) {
            alternativePathCounter--;
        } else {
            usingAlternativePath = false;
        }
    }

    protected void handleAggroSystem(int distance, boolean canAttackPlayer) {
        // Enter aggro state
        if (!onPath && distance <= aiConfig.aggroRange && canAttackPlayer) {
            if (new Random().nextInt(100) + 1 > aiConfig.aggroChance) {
                enterAggroState();
            }
        }

        // Exit aggro state
        if (onPath && (!canAttackPlayer || distance >= aiConfig.disengageRange)) {
            if (new Random().nextInt(100) + 1 > aiConfig.disengageChance) {
                exitAggroState();
            }
        }
    }

    protected void handleStuckDetection() {
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

            if (stuckCounter > aiConfig.stuckThreshold || noMovementCounter > aiConfig.noMovementThreshold) {
                handleStuckSituation();
                resetStuckCounters();
            }
        }
    }

    protected void handlePatrolCooldown() {
        if (patrolCooldown > 0) {
            patrolCooldown--;
        }
    }

    // ========== State Management ==========

    protected void enterAggroState() {
        onPath = true;
        actionLockCounter = 0;
        stuckCounter = 0;
        patrolState = false;
        lastPathRecalculation = 0;
    }

    protected void exitAggroState() {
        onPath = false;
        stuckCounter = 0;
        patrolState = true;
        patrolCooldown = 100;
        usingAlternativePath = false;
    }

    protected void resetStuckCounters() {
        stuckCounter = 0;
        noMovementCounter = 0;
    }

    // ========== Action Decision Making ==========

    @Override
    public void setAction() {
        boolean canAttackPlayer = canAttackPlayer();
        int distance = calculateDistanceToPlayer();

        if (onPath && canAttackPlayer) {
            handleAggroActions(distance);
        } else if (onPath) {
            handlePlayerInvincible();
        } else {
            handleIdleBehavior(distance, canAttackPlayer);
        }
    }

    protected void handleAggroActions(int distance) {
        int endCol = (gp.player.x + gp.player.solidArea.x) / gp.tileSize;
        int endRow = (gp.player.y + gp.player.solidArea.y) / gp.tileSize;

        boolean shouldRecalculate = shouldRecalculatePath();

        if (distance <= 1) {
            handleMeleeRange();
        } else if (distance <= 3) {
            handleCloseRange(endCol, endRow, shouldRecalculate);
        } else {
            handleLongRange(endCol, endRow, shouldRecalculate);
        }
    }

    protected void handleMeleeRange() {
        direction = getDirectionTowardPlayer();
        attemptAttack();
    }

    protected void handleCloseRange(int endCol, int endRow, boolean shouldRecalculate) {
        direction = getDirectionTowardPlayer();
        if (shouldRecalculate && !usingAlternativePath) {
            searchPath(endCol, endRow);
            lastPathRecalculation = aiConfig.pathRecalculationInterval;
        }
        attemptAttack();
    }

    protected void handleLongRange(int endCol, int endRow, boolean shouldRecalculate) {
        if (shouldRecalculate && !usingAlternativePath) {
            // Add randomness to target position to prevent clustering
            if (new Random().nextInt(100) < 20) {
                endCol += new Random().nextInt(3) - 1;
                endRow += new Random().nextInt(3) - 1;
            }
            searchPath(endCol, endRow);
            lastPathRecalculation = aiConfig.longRangePathInterval;
        }
        attemptRangedAttack();
    }

    protected void handleIdleBehavior(int distance, boolean canAttackPlayer) {
        actionLockCounter++;

        if (patrolState && patrolCooldown <= 0) {
            handlePatrolBehavior(distance, canAttackPlayer);
        } else if (actionLockCounter >= aiConfig.idleActionTime) {
            chooseSmartDirection(distance, canAttackPlayer);
            actionLockCounter = 0;

            if (canAttackPlayer && distance <= 10 && new Random().nextInt(100) > aiConfig.idleRangedAttackChance) {
                attemptRangedAttack();
            }
        }
    }

    // ========== Combat System ==========

    protected void attemptAttack() {
        if (!canAttackPlayer()) return;

        int attackChance = new Random().nextInt(100) + 1;

        if (attackChance > aiConfig.meleeAttackChance &&
                !projectile.alive &&
                shotAvailableCounter >= aiConfig.meleeAttackCooldown) {

            fireProjectile();
        }
    }

    protected void attemptRangedAttack() {
        if (!canAttackPlayer()) return;

        int attackChance = new Random().nextInt(100) + 1;

        if (attackChance > aiConfig.rangedAttackChance &&
                !projectile.alive &&
                shotAvailableCounter >= aiConfig.rangedAttackCooldown) {

            fireProjectile();
        }
    }

    protected void fireProjectile() {
        projectile.set(x, y, direction, true, this);
        gp.projectiles.add(projectile);
        shotAvailableCounter = 0;
    }

    // ========== Utility Methods ==========

    protected boolean canAttackPlayer() {
        return !gp.player.invincible && gp.player.health > 0 && hasLineOfSight();
    }

    protected boolean hasLineOfSight() {
        int distance = calculateDistanceToPlayer();
        return distance <= aiConfig.lineOfSightRange;
    }

    protected int calculateDistanceToPlayer() {
        int xDist = Math.abs(x - gp.player.x);
        int yDist = Math.abs(y - gp.player.y);
        return (xDist + yDist) / gp.tileSize;
    }

    protected boolean shouldRecalculatePath() {
        return lastPathRecalculation == 0 ||
                Math.abs(gp.player.x - prevX) > gp.tileSize / 2 ||
                Math.abs(gp.player.y - prevY) > gp.tileSize / 2;
    }

    // ========== Movement and Direction Methods ==========

    protected String getDirectionTowardPlayer() {
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? "right" : "left";
        } else {
            return dy > 0 ? "down" : "up";
        }
    }

    protected String getOppositeDirectionToPlayer() {
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? "left" : "right";
        } else {
            return dy > 0 ? "up" : "down";
        }
    }

    protected String getPerpendicularDirection() {
        Random rand = new Random();
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return rand.nextBoolean() ? "up" : "down";
        } else {
            return rand.nextBoolean() ? "left" : "right";
        }
    }

    protected String getDirectionAroundObstacle() {
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dy > 0 ? "down" : "up";
        } else {
            return dx > 0 ? "right" : "left";
        }
    }

    protected String chooseRandomDirection() {
        Random rand = new Random();
        int i = rand.nextInt(100) + 1;

        if (i <= 25) return "up";
        else if (i <= 50) return "down";
        else if (i <= 75) return "left";
        else return "right";
    }

    protected String getOppositeDirection() {
        return switch (direction) {
            case "up" -> "down";
            case "down" -> "up";
            case "left" -> "right";
            case "right" -> "left";
            default -> "down";
        };
    }

    // ========== Advanced Behavior Systems ==========

    protected void checkIfBlockedByOtherMonsters() {
        personalSpaceCounter++;

        if (personalSpaceCounter >= aiConfig.personalSpaceCheckInterval) {
            personalSpaceCounter = 0;

            for (int i = 0; i < gp.monster.length; i++) {
                for (int j = 0; j < gp.monster[i].length; j++) {
                    Entity entity = gp.monster[i][j];
                    if (entity != this && entity != null && entity.alive) {
                        int distance = Math.abs(entity.x - x) + Math.abs(entity.y - y);

                        if (distance < gp.tileSize * 2 && entity.direction.equals(direction)) {
                            if (new Random().nextInt(100) < aiConfig.crowdAvoidanceChance && !usingAlternativePath) {
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

    protected void handleCrowdSituation() {
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

    protected void handlePlayerInvincible() {
        invincibleResponseCounter++;

        if (invincibleResponseCounter > aiConfig.invincibleResponseTime) {
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

    protected void handlePatrolBehavior(int distance, boolean canAttackPlayer) {
        patrolMoveCounter++;

        if (patrolMoveCounter >= patrolMoveDuration) {
            choosePatrolDirection();
            patrolMoveCounter = 0;
            patrolMoveDuration = new Random().nextInt(100) + 50;
        }

        if (canAttackPlayer && distance <= aiConfig.aggroRange &&
                new Random().nextInt(100) > aiConfig.patrolAggroChance) {
            enterAggroState();
        }
    }

    protected void chooseSmartDirection(int distance, boolean canAttackPlayer) {
        Random rand = new Random();

        if (canAttackPlayer && distance <= 12 && rand.nextInt(100) > aiConfig.smartDirectionChance) {
            direction = getDirectionTowardPlayer();
        } else {
            int i = rand.nextInt(100) + 1;
            if (i <= 25) direction = "up";
            else if (i <= 50) direction = "down";
            else if (i <= 75) direction = "left";
            else direction = "right";
        }
    }

    protected void choosePatrolDirection() {
        Random rand = new Random();
        int i = rand.nextInt(100) + 1;

        if (i <= 35) {
            // Keep same direction
        } else if (i <= 65) {
            direction = getSlightTurnDirection();
        } else {
            String[] directions = {"up", "down", "left", "right"};
            direction = directions[rand.nextInt(directions.length)];
        }
    }

    protected String getSlightTurnDirection() {
        Random rand = new Random();
        String[] possibleDirections = switch (direction) {
            case "up", "down" -> new String[]{"left", "right"};
            case "left", "right" -> new String[]{"up", "down"};
            default -> new String[]{"up", "down", "left", "right"};
        };

        return possibleDirections[rand.nextInt(possibleDirections.length)];
    }

    protected void handleStuckSituation() {
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
    }

    // ========== Damage Response ==========

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
        usingAlternativePath = false;
        actionLockCounter -= new Random().nextInt(20);
    }

    // ========== Getters and Setters ==========

    public AIConfig getAiConfig() { return aiConfig; }
    public void setAiConfig(AIConfig aiConfig) { this.aiConfig = aiConfig; }

    public boolean isPatrolState() { return patrolState; }
    public void setPatrolState(boolean patrolState) { this.patrolState = patrolState; }

    public int getPatrolCooldown() { return patrolCooldown; }
    public void setPatrolCooldown(int patrolCooldown) { this.patrolCooldown = patrolCooldown; }

    public boolean isUsingAlternativePath() { return usingAlternativePath; }
    public void setUsingAlternativePath(boolean usingAlternativePath) { this.usingAlternativePath = usingAlternativePath; }

    public int getStuckCounter() { return stuckCounter; }
    public int getNoMovementCounter() { return noMovementCounter; }
}