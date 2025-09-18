package Monster;

import Entity.Entity;
import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseMonster extends Entity {

    public enum CombatStyle { MELEE, RANGED, HYBRID }
    private enum MeleePhase { NONE, WINDUP, ACTIVE }

    // ========== AI State Management ==========
    protected int stuckCounter = 0;
    protected int noMovementCounter = 0;
    protected int prevX = 0;
    protected int prevY = 0;

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

    // ========== Advanced Animation System ==========
    protected BufferedImage[][] walkDirFrames = new BufferedImage[4][];
    protected BufferedImage[][] attackDirFrames = new BufferedImage[4][];
    protected BufferedImage[][] idleDirFrames = new BufferedImage[4][];
    protected BufferedImage[][] deathDirFrames = new BufferedImage[4][];
    protected BufferedImage[] deathFrames;

    protected int walkFrameIndex = 0, attackFrameIndex = 0, idleFrameIndex = 0, deathFrameIndex = 0;
    protected int walkFrameTick = 0, attackFrameTick = 0, idleFrameTick = 0, deathFrameTick = 0;

    protected int animWalkSpeed = 15;
    protected int animAttackSpeed = 12;
    protected int animIdleSpeed = 20;
    protected int animDeathSpeed = 12;
    private int fallbackDeathCounter=0;

    protected int attackAnimationTimer = 0;
    protected int attackAnimationDuration = 0;

    protected boolean meleeInProgress = false;
    protected int meleeTimer = 0;
    protected MeleePhase meleePhase = MeleePhase.NONE;

    // Speed variation
    protected int originalSpeed = 0;

    // Animation movement detection
    protected int prevAnimX = 0, prevAnimY = 0;

    // ========== AI Configuration ==========
    protected AIConfig aiConfig;

    protected static class AIConfig {
        public int aggroRange = 6;
        public int disengageRange = 18;
        public int lineOfSightRange = 12;

        public int aggroChance = 75;
        public int disengageChance = 35;
        public int meleeAttackChance = 90;
        public int rangedAttackChance = 95;
        public int idleRangedAttackChance = 90;
        public int patrolAggroChance = 65;
        public int smartDirectionChance = 55;

        public int stuckThreshold = 60;
        public int noMovementThreshold = 20;
        public int idleActionTime = 150;
        public int personalSpaceCheckInterval = 10;
        public int crowdAvoidanceChance = 30;

        public int meleeAttackCooldown = 30;
        public int rangedAttackCooldown = 30;
        public int meleeAnimationTime = 20;
        public int meleeWindupFrames = 10;
        public int meleeActiveFrames = 8;

        public int longRangePathInterval = 50;

        public CombatStyle style = CombatStyle.HYBRID;
        public float retreatSpeedMultiplier = 1.3f;
        public float alternativePathSpeedMultiplier = 1.15f;
        public int meleeRangeTiles = 1;
        public int minRangedDistanceTiles = 3;
        public int maxRangedDistanceTiles = 8;
    }

    public BaseMonster(GamePanel gp) {
        super(gp);
        this.aiConfig = createAIConfig();
        entityType = 2;
        if (aiConfig.style == CombatStyle.RANGED) {
            allowContactDamage = false;
        }
    }

    // ========== Abstract Methods ==========

    protected abstract AIConfig createAIConfig();

    public abstract void getImage();

    public abstract void checkDrop();

    protected void initAdvancedAnimations() {  }

    // ========== Core Update Loop ==========

    @Override
    public void update() {
        if (originalSpeed <= 0) {
            originalSpeed = speed > 0 ? speed : 1;
        } else {
            speed = originalSpeed;
        }
        if (usingAlternativePath) {
            speed = Math.max(1, Math.round(originalSpeed * aiConfig.alternativePathSpeedMultiplier));
        }

        if (dying) {
            if (hasAdvancedAnimations()) {
                updateAdvancedAnimationState();
            } else {
                fallbackDeathCounter++;
                if (fallbackDeathCounter > 40) { alive = false;fallbackDeathCounter=0; }
            }
            attacking = false;
            meleeInProgress = false;
            return;
        }

        super.update();
        processMeleeAttackState();
        updateCounters();
        updateAdvancedAnimationState();

        int distance = calculateDistanceToPlayer();

        handleAggroSystem(distance);
        handleStuckDetection();
        handlePatrolCooldown();

        if (attackAnimationTimer > 0 || attacking) {
            BufferedImage[] frames = attackDirFrames[dirIndex(direction)];
            if (frames != null && frames.length > 0) {
                int needed = animAttackSpeed * frames.length;
                if (attackAnimationDuration < needed) {
                    attackAnimationDuration = needed;
                    if (attackAnimationTimer < needed) {
                        attackAnimationTimer = needed;
                    }
                }
            }
        }
        if (attackAnimationTimer > 0) {
            attackAnimationTimer--;
            if (attackAnimationDuration == 0 && attacking) {
                BufferedImage[] frames = attackDirFrames[dirIndex(direction)];
                if (frames != null && frames.length > 0) {
                    attackAnimationDuration = animAttackSpeed * frames.length;
                    attackAnimationTimer = Math.max(attackAnimationTimer, attackAnimationDuration);
                }
            }
            if (attackAnimationTimer == 0 && !meleeInProgress) {
                attacking = false;
                attackAnimationDuration = 0;
                attackFrameIndex = 0;
            }
        }
        prevAnimX = x; prevAnimY = y;
    }

    protected void processMeleeAttackState() {
        if (!meleeInProgress) return;
        meleeTimer++;
        if (meleePhase == MeleePhase.WINDUP && meleeTimer >= aiConfig.meleeWindupFrames) {
            meleePhase = MeleePhase.ACTIVE;
            if (isPlayerInMeleeHitbox()) {
                damagePlayer(attack);
            }
        }
        if (meleePhase == MeleePhase.ACTIVE) {
            if (meleeTimer >= aiConfig.meleeWindupFrames + aiConfig.meleeActiveFrames) {
                meleeInProgress = false;
                meleeTimer = 0;
                meleePhase = MeleePhase.NONE;
            }
        }
    }

    private Rectangle buildMeleeHitboxRect(){
        int rangePixels = aiConfig.meleeRangeTiles * gp.tileSize;
        int left = x + solidArea.x;
        int top = y + solidArea.y;
        int right = left + solidArea.width;
        int bottom = top + solidArea.height;
        return switch (direction) {
            case "up", "up_left", "up_right" -> new Rectangle(left, top - rangePixels, solidArea.width, rangePixels);
            case "down", "down_left", "down_right" -> new Rectangle(left, bottom, solidArea.width, rangePixels);
            case "left" -> new Rectangle(left - rangePixels, top, rangePixels, solidArea.height);
            case "right" -> new Rectangle(right, top, rangePixels, solidArea.height);
            default -> {
                int pad = rangePixels / 2;
                yield new Rectangle(left - pad/2, top - pad/2, solidArea.width + pad, solidArea.height + pad);
            }
        };
    }
    public Rectangle debugMeleeRect(){ return buildMeleeHitboxRect(); }
    public boolean isMeleeActivePhase(){ return meleePhase == MeleePhase.ACTIVE; }
    public boolean isMeleeWindupPhase(){ return meleePhase == MeleePhase.WINDUP; }

    private boolean isPlayerInMeleeHitbox() {
        Rectangle hitBox = buildMeleeHitboxRect();
        int pCenterX = gp.player.x + gp.player.solidArea.x + gp.player.solidArea.width/2;
        int pCenterY = gp.player.y + gp.player.solidArea.y + gp.player.solidArea.height/2;
        return hitBox.contains(pCenterX, pCenterY);
    }

    protected boolean hasAdvancedAnimations() {
        for (int i=0;i<4;i++) {
            if (walkDirFrames[i]!=null) return true;
        }
        return false;
    }

    protected boolean isMoving() { return x != prevAnimX || y != prevAnimY; }

    protected void updateAdvancedAnimationState() {
        if (!hasAdvancedAnimations()) return;
        if (dying) {
            BufferedImage[] dFrames = deathDirFrames[dirIndex(direction)];
            if (dFrames == null) dFrames = deathFrames;
            if (dFrames != null) {
                deathFrameTick++;
                if (deathFrameTick >= animDeathSpeed) {
                    deathFrameTick = 0;
                    if (deathFrameIndex < dFrames.length - 1) deathFrameIndex++; else alive = false;
                }
            }
            return;
        }
        if (attacking) {
            BufferedImage[] aFrames = attackDirFrames[dirIndex(direction)];
            if (aFrames != null && aFrames.length > 0) {
                attackFrameTick++;
                if (attackFrameTick >= animAttackSpeed) {
                    attackFrameTick = 0;
                    attackFrameIndex = (attackFrameIndex + 1) % aFrames.length;
                }
                return;
            }
        }
        if (isMoving()) {
            BufferedImage[] wFrames = walkDirFrames[dirIndex(direction)];
            if (wFrames != null && wFrames.length > 1) {
                walkFrameTick++;
                if (walkFrameTick >= animWalkSpeed) {
                    walkFrameTick = 0;
                    walkFrameIndex = (walkFrameIndex + 1) % wFrames.length;
                }
                idleFrameIndex = 0; idleFrameTick = 0;
            } else {
                walkFrameTick = 0; walkFrameIndex = 0;
                idleFrameIndex = 0; idleFrameTick = 0;
            }
        } else {
            idleFrameTick++;
            BufferedImage[] iFrames = idleDirFrames[dirIndex(direction)];
            if (iFrames != null && iFrames.length > 1) {
                if (idleFrameTick >= animIdleSpeed) {
                    idleFrameTick = 0;
                    idleFrameIndex = (idleFrameIndex + 1) % iFrames.length;
                }
            } else {
                idleFrameTick = 0; idleFrameIndex = 0;
            }
        }
    }

    private BufferedImage legacyAnimatedWalkFrame() {
        return switch (direction) {
            case "up", "up_left", "up_right" -> (spriteNum == 1 ? (up1 != null ? up1 : up2) : (up2 != null ? up2 : up1));
            case "down", "down_left", "down_right" -> (spriteNum == 1 ? (down1 != null ? down1 : down2) : (down2 != null ? down2 : down1));
            case "left" -> (spriteNum == 1 ? (left1 != null ? left1 : left2) : (left2 != null ? left2 : left1));
            case "right" -> (spriteNum == 1 ? (right1 != null ? right1 : right2) : (right2 != null ? right2 : right1));
            default -> (down1 != null ? down1 : down2);
        };
    }

    protected BufferedImage resolveCurrentFrame() {
        int dIdx = dirIndex(direction);
        if (dying) {
            BufferedImage[] dFrames = deathDirFrames[dIdx];
            if (dFrames == null) dFrames = deathFrames;
            if (dFrames != null) return dFrames[Math.min(deathFrameIndex, dFrames.length - 1)];
        }
        if (attacking) {
            BufferedImage[] aFrames = attackDirFrames[dIdx];
            if (aFrames != null && aFrames.length > 0) return aFrames[Math.min(attackFrameIndex, aFrames.length - 1)];
        }
        if (isMoving()) {
            BufferedImage[] wFrames = walkDirFrames[dIdx];
            if (wFrames != null && wFrames.length > 1) {
                long tick = gp.playerTick();
                int idx = (int)((tick / Math.max(1, animWalkSpeed)) % wFrames.length);
                return wFrames[idx];
            }
            return legacyAnimatedWalkFrame();
        } else {
            BufferedImage[] iFrames = idleDirFrames[dIdx];
            if (iFrames != null && iFrames.length > 1) {
                long tick = gp.playerTick();
                int idx = (int)((tick / Math.max(1, animIdleSpeed)) % iFrames.length);
                return iFrames[idx];
            }
            if (iFrames != null && iFrames.length == 1) return iFrames[0];
        }
        return legacyAnimatedWalkFrame();
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

    protected void handleAggroSystem(int distance) {
        if (!onPath && distance <= aiConfig.aggroRange) {
            if (new Random().nextInt(100) + 1 > aiConfig.aggroChance) {
                enterAggroState();
            }
        }
        if (onPath && distance >= aiConfig.disengageRange) {
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
        int distance = calculateDistanceToPlayer();
        boolean losAttackOk = canAttackPlayer();

        if (onPath) {
            handleChase(distance, losAttackOk);
        } else {
            handleIdleBehavior(distance, losAttackOk);
        }
    }

    protected void handleChase(int distance, boolean losAttackOk){
        int endCol = (gp.player.x + gp.player.solidArea.x) / gp.tileSize;
        int endRow = (gp.player.y + gp.player.solidArea.y) / gp.tileSize;
        boolean shouldRecalculate = shouldRecalculatePath();
        switch (aiConfig.style) {
            case MELEE -> handleMeleeChase(distance, endCol, endRow, shouldRecalculate, losAttackOk);
            case RANGED -> handleRangedChase(distance, endCol, endRow, shouldRecalculate, losAttackOk);
            case HYBRID -> handleHybridChase(distance, endCol, endRow, shouldRecalculate, losAttackOk);
        }
    }
    protected void handleMeleeChase(int distance,int endCol,int endRow,boolean recalc,boolean los){
        if (distance <= aiConfig.meleeRangeTiles && los){
            direction = getDirectionTowardPlayer();
            attemptMeleeStrike();
        } else if (recalc) {
            searchPath(endCol,endRow);
        }
    }
    protected void handleRangedChase(int distance,int endCol,int endRow,boolean recalc,boolean los){
        handleRangedStyle(distance,endCol,endRow,recalc);
        if (los) attemptRangedAttack();
    }
    protected void handleHybridChase(int distance,int endCol,int endRow,boolean recalc,boolean los){
        if (distance <= aiConfig.meleeRangeTiles && los){
            handleMeleeRange();
            attemptMeleeStrike();
        } else if (distance <= 3) {
            if (recalc) searchPath(endCol,endRow);
            if (los) attemptRangedAttack();
        } else {
            if (recalc) searchPath(endCol,endRow);
            if (los) attemptRangedAttack();
        }
    }

    protected void handleRangedStyle(int distance, int endCol, int endRow, boolean shouldRecalculate) {
        if (distance < aiConfig.minRangedDistanceTiles) {
            direction = getOppositeDirectionToPlayer();
            onPath = false;
            speed = Math.max(1, Math.round(originalSpeed * aiConfig.retreatSpeedMultiplier));
        } else if (distance > aiConfig.maxRangedDistanceTiles) {
            if (shouldRecalculate && !usingAlternativePath) {
                searchPath(endCol, endRow);
                lastPathRecalculation = aiConfig.longRangePathInterval;
            }
        } else {
            if (onPath && new Random().nextInt(100) < 30) onPath = false;
            direction = getDirectionTowardPlayer();
            attemptRangedAttack();
        }
    }

    protected void handleMeleeRange() {
        direction = getDirectionTowardPlayer();
        if (aiConfig.style == CombatStyle.MELEE || new Random().nextInt(100) < 50) {
            attemptMeleeStrike();
        } else {
            attemptRangedAttack();
        }
    }

    protected void handleIdleBehavior(int distance, boolean canAttackPlayer) {
        actionLockCounter++;

        if (patrolState && patrolCooldown <= 0) {
            handlePatrolBehavior(distance, canAttackPlayer);
        } else if (actionLockCounter >= aiConfig.idleActionTime) {
            chooseSmartDirection(distance, canAttackPlayer);
            actionLockCounter = 0;

            if (canAttackPlayer && aiConfig.style != CombatStyle.MELEE && distance <= 10 && new Random().nextInt(100) > aiConfig.idleRangedAttackChance) {
                attemptRangedAttack();
            }
        }
    }

    // ========== Combat System ==========

    protected void attemptMeleeStrike() {
        if (!canAttackPlayer()) return;
        if (calculateDistanceToPlayer() > aiConfig.meleeRangeTiles) return;
        if (meleeInProgress) return;
        int chance = new Random().nextInt(100) + 1;
        if (chance > aiConfig.meleeAttackChance && shotAvailableCounter >= aiConfig.meleeAttackCooldown) {
            meleeInProgress = true;
            meleeTimer = 0;
            meleePhase = MeleePhase.WINDUP;
            attacking = true;
            attackFrameIndex = 0;
            shotAvailableCounter = 0;
            attackAnimationTimer = computeAttackAnimationDuration();
            attackAnimationDuration = attackAnimationTimer;
        }
    }

    protected void attemptRangedAttack() {
        if (!canAttackPlayer()) return;
        if (aiConfig.style == CombatStyle.MELEE) return;
        int attackChance = new Random().nextInt(100) + 1;
        if (attackChance > aiConfig.rangedAttackChance && projectile != null && !projectile.alive && shotAvailableCounter >= aiConfig.rangedAttackCooldown) {
            fireProjectile();
        }
    }

    protected void fireProjectile() {
        if (projectile == null) return;
        projectile.set(x, y, direction, true, this);
        gp.projectiles.add(projectile);
        shotAvailableCounter = 0;
        attacking = true;
        attackFrameIndex = 0;
        attackAnimationTimer = computeAttackAnimationDuration();
        attackAnimationDuration = attackAnimationTimer;
    }

    private int computeAttackAnimationDuration(){
        BufferedImage[] frames = attackDirFrames[dirIndex(direction)];
        if (frames != null && frames.length > 0) {
            int needed = animAttackSpeed * frames.length;
            return Math.max(needed, aiConfig.meleeAnimationTime);
        }
        return aiConfig.meleeAnimationTime;
    }

    // ====================== RESTORED UTILITY & AI METHODS =====================
    protected int calculateDistanceToPlayer() {
        int xDist = Math.abs(x - gp.player.x);
        int yDist = Math.abs(y - gp.player.y);
        return (xDist + yDist) / gp.tileSize;
    }
    protected boolean canAttackPlayer() {
        return gp.player.health > 0 && !gp.player.invincible && hasLineOfSight();
    }
    protected boolean shouldRecalculatePath() {
        return lastPathRecalculation == 0 ||
                Math.abs(gp.player.x - prevX) > gp.tileSize / 2 ||
                Math.abs(gp.player.y - prevY) > gp.tileSize / 2;
    }
    protected String getDirectionTowardPlayer() {
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy) * 2) {
            return dx > 0 ? "right" : "left";
        } else {
            return dy > 0 ? "down" : "up";
        }
    }
    protected String getOppositeDirectionToPlayer() {
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;

        if (Math.abs(dx) > Math.abs(dy) * 2) {
            return dx > 0 ? "left" : "right";
        } else{
            return dy > 0 ? "up" : "down";
        }
    }
    protected String getPerpendicularDirection() {
        Random r = new Random();
        int dx = gp.player.x - x;
        int dy = gp.player.y - y;
        if (Math.abs(dx) > Math.abs(dy)) {
            return r.nextBoolean() ? "up" : "down";
        } else {
            return r.nextBoolean() ? "left" : "right";
        }
    }
    protected void checkIfBlockedByOtherMonsters() {
        personalSpaceCounter++;
        if (personalSpaceCounter < aiConfig.personalSpaceCheckInterval) return;
        personalSpaceCounter = 0;
        for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
            Entity other = gp.monster[gp.currentMap][i];
            if (other == null || other == this || !other.alive) continue;
            int dist = Math.abs(other.x - x) + Math.abs(other.y - y);
            if (dist < gp.tileSize && other.direction.equals(direction)) {
                if (new Random().nextInt(100) < aiConfig.crowdAvoidanceChance && !usingAlternativePath) {
                    usingAlternativePath = true;
                    alternativePathCounter = 80;
                    handleCrowdSituation();
                    return;
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
            Timer t = new Timer();
            t.schedule(new TimerTask(){public void run(){speed = originalSpeed;}} , 1000);
        } else {
            direction = getOppositeDirectionToPlayer();
            onPath = false;
            actionLockCounter = -30;
        }
    }

    protected void handlePatrolBehavior(int distance, boolean canAttackPlayer) {
        patrolMoveCounter++;
        if (patrolMoveCounter >= patrolMoveDuration) {
            choosePatrolDirection();
            patrolMoveCounter = 0;
            patrolMoveDuration = new Random().nextInt(100) + 50;
        }
        if (canAttackPlayer && distance <= aiConfig.aggroRange && new Random().nextInt(100) > aiConfig.patrolAggroChance) {
            enterAggroState();
        }
    }
    protected void choosePatrolDirection() {
        Random rand = new Random();
        int i = rand.nextInt(100) + 1;
        if (i <= 35) {
            // keep same direction
        } else if (i <= 65) {
            direction = getPerpendicularDirection();
        } else {
            String[] dir = {"up","down","left","right"};
            direction = dir[rand.nextInt(dir.length)];
        }
    }
    protected void chooseSmartDirection(int distance, boolean canAttackPlayer) {
        Random rand = new Random();
        if (canAttackPlayer && distance <= 12 && rand.nextInt(100) > aiConfig.smartDirectionChance) {
            direction = getDirectionTowardPlayer();
        } else {
            String[] dir = {"up","down","left","right"};
            direction = dir[rand.nextInt(dir.length)];
        }
    }
    protected void handleStuckSituation() {
        Random rand = new Random();
        int choice = rand.nextInt(100);
        if (choice < 30) {
            direction = getOppositeDirectionToPlayer();
            onPath = false; actionLockCounter = -70; stuckCooldown = 50;
        } else if (choice < 60) {
            direction = getPerpendicularDirection();
            onPath = false; actionLockCounter = -35; stuckCooldown = 35;
        } else if (choice < 85) {
            direction = getDirectionTowardPlayer();
            onPath = false; actionLockCounter = -25; stuckCooldown = 25;
        } else {
            onPath = false; stuckCooldown = 15;
        }
    }

    // ====================== FIX Line of Sight =============================
    protected boolean hasLineOfSight() {
        int startCol = (x + solidArea.x + solidArea.width / 2) / gp.tileSize;
        int startRow = (y + solidArea.y + solidArea.height / 2) / gp.tileSize;
        int endCol = (gp.player.x + gp.player.solidArea.x + gp.player.solidArea.width / 2) / gp.tileSize;
        int endRow = (gp.player.y + gp.player.solidArea.y + gp.player.solidArea.height / 2) / gp.tileSize;
        int dx = Math.abs(endCol - startCol);
        int sx = startCol < endCol ? 1 : -1;
        int dy = -Math.abs(endRow - startRow);
        int sy = startRow < endRow ? 1 : -1;
        int err = dx + dy;
        int col = startCol, row = startRow;
        while (true) {
            if (!(col == startCol && row == startRow) && !(col == endCol && row == endRow)) {
                if (col < 0 || row < 0 || col >= gp.maxWorldCol || row >= gp.maxWorldRow) return false;
                int tileNum = gp.tileManager.mapTileNum[gp.currentMap][col][row];
                if (gp.tileManager.tiles[tileNum] != null && gp.tileManager.tiles[tileNum].collision) return false;
            }
            if (col == endCol && row == endRow) break;
            int e2 = 2 * err;
            if (e2 >= dy) { err += dy; col += sx; }
            if (e2 <= dx) { err += dx; row += sy; }
        }
        return true;
    }

    protected float renderScale = 1f;
    public void setRenderScale(float scale){ this.renderScale = Math.max(0.1f, scale); }

    protected int dirIndex(String dir) {
        return switch (dir) {
            case "up", "up_left", "up_right" -> 0;
            case "down", "down_left", "down_right" -> 1;
            case "left" -> 2;
            case "right" -> 3;
            default -> 1;
        };
    }

    //Debug
    private static final boolean DEBUG_ANIM = false;
    protected BufferedImage[] loadFrames(String prefix, int count) {
        if (count <= 0) return null;
        BufferedImage[] temp = new BufferedImage[count];
        int loaded = 0;
        boolean warned = false;
        for (int i = 0; i < count; i++) {
            BufferedImage img = setup(prefix + (i + 1));
            if (img == null) {
                if (!warned) {
                    System.err.println("[WARN] Missing frame(s) for prefix: " + prefix + " (first missing at index " + (i+1) + ")");
                    warned = true;
                }
                continue;
            }
            temp[loaded++] = img;
        }
        if (loaded == 0) {
            if (DEBUG_ANIM) System.err.println("[ANIM] 0 frames loaded for prefix " + prefix);
            return null;
        }
        if (loaded == 1) {
            BufferedImage[] dup = new BufferedImage[2];
            dup[0] = temp[0];
            dup[1] = temp[0];
            if (DEBUG_ANIM) System.out.println("[ANIM] Only 1 frame for prefix " + prefix + ", duplicating to 2");
            return dup;
        }
        if (loaded < count) {
            if (DEBUG_ANIM) System.out.println("[ANIM] Loaded " + loaded + "/" + count + " frames for prefix " + prefix);
            BufferedImage[] trimmed = new BufferedImage[loaded];
            System.arraycopy(temp, 0, trimmed, 0, loaded);
            return trimmed;
        }
        if (DEBUG_ANIM) System.out.println("[ANIM] Loaded all " + count + " frames for prefix " + prefix);
        return temp;
    }
    protected void assignWalkFrames(int dir, BufferedImage[] frames){ walkDirFrames[dir]=frames; }
    protected void assignAttackFrames(int dir, BufferedImage[] frames){ attackDirFrames[dir]=frames; }
    protected void assignIdleFrames(int dir, BufferedImage[] frames){ idleDirFrames[dir]=frames; }

    protected boolean isOnScreen(int screenX, int screenY){
        int size = (int)(gp.tileSize * renderScale);
        return screenX + size > 0 && screenX - size < gp.screenWidth && screenY + size > 0 && screenY - size < gp.screenHeight;
    }

    @Override
    public void draw(Graphics2D g) {
        if (!hasAdvancedAnimations()) { super.draw(g); return; }
        int screenX = x - gp.player.x + gp.player.screenX;
        int screenY = y - gp.player.y + gp.player.screenY;
        if (!isOnScreen(screenX, screenY)) return;
        BufferedImage frame = resolveCurrentFrame();
        if (invincible) { hpBarOn = true; hpBarCounter = 0; }
        if (entityType == 2 && hpBarOn) {
            double oneScale = (double) gp.tileSize / maxHealth;
            double hpBarValue = oneScale * health;
            g.setColor(new Color(35,35,35));
            g.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);
            g.setColor(new Color(213,0,23,255));
            g.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
            hpBarCounter++;
            if (hpBarCounter > 600) { hpBarCounter = 0; hpBarOn = false; }
        }
        Composite originalComposite = g.getComposite();
        if (invincible) changeAlpha(g, 0.5f); else if (!dying) changeAlpha(g, 1f);
        if (dying && (deathDirFrames[0] == null && deathFrames == null)) {
            dyingAnimation(g);
        }
        int drawW = (int)(gp.tileSize * renderScale);
        int drawH = (int)(gp.tileSize * renderScale);
        int drawX = screenX - (drawW - gp.tileSize)/2;
        int drawY = screenY - (drawH - gp.tileSize);
        g.drawImage(frame, drawX, drawY, drawW, drawH, null);
        g.setComposite(originalComposite);
    }
}
