package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import object.*;
import tiles_interactive.IT_DryTree;
import tiles_interactive.IT_Wall;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Player extends Entity {
    KeyHandler keyHandler;

    public int screenX;
    public int screenY;

    public double currentSpeed = 0;

    public static final int PARRY_WINDOW_FRAMES = 10;
    private String moveDirection = "down";
    private String facingDirection = "down";
    private String guardDirection = null;

    private boolean hasHit = false;
    private int attackCounter = 0;

    public final int meleeReach;
    public final int meleeSideInflate;

    public int maxDrunk;
    public int drunk;
    public int drinkPercent;
    public final int maxDrinkPercent=100;

    private int baseStrength;
    private int baseDexterity;

    public AffineTransform drunkOriginalTx;

    private int walkAnimationFrame = 0;
    private int attackAnimationFrame = 0;

    public BufferedImage[] upImages = new BufferedImage[10];
    public BufferedImage[] downImages = new BufferedImage[10];
    public BufferedImage[] leftImages = new BufferedImage[10];
    public BufferedImage[] rightImages = new BufferedImage[10];

    public BufferedImage[] sleep=new BufferedImage[15];
    public BufferedImage[] wake=new BufferedImage[13];
    public BufferedImage idle_up, idle_down, idle_left, idle_right;
    public BufferedImage[] idleImages=new  BufferedImage[4];
    private boolean longIdle = false;
    private int longIdleCounter = 0;
    private int longIdleAnimFrame = 0;
    private int longIdleAnimTick = 0;

    public BufferedImage[] attackUpImages = new BufferedImage[5];
    public BufferedImage[] attackDownImages = new BufferedImage[5];
    public BufferedImage[] attackLeftImages = new BufferedImage[5];
    public BufferedImage[] attackRightImages = new BufferedImage[5];

    public BufferedImage[] dieImages = new BufferedImage[36];
    private boolean deathSfxPlayed=false;

    private final Random rng = new Random();


    public Player(GamePanel gp, KeyHandler kh) {
        super(gp);
        this.keyHandler = kh;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        meleeReach= (int) (gp.tileSize*1.5);
        meleeSideInflate=gp.tileSize;
        solidArea = new Rectangle(8, 16, 16, 16);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        getPlayerIdleImages();
        setItems();
        setDialogue();
    }

    public void setDefaultValues() {

        setDefaultPosition();

        dieFrame=0;
        deathSfxPlayed=false;

        speed = 4;

        maxHealth = 100;
        health = maxHealth;
        coin=1000;

        maxDrunk = 6;
        drunk = 0;
        drinkPercent=0;

        level = 1;
        baseStrength=1;
        baseDexterity = 1;
        strength = baseStrength;
        dexterity = baseDexterity;
        exp=0;
        nextLevelExp=10;


        currentWeapon=new Obj_Wooden_Sword(gp);
        currentShield=new Obj_Shield(gp);
        projectile=new Obj_Dagger(gp);

        reStats();
    }

    public void setDefaultPosition(){
        x = gp.tileSize * 23;
        y = gp.tileSize * 23;
//        gp.currentMap=2;
//        x = gp.tileSize * 9;
//        y = gp.tileSize * 42;
        moveDirection = "down";
        facingDirection = "down";
        invincible=false;
        transparent=false;
        attacking=false;
        guard=false;
        actionLockCounter = 0;
        attackCounter = 0;
        longIdleCounter = 0;
        spriteCounter = 0;
        dyingCounter = 0;
        invincibleCounter = 0;
        guardCounter = 0;
        offBalanceCounter = 0;
        shotAvailableCounter=0;
        idleCounter = 0;

        gp.gameState = gp.playState;
    }

    public void restoreStatus(){
        maxHealth = 6;
        health = maxHealth;

    }

    private List<Entity> getEquippedItems(){
        List<Entity> list=new ArrayList<>();
        if(currentWeapon!=null){list.add(currentWeapon);}
        if(currentShield!=null){list.add(currentShield);}
        if(currentHelmet!=null){list.add(currentHelmet);}
        if(currentChest!=null){list.add(currentChest);}
        if(currentBoots!=null){list.add(currentBoots);}
        return list;
    }

    public void reStats(){
        int totalStrength=baseStrength;
        int totalDexterity=baseDexterity;
        int weaponScale=(currentWeapon!=null)?currentWeapon.attackValue:0;
        int flatAttackBonus=0;
        int flatDefenseBonus=0;
        int armourDefenseSum=0;

        for(Entity e:getEquippedItems()){
            totalStrength+=e.strengthBonus;
            totalDexterity+=e.dexterityBonus;
            flatAttackBonus+=e.attackFlatBonus;
            armourDefenseSum+=e.defenseValue;
            flatDefenseBonus+=e.defenseFlatBonus;

        }
        strength=totalStrength;
        dexterity=totalDexterity;

        attack=(strength*weaponScale)+flatAttackBonus;
        defense=(dexterity*armourDefenseSum)+flatDefenseBonus;
        if(attack<0) attack=0;
        if(defense<0) defense=0;
    }

    private void updateDrunkFromPercent(){
        float units=(drinkPercent/100f)*maxDrunk;
        drunk=Math.round(units);
        if(drunk>maxDrunk) drunk=maxDrunk;
        if(drunk<0) drunk=0;
    }

    private void consumeDrunk(Entity drink){
        if(drink.alcohol>0){
            drinkPercent+=drink.alcohol;
            if(drinkPercent>maxDrinkPercent) drinkPercent=maxDrinkPercent;
            updateDrunkFromPercent();
            if(drink.amount>1){
                drink.amount--;
            }
           else {
                inventory.remove(drink);
            }
            gp.se.playSE(20);
            gp.ui.addMessage("Alcohol +" + drink.alcohol + "% (" + drinkPercent + "%)");
        }
    }

    public void setItems(){
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new Obj_Gold_Key(gp));
        inventory.add(new Obj_Pickaxe(gp));

    }

    public void getPlayerImage() {
        try {
            for (int i = 0; i < 10; i++) {
                upImages[i] = setup("player/pirate_walk_up" + (i + 1));
                downImages[i] = setup("player/pirate_walk_down" + (i + 1));
                leftImages[i] = setup("player/pirate_walk_left" + (i + 1));
                rightImages[i] = setup("player/pirate_walk_right" + (i + 1));
            }

            idle_up = setup("player/pirate_walk_up1");
            idle_down = setup("player/pirate_walk_down1");
            idle_left = setup("player/pirate_walk_left1");
            idle_right = setup("player/pirate_walk_right1");

            for (int i = 0; i < 36; i++) {
                dieImages[i] = setup("player/pirate_die" + (i));
            }
            for(int i=0;i<15;i++){
                sleep[i]=setup("player/sleep_" + (i+1));
            }
            for(int i=0;i<13;i++){
                wake[i]=setup("player/wake_" + (i+1));
            }

            guardDown = setup("player/shield_down");
            guardUp = setup("player/shield_up");
            guardLeft = setup("player/shield_left");
            guardRight = setup("player/shield_right");
        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    public void getPlayerAttackImage() {
        try {
            for (int i = 0; i < 5; i++) {
                attackUpImages[i] = setup("player/pirate_attack_up" + (i + 1));
                attackDownImages[i] = setup("player/pirate_attack_down" + (i + 1));
                attackLeftImages[i] = setup("player/pirate_attack_left" + (i + 1));
                attackRightImages[i] = setup("player/pirate_attack_right" + (i + 1));
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    public void getPlayerIdleImages() {
        try {
            for (int i = 0; i < 4; i++) {
                idleImages[i] = setup("player/idle" + (i + 1));
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    public void recenter() {
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
    }

    public String getGuardDirection(){ return guardDirection; }
    private void spawnGuardParticles(boolean parry){
        Color c = parry ? new Color(255,215,0) : new Color(140,160,255);
        int size = parry ? 6 : 4;
        int speed = parry ? 2 : 1;
        int life = parry ? 22 : 14;
        for(int i=0;i<10;i++){
            int vx = (int)Math.round((Math.random()*2)-1);
            int vy = (int)Math.round((Math.random()*2)-1);
            if(vx==0 && vy==0) vy=1;
            gp.particles.add(new Particle(gp,this,c,vx,vy,size,speed,life));
        }
    }
    public void onParry(){ spawnGuardParticles(true); }
    public void onGuardBlock(){ spawnGuardParticles(false); }

    public void update() {

        if (health <= 0) {
            if (gp.gameState == gp.gameOverState) return;
            if (!deathSfxPlayed) {
                gp.music.stopMusic();
                gp.music.playSE(29);
                deathSfxPlayed = true;
            }

            if (dieFrame < 35) {
                if (spriteCounter % 10 == 0) {
                    dieFrame++;
                }
                spriteCounter++;
            } else {
                if (spriteCounter > 60) {
                    gp.se.playSE(28);
                    gp.music.stopMusic();
                    gp.gameState = gp.gameOverState;
                    deathSfxPlayed = false;
                    gp.ui.commandNum=0;
                    gp.music.playMusic(14);
                    spriteCounter = 0;
                } else {
                    spriteCounter++;
                }
            }
            return;
        }

        if(drinkPercent>99 && !gp.ui.sleepActive){
            if(rng.nextInt(10)<=5){
                gp.music.playSE(6);
            }
            else {
                gp.music.playSE(7);
            }
            gp.ui.startSleep();
        }

        double dx = 0, dy = 0;
        boolean moved = false;

        if (!attacking && keyHandler.attackClicked) {
            attacking = true;
            keyHandler.attackClicked = false;
            attackCounter = 0;
            hasHit = false;
            gp.se.playSE(17);
        }

        boolean up = keyHandler.upPressed;
        boolean down = keyHandler.downPressed;
        boolean left = keyHandler.leftPressed;
        boolean right = keyHandler.rightPressed;

        if (up && left) {
            dx = -1; dy = -1;
            moveDirection = "up_left";
        } else if (up && right) {
            dx = 1; dy = -1;
            moveDirection = "up_right";
        } else if (down && left) {
            dx = -1; dy = 1;
            moveDirection = "down_left";
        } else if (down && right) {
            dx = 1; dy = 1;
            moveDirection = "down_right";
        } else if (up) {
            dy = -1; moveDirection = "up";
        } else if (down) {
            dy = 1; moveDirection = "down";
        } else if (left) {
            dx = -1; moveDirection = "left";
        } else if (right) {
            dx = 1; moveDirection = "right";
        }

        if (dx != 0 || dy != 0) {
            facingDirection = moveDirection;
            moved = true;
        }

        if (dx != 0 && dy != 0) {
            dx /= Math.sqrt(2);
            dy /= Math.sqrt(2);
        }


        double acceleration = 0.20;
        double maxSpeed = 4.0;
        double deceleration = 0.35;
        double minStopSpeed = 0.10;

        if(moved || attacking) {
            longIdle=false;
            longIdleCounter = 0;
            longIdleAnimFrame = 0;
            longIdleAnimTick = 0;
            currentSpeed = Math.min(currentSpeed + acceleration, maxSpeed);
        } else {
            currentSpeed = Math.max(0, currentSpeed - deceleration);
            if (currentSpeed < minStopSpeed) {
                currentSpeed = 0;
                walkAnimationFrame = 0;
            }
            if (!longIdle) {
                int longIdleDelay = 300;
                if (longIdleCounter < longIdleDelay) {
                    longIdleCounter++;
                    if (longIdleCounter == longIdleDelay) {
                        longIdle = true;
                    }
                }
            } else {
                longIdleAnimTick++;
                int longIdleAnimSpeed = 30;
                if (longIdleAnimTick >= longIdleAnimSpeed) {
                    longIdleAnimTick = 0;
                    longIdleAnimFrame = (longIdleAnimFrame + 1) % idleImages.length;
                }
            }
        }

        double[] wobble={dx,dy};
        gp.drinkSystem.distortInput(this,wobble);
        dx=wobble[0];
        dy=wobble[1];

        double futureX = x + dx * currentSpeed;
        double futureY = y + dy * currentSpeed;

        boolean canMoveX = true, canMoveY = true;

        // Tile collision check for X movement
        if (dx != 0) {
            double tempX = x;
            x = (int) futureX;
            direction = dx > 0 ? "right" : "left";
            collisionOn = false;
            gp.collisionCheck.checkTile(this);
            canMoveX = !collisionOn;
            x = (int) tempX;
        }

        // Tile collision check for Y movement
        if (dy != 0) {
            double tempY = y;
            y = (int) futureY;
            direction = dy > 0 ? "down" : "up";
            collisionOn = false;
            gp.collisionCheck.checkTile(this);
            canMoveY = !collisionOn;
            y = (int) tempY;
        }

        // NPC collision check for X movement
        if (dx != 0 && canMoveX) {
            double tempX = x;
            x = (int) futureX;
            direction = dx > 0 ? "right" : "left";
            int npcIndexX = gp.collisionCheck.checkEntity(this, gp.npc);
            if (npcIndexX != 999) canMoveX = false;
            x = (int) tempX;
        }

        //InteractiveTiles check for X movement
        if (dx != 0 && canMoveX) {
            double tempX = x;
            x = (int) futureX;
            direction = dx > 0 ? "right" : "left";
            int iTileIndexX = gp.collisionCheck.checkEntity(this, gp.iTile);
            if (iTileIndexX != 999) canMoveX = false;
            x = (int) tempX;
        }

        // NPC collision check for Y movement
        if (dy != 0 && canMoveY) {
            double tempY = y;
            y = (int) futureY;
            direction = dy > 0 ? "down" : "up";
            int npcIndexY = gp.collisionCheck.checkEntity(this, gp.npc);
            if (npcIndexY != 999) canMoveY = false;
            y = (int) tempY;
        }

        //InteractiveTiles check for Y movement
        if (dy != 0 && canMoveY) {
            double tempY = y;
            y = (int) futureY;
            direction = dy > 0 ? "down" : "up";
            int iTileIndexY = gp.collisionCheck.checkEntity(this, gp.iTile);
            if (iTileIndexY != 999) canMoveY = false;
            y = (int) tempY;
        }

        // Object collision check for X movement
        if (dx != 0 && canMoveX) {
            double tempX = x;
            x = (int) futureX;
            direction = dx > 0 ? "right" : "left";

            // Check for blocking objects
            for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                if (gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].collision) {
                    collisionOn = false;
                    gp.collisionCheck.checkObject(this,i, gp.obj[gp.currentMap]);
                    if (collisionOn) {
                        canMoveX = false;
                        break;
                    }
                }
            }
            x = (int) tempX;
        }
        // Object collision check for Y movement
        if (dy != 0 && canMoveY) {
            double tempY = y;
            y = (int) futureY;
            direction = dy > 0 ? "down" : "up";

            // Check for blocking objects
            for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                if (gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].collision) {
                    collisionOn = false;
                    gp.collisionCheck.checkObject(this,i, gp.obj[gp.currentMap]);
                    if (collisionOn) {
                        canMoveY = false;
                        break;
                    }
                }
            }
            y = (int) tempY;
        }

        direction = moveDirection;

        // Object collision
        int objIndex = gp.collisionCheck.checkObjForInteraction(this);
        pickUpObj(objIndex);

        // NPC interaction
        collisionOn = false;
        gp.collisionCheck.checkEntity(this, gp.npc);
        interactNPC();

        // Monster collision
        int monsterIndex = gp.collisionCheck.checkEntity(this, gp.monster);
        contactMonster(monsterIndex);

        gp.collisionCheck.checkEntity(this, gp.iTile);

        // Check events
        gp.eventHandler.checkEvent();

        gp.keyHandler.ePressed=false;

        if (canMoveX) x += (int) (dx * currentSpeed);
        if (canMoveY) y += (int) (dy * currentSpeed);

        if (!attacking) {
            if (moved && currentSpeed > 0) {
                spriteCounter++;
                if (spriteCounter > 5) {
                    walkAnimationFrame = (walkAnimationFrame + 1) % 10;
                    spriteCounter = 0;
                }
            } else if(currentSpeed==0){
                walkAnimationFrame = 0;
            }
        }

        if (attacking) {
            attack();
        } else {
            boolean q = keyHandler.qPressed;
            if(q){
                if(!guard){
                    guard = true;
                    guardCounter = 0;
                    guardDirection = facingDirection;
                } else {
                    guardCounter++;
                }
            } else {
                guard = false;
                guardDirection = null;
            }
        }

        if(gp.keyHandler.shotKeyPressed && !projectile.alive && shotAvailableCounter==60){
            projectile.set(x,y,facingDirection,true,this);
            gp.projectiles.add(projectile);

            shotAvailableCounter=0;

            gp.se.playSE(21);
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                transparent=false;
                invincibleCounter = 0;
            }
        }

        if(shotAvailableCounter<60){
            shotAvailableCounter++;
        }
        updateDrunkFromPercent();

    }

    public void attack(){
        attackCounter++;
        if (attackCounter <= 5) {
            attackAnimationFrame = 0;
        } else if (attackCounter <= 25) {
            int frameIndex = (attackCounter - 5) / 4;
            attackAnimationFrame = Math.min(frameIndex, 4);

            if (attackCounter == 15 && !hasHit) {
                Rectangle area = buildAttackArea();
                checkAttackHit(area, attack, false);
                damageInteractiveTile(area);
                hasHit = true;
            }
        } else {
            attacking = false;
            attackCounter = 0;
            hasHit = false;
            attackAnimationFrame = 0;
        }
    }

    private Rectangle buildAttackArea(){
        int range = meleeReach;
        int baseX = x + solidArea.x;
        int baseY = y + solidArea.y;
        int w = solidArea.width;
        int h = solidArea.height;
        Rectangle r = new Rectangle();

        int leftInflate = meleeSideInflate;
        int rightInflate = meleeSideInflate;
        int topInflate = meleeSideInflate;
        int bottomInflate = meleeSideInflate;

        switch (facingDirection) {
            case "up":
                r.setBounds(
                        baseX - leftInflate,
                        baseY - range,
                        w + leftInflate + rightInflate,
                        range + topInflate
                );
                break;
            case "left":
                r.setBounds(
                        baseX - range,
                        baseY - topInflate,
                        range + leftInflate,
                        h + topInflate + bottomInflate
                );
                break;
            case "right":
                r.setBounds(
                        baseX + w - rightInflate,
                        baseY - topInflate,
                        range + rightInflate,
                        h + topInflate + bottomInflate
                );
                break;
            case "up_left":
                r.setBounds(
                        baseX - range,
                        baseY - range,
                        range + w/2 + leftInflate,
                        range + h/2 + topInflate
                );
                break;
            case "up_right":
                r.setBounds(
                        baseX + w/2 - rightInflate,
                        baseY - range,
                        range + w/2 + rightInflate,
                        range + h/2 + topInflate
                );
                break;
            case "down_left":
                r.setBounds(
                        baseX - range,
                        baseY + h/2 - topInflate,
                        range + w/2 + leftInflate,
                        range + h/2 + bottomInflate
                );
                break;
            case "down_right":
                r.setBounds(
                        baseX + w/2 - rightInflate,
                        baseY + h/2 - topInflate,
                        range + w/2 + rightInflate,
                        range + h/2 + bottomInflate
                );
                break;
            case "down":
            default:
                r.setBounds(baseX - leftInflate, baseY + h - topInflate,
                        w + leftInflate + rightInflate, range + bottomInflate);
        }
        return r;
    }

    public void checkAttackHit(Rectangle area,int damageValue, boolean fromProjectile){
        for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
            Entity mon = gp.monster[gp.currentMap][i];
            if (mon == null || mon.dying || !mon.alive) continue;

            Rectangle monBox = new Rectangle(mon.x + mon.solidArea.x,
                    mon.y + mon.solidArea.y,
                    mon.solidArea.width,
                    mon.solidArea.height);
            if (!area.intersects(monBox)) continue;

            gp.se.playSE(16);

            int damage;
            damage=damageValue-mon.defense;
            if(damage<=0){
                damage=1;
            }
            mon.health -= damage;

            mon.hpBarOn = true;
            mon.hpBarCounter = 0;
            mon.invincible = true;
            mon.invincibleCounter = 0;
            mon.damageReaction();

            if (mon.health <= 0) {
                gp.se.playSE(23);
                gp.ui.addMessage(mon.exp + " EXP");
                exp += mon.exp;
                checkLevelUp();
                mon.dying = true;
            }

            if (!fromProjectile) {
                break;
            }
        }
    }

    public void damageInteractiveTile(Rectangle attackArea){
        int playerCenterX = x + solidArea.x + solidArea.width/2;
        int playerCenterY = y + solidArea.y + solidArea.height/2;

        List<Integer> hits = new ArrayList<>();
        for(int i=0;i<gp.iTile[gp.currentMap].length;i++){
            if(gp.iTile[gp.currentMap][i]==null) continue;
            if(!gp.iTile[gp.currentMap][i].destructible) continue;
            if(gp.iTile[gp.currentMap][i].invincible) continue;

            Rectangle tileBox = new Rectangle(
                    gp.iTile[gp.currentMap][i].x + gp.iTile[gp.currentMap][i].solidArea.x,
                    gp.iTile[gp.currentMap][i].y + gp.iTile[gp.currentMap][i].solidArea.y,
                    gp.iTile[gp.currentMap][i].solidArea.width,
                    gp.iTile[gp.currentMap][i].solidArea.height
            );
            if(attackArea.intersects(tileBox)){
                hits.add(i);
            }
        }

        if(hits.isEmpty()) return;

        int chosenIndex = hits.getFirst();
        long bestDist = Long.MAX_VALUE;
        for(int idx : hits){
            Entity t = gp.iTile[gp.currentMap][idx];
            int cx = t.x + t.solidArea.x + t.solidArea.width/2;
            int cy = t.y + t.solidArea.y + t.solidArea.height/2;
            long dx = cx - playerCenterX;
            long dy = cy - playerCenterY;
            long d2 = dx*dx + dy*dy;
            if(d2 < bestDist){
                bestDist = d2;
                chosenIndex = idx;
            }
        }

        if(!gp.iTile[gp.currentMap][chosenIndex].isCorrectItem(this)){
            gp.se.playSE(27);
            if(gp.iTile[gp.currentMap][chosenIndex] instanceof IT_DryTree) {
                gp.ui.addMessage("Need an Axe");
            }else if(gp.iTile[gp.currentMap][chosenIndex] instanceof IT_Wall) {
                gp.ui.addMessage("Need an Pickaxe");
            }
            return;
        }

        gp.iTile[gp.currentMap][chosenIndex].playSE();
        gp.iTile[gp.currentMap][chosenIndex].health--;
        gp.iTile[gp.currentMap][chosenIndex].invincible = true;
        generateParticle(gp.iTile[gp.currentMap][chosenIndex],gp.iTile[gp.currentMap][chosenIndex]);
        if(gp.iTile[gp.currentMap][chosenIndex].health<=0){
            gp.iTile[gp.currentMap][chosenIndex].health=0;
            if(gp.iTile[gp.currentMap][chosenIndex] instanceof IT_DryTree) {
                gp.ui.addMessage("Tree felled");
                gp.iTile[gp.currentMap][chosenIndex] = gp.iTile[gp.currentMap][chosenIndex].getDestroyedFrom();
            }
            else if(gp.iTile[gp.currentMap][chosenIndex] instanceof IT_Wall) {
                gp.ui.addMessage("Wall destroyed");
                gp.iTile[gp.currentMap][chosenIndex].checkDrop();
                gp.iTile[gp.currentMap][chosenIndex] = gp.iTile[gp.currentMap][chosenIndex].getDestroyedFrom();

            }
        }
    }

    public void pickUpObj(int i) {
        if (i != 999) {
            Entity obj = gp.obj[gp.currentMap][i];

            if (!obj.pickable && obj.obstacle) {
                if (keyHandler.ePressed) {
                    obj.interact();
                }
                return;
            } else if (!obj.pickable) {
                return;
            }

            if (gp.obj[gp.currentMap][i].gearType == 3) {
                gp.obj[gp.currentMap][i].use(this);
            } else {
                String text;
                if (canObtainItem(gp.obj[gp.currentMap][i])) {
                    gp.se.playSE(2);
                    text = "Picked up " + gp.obj[gp.currentMap][i].name;
                } else {
                    text = "Inventory Full";
                }
                gp.ui.addMessage(text);
            }
            gp.obj[gp.currentMap][i] = null;
        }
    }

    public void interactNPC() {
        if (gp.keyHandler.ePressed) {
            int nearestNPC = findNearestNPC();
            if (nearestNPC != 999) {
                gp.npc[gp.currentMap][nearestNPC].speak();
            }
        }

        if (isMoving()) {
            int nearestNPC = findNearestNPCForPushing();
            if (nearestNPC != 999) {
                Entity npc = gp.npc[gp.currentMap][nearestNPC];
                if (npc.name != null && npc.name.equals("Rock")) {
                    if (isMovingTowardsNPC(npc)) {
                        npc.move(direction);
                    }
                }
            }
        }
    }


    private int findNearestNPCForPushing() {
        int nearestIndex = 999;
        double shortestDistance = Double.MAX_VALUE;
        int pushingRange = gp.tileSize;

        for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
            if (gp.npc[gp.currentMap][i] != null) {
                Entity npc = gp.npc[gp.currentMap][i];

                if (isPlayerTouchingNPC(npc)) {
                    double playerCenterX = x + gp.tileSize / 2.0;
                    double playerCenterY = y + gp.tileSize / 2.0;
                    double npcCenterX = npc.x + gp.tileSize / 2.0;
                    double npcCenterY = npc.y + gp.tileSize / 2.0;

                    double distance = Math.sqrt(
                            Math.pow(playerCenterX - npcCenterX, 2) +
                                    Math.pow(playerCenterY - npcCenterY, 2)
                    );

                    if (distance <= pushingRange && distance < shortestDistance) {
                        shortestDistance = distance;
                        nearestIndex = i;
                    }
                }
            }
        }

        return nearestIndex;
    }

    private boolean isPlayerTouchingNPC(Entity npc) {
        Rectangle playerRect = new Rectangle(
            x + solidArea.x,
            y + solidArea.y,
            solidArea.width,
            solidArea.height
        );

        int buffer = 8;
        Rectangle npcRect = new Rectangle(
            npc.x + npc.solidArea.x - buffer,
            npc.y + npc.solidArea.y - buffer,
            npc.solidArea.width + (buffer * 2),
            npc.solidArea.height + (buffer * 2)
        );

        return playerRect.intersects(npcRect);
    }

    private boolean isMoving() {
        return gp.keyHandler.upPressed || gp.keyHandler.downPressed ||
               gp.keyHandler.leftPressed || gp.keyHandler.rightPressed;
    }

    private boolean isMovingTowardsNPC(Entity npc) {
        int playerCenterX = x + solidArea.x + solidArea.width / 2;
        int playerCenterY = y + solidArea.y + solidArea.height / 2;
        int npcCenterX = npc.x + npc.solidArea.x + npc.solidArea.width / 2;
        int npcCenterY = npc.y + npc.solidArea.y + npc.solidArea.height / 2;

        switch (direction) {
            case "up":
                return playerCenterY > npcCenterY + (npc.solidArea.height / 4);
            case "down":
                return playerCenterY < npcCenterY - (npc.solidArea.height / 4);
            case "left":
                return playerCenterX > npcCenterX + (npc.solidArea.width / 4);
            case "right":
                return playerCenterX < npcCenterX - (npc.solidArea.width / 4);
            default:
                return false;
        }
    }

    private int findNearestNPC() {
        int nearestIndex = 999;
        double shortestDistance = Double.MAX_VALUE;
        int interactionRange = gp.tileSize *2;

        for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
            if (gp.npc[gp.currentMap][i] != null) {
                double playerCenterX = x + gp.tileSize / 2.0;
                double playerCenterY = y + gp.tileSize / 2.0;
                double npcCenterX = gp.npc[gp.currentMap][i].x + gp.tileSize / 2.0;
                double npcCenterY = gp.npc[gp.currentMap][i].y + gp.tileSize / 2.0;

                double distance = Math.sqrt(
                        Math.pow(playerCenterX - npcCenterX, 2) +
                                Math.pow(playerCenterY - npcCenterY, 2)
                );

                if (distance <= interactionRange && distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestIndex = i;
                }
            }
        }

        return nearestIndex;
    }

    public void contactMonster(int i) {
        if (i != 999 && !invincible && !gp.monster[gp.currentMap][i].dying) {
            gp.se.playSE(18);
            if(gp.monster[gp.currentMap][i].offBalance){
                attack*=2;
            }
            int damage=gp.monster[gp.currentMap][i].attack-defense;
            if(damage<=0){
                damage=1;
            }
            health -= damage;
            if(health<=0){
                health=0;
            }
            invincible = true;
            transparent=true;
            damageReaction();
        }
    }

    public void draw(Graphics2D g2d) {
        recenter();
        BufferedImage image = null;

        if (health <= 0) {
            image = dieImages[Math.min(dieFrame, 35)];
            g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            return;
        }

        if (longIdleAnimFrame >= idleImages.length) {
            longIdleAnimFrame = 0;
        }

        if (longIdle) {
            image = idleImages[longIdleAnimFrame];
        }else if (attacking) {
                switch (facingDirection) {
                    case "up", "up_left", "up_right" -> image = attackUpImages[attackAnimationFrame];
                    case "down", "down_left", "down_right" -> image = attackDownImages[attackAnimationFrame];
                    case "left" -> image = attackLeftImages[attackAnimationFrame];
                    case "right" -> image = attackRightImages[attackAnimationFrame];
                }
            } else if(guard)
        {
            String dir = guardDirection != null ? guardDirection : facingDirection;
            switch (dir) {
                case "up", "up_left", "up_right" -> image = guardUp;
                case "down", "down_left", "down_right" -> image = guardDown;
                case "left" -> image = guardLeft;
                case "right" -> image = guardRight;
            }
        }
        else {
            if (currentSpeed > 0) {
                switch (moveDirection) {
                    case "up" -> image = upImages[walkAnimationFrame];
                    case "down" -> image = downImages[walkAnimationFrame];
                    case "left", "up_left", "down_left" -> image = leftImages[walkAnimationFrame];
                    case "right", "up_right", "down_right" -> image = rightImages[walkAnimationFrame];
                }
            } else {
                image = switch (facingDirection) {
                    case "up", "up_left", "up_right" -> idle_up;
                    case "down", "down_left", "down_right" -> idle_down;
                    case "left" -> idle_left;
                    case "right" -> idle_right;
                    default -> idle_down;
                };
            }
        }

        if(image==null){
            image=idleImages[3];
        }

            Composite originalComposite = g2d.getComposite();
            if (transparent) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            }

            g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            g2d.setComposite(originalComposite);

    }

    private boolean collidesTilesAt(int nx, int ny){
        int leftWorldX   = nx + solidArea.x;
        int rightWorldX  = nx + solidArea.x + solidArea.width  - 1;
        int topWorldY    = ny + solidArea.y;
        int bottomWorldY = ny + solidArea.y + solidArea.height - 1;
        int leftCol   = leftWorldX / gp.tileSize;
        int rightCol  = rightWorldX / gp.tileSize;
        int topRow    = topWorldY / gp.tileSize;
        int bottomRow = bottomWorldY / gp.tileSize;
        int maxCol = gp.tileManager.mapTileNum[gp.currentMap].length - 1;
        int maxRow = gp.tileManager.mapTileNum[gp.currentMap][0].length - 1;
        if(leftCol<0) leftCol=0; if(rightCol<0) rightCol=0; if(topRow<0) topRow=0; if(bottomRow<0) bottomRow=0;
        if(leftCol>maxCol) leftCol=maxCol; if(rightCol>maxCol) rightCol=maxCol; if(topRow>maxRow) topRow=maxRow; if(bottomRow>maxRow) bottomRow=maxRow;
        return gp.tileManager.isCollision(gp.currentMap,leftCol,topRow)
                || gp.tileManager.isCollision(gp.currentMap,rightCol,topRow)
                || gp.tileManager.isCollision(gp.currentMap,leftCol,bottomRow)
                || gp.tileManager.isCollision(gp.currentMap,rightCol,bottomRow);
    }
    private void safePixelMove(int dx, int dy){
        if(dx==0 && dy==0) return;
        int nx = x + dx;
        int ny = y + dy;
        if(!collidesTilesAt(nx, ny)){
            x = nx; y = ny;
        }
    }
    private void safeKnockback(int dx, int dy){
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        if(steps==0) return;
        double stepX = dx / (double)steps;
        double stepY = dy / (double)steps;
        double accX = 0, accY = 0;
        for(int i=0;i<steps;i++){
            accX += stepX;
            accY += stepY;
            int moveX = (int)Math.round(accX);
            int moveY = (int)Math.round(accY);
            if(moveX!=0 || moveY!=0){
                safePixelMove(moveX, moveY);
                accX -= moveX;
                accY -= moveY;
            }
        }
    }

    public void damageReaction() {
        int knock = gp.tileSize / 4;
        switch (facingDirection) {
            case "up","up_left","up_right" -> safeKnockback(0, knock);
            case "down","down_left","down_right" -> safeKnockback(0, -knock);
            case "left" -> safeKnockback(knock, 0);
            case "right" -> safeKnockback(-knock, 0);
        }
    }

    public void setDialogue(){
        dialogue[0][0]="Ahoy, you are at another level,"+level+"!\nStats improved";

    }

    public void checkLevelUp(){
        if(exp>=nextLevelExp){
            gp.se.playSE(11);
            level++;
            nextLevelExp=nextLevelExp*2;
            maxHealth+=1;
            health=maxHealth;
            gp.drinkSystem.soberUp(gp.player);
            baseStrength++;
            baseDexterity++;
            reStats();
            coin+=exp;
            exp=0;
            setDialogue();
            startDialogue(this,0);
        }
    }

    public boolean isEquipped(Entity item){
        return item!=null && (item==currentWeapon || item==currentShield ||
                item==currentHelmet || item==currentChest || item==currentBoots);
    }

    private void unequipItem(Entity item){
        if(item==null) return;
        if(item==currentWeapon) currentWeapon=null;
        else if(item==currentShield) currentShield=null;
        else if(item==currentHelmet) currentHelmet=null;
        else if(item==currentChest) currentChest=null;
        else if(item==currentBoots) currentBoots=null;
        gp.ui.addMessage(item.name+" unequipped");
        reStats();
    }

    private void equipItem(Entity item){
        if(item==null) return;
        if(item.gearType==0){
            currentWeapon=item;
        } else if(item.gearType==1){
            currentShield=item;
        } else if(item.armourType==0){
            currentHelmet=item;
        } else if(item.armourType==1){
            currentChest=item;
        } else if(item.armourType==2){
            currentBoots=item;
        } else {
            return;
        }
        gp.ui.addMessage(item.name+" equipped");
        reStats();
    }

    private boolean isEquipment(Entity item){
        return item.gearType==0 || item.gearType==1 || item.armourType>=0;
    }

    public void selectItem(){
        int itemIndex=gp.ui.getItemIndexSlot(gp.ui.slotCol,gp.ui.slotRow);
        if(itemIndex>=inventory.size()) return;

        Entity selectedItem=inventory.get(itemIndex);

        if(selectedItem.gearType==2){
            if(selectedItem.alcohol>0){
                consumeDrunk(selectedItem);
            } else {
                if(selectedItem.use(this)) {
                    if(selectedItem.amount>1){
                        selectedItem.amount--;
                    }else{
                        inventory.remove(selectedItem);
                    }
                    gp.ui.addMessage(selectedItem.name + " used");
                }
            }
            return;
        }

        if(isEquipment(selectedItem)){
            if(isEquipped(selectedItem)){
                unequipItem(selectedItem);
            } else {
                equipItem(selectedItem);
            }
        }
    }

    public BufferedImage getCurrentFrame() {
        if (dead) return dieImages[Math.min(dieFrame, 2)];

        if (attacking) {
            return switch (facingDirection) {
                case "up", "up_left", "up_right" -> attackUpImages[attackAnimationFrame];
                case "down", "down_left", "down_right" -> attackDownImages[attackAnimationFrame];
                case "left" -> attackLeftImages[attackAnimationFrame];
                case "right" -> attackRightImages[attackAnimationFrame];
                default -> downImages[0];
            };
        }

        if (currentSpeed > 0) {
            return switch (moveDirection) {
                case "up" -> upImages[walkAnimationFrame];
                case "down" -> downImages[walkAnimationFrame];
                case "left", "up_left", "down_left" -> leftImages[walkAnimationFrame];
                case "right", "up_right", "down_right" -> rightImages[walkAnimationFrame];
                default -> downImages[0];
            };
        }

        return switch (facingDirection) {
            case "up", "up_left", "up_right" -> idle_up;
            case "down", "down_left", "down_right" -> idle_down;
            case "left" -> idle_left;
            case "right" -> idle_right;
            default -> idle_down;
        };
    }

    public boolean canObtainItem(Entity item){
        boolean canObtain=false;
        Entity newItem=gp.eGen.getObject(item.name);
        if(newItem.stackable){
            int index=searchItemInInventory(newItem.name);
            if(index!=999){
                inventory.get(index).amount++;
                canObtain=true;
            }else{
                if(inventory.size()<maxInventorySize){
                    inventory.add(newItem);
                    canObtain=true;
                }
            }
        }else{
            if(inventory.size()<maxInventorySize){
                inventory.add(newItem);
                canObtain=true;
            }
        }
     return canObtain;
    }

    public int getSlotForGear(Entity item){
        if(item==null) return -1;

        for(int i=0;i<inventory.size();i++){
            if(Objects.equals(inventory.get(i).name, item.name)){
                return i;
            }
        }
        return -1;
    }
}

