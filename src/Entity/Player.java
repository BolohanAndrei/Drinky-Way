package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    public double currentSpeed = 0;

    private String moveDirection = "down";
    private String facingDirection = "down";

    private boolean hasHit = false;
    private int attackCounter = 0;

    private final int meleeReach;
    private final int meleeSideInflate;


    public int maxDrunk;
    public int drunk;
    public int drinkPercent;
    public final int maxDrinkPercent=100;

    public ArrayList<Entity> inventory=new ArrayList<>();
    public final int maxInventorySize=20;

    private int baseStrength;
    private int baseDexterity;

    public AffineTransform drunkOriginalTx;

    public Player(GamePanel gp, KeyHandler kh) {
        super(gp);
        this.keyHandler = kh;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        meleeReach= (int) (gp.tileSize*1.5);
        meleeSideInflate=gp.tileSize/2;
        solidArea = new Rectangle(8, 16, 16, 16);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {
        x = gp.tileSize * 23;
        y = gp.tileSize * 23;

        speed = 4;
        moveDirection = "down";
        facingDirection = "down";

        maxHealth = 6;
        health = maxHealth;

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
        coin=0;

        currentWeapon=new Obj_Wooden_Sword(gp);
        currentShield=new Obj_Shield(gp);
        currentHelmet=new Obj_Armour_Helmet_Crusty(gp);
        currentChest=new Obj_Armour_Chest_Crusty(gp);
        currentBoots=new Obj_Armour_Boots_Crusty(gp);
        projectile=new Obj_Dagger(gp);
        reStats();

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
            inventory.remove(drink);
            gp.sound.playSE(20);
            gp.ui.addMessage("Alcohol +" + drink.alcohol + "% (" + drinkPercent + "%)");
        }
    }

    public void setItems(){
        inventory.add(currentWeapon); //wooden Sword
        inventory.add(currentShield); //wooden Shield
        inventory.add(currentChest);
        inventory.add(currentBoots);
        inventory.add(currentHelmet);
        inventory.add(new Obj_Rum(gp));
        inventory.add(new Obj_Tequila(gp));
        inventory.add(new Obj_Whiskey(gp));
        inventory.add(new Obj_Beer(gp));
        inventory.add(new Obj_Drugs(gp));
        inventory.add(new Obj_Cigarette(gp));
    }

    public void getPlayerImage() {
        try {
            up1 = setup("player/pirate_up_1");
            up2 = setup("player/pirate_up_2");
            down1 = setup("player/pirate_down_1");
            down2 = setup("player/pirate_down_2");
            left1 = setup("player/pirate_left_1");
            left2 = setup("player/pirate_left_2");
            right1 = setup("player/pirate_right_1");
            right2 = setup("player/pirate_right_2");

            idle_up = setup("player/pirate_up_idle");
            idle_down = setup("player/pirate_down_idle");
            idle_left = setup("player/pirate_left_idle");
            idle_right = setup("player/pirate_right_idle");

            die1 = setup("player/die1");
            die2 = setup("player/die2");
            die3 = setup("player/die3");
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }

    public void getPlayerAttackImage() {
        try {
            attackUp1 = setup("player/attack_up_1");
            attackUp2 = setup("player/attack_up_2");
            attackUDown1 = setup("player/attack_down_1");
            attackDown2 = setup("player/attack_down_2");
            attackLeft1 = setup("player/attack_left_1");
            attackLeft2 = setup("player/attack_left_2");
            attackRight1 = setup("player/attack_right_1");
            attackRight2 = setup("player/attack_right_2");
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }

    public void update() {
        double dx = 0, dy = 0;
        boolean moved = false;

        if (!attacking && keyHandler.attackClicked) {
            attacking = true;
            keyHandler.attackClicked = false;
            attackCounter = 0;
            hasHit = false;
            gp.sound.playSE(17);
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

            if(moved) {
                double acceleration = 0.2;
                double maxSpeed = 4.0;
                currentSpeed = Math.min(currentSpeed + acceleration, maxSpeed);
            } else {
                currentSpeed = 0;
            }


        double[] wobble={dx,dy};
        gp.drinkSystem.distortInput(this,wobble);
        dx=wobble[0];
        dy=wobble[1];
        // Collision checks
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

        // NPC collision check for Y movement
        if (dy != 0 && canMoveY) {
            double tempY = y;
            y = (int) futureY;
            direction = dy > 0 ? "down" : "up";
            int npcIndexY = gp.collisionCheck.checkEntity(this, gp.npc);
            if (npcIndexY != 999) canMoveY = false;
            y = (int) tempY;
        }

        // Restore actual movement direction
        direction = moveDirection;

        // Object collision
        int objIndex = gp.collisionCheck.checkObj(this, true);
        pickUpObj(objIndex);

        // NPC interaction
        collisionOn = false;
        gp.collisionCheck.checkEntity(this, gp.npc);
        interactNPC();

        // Monster collision
        int monsterIndex = gp.collisionCheck.checkEntity(this, gp.monster);
        contactMonster(monsterIndex);

        // Check events
        gp.eventHandler.checkEvent();


        gp.keyHandler.ePressed=false;



        // Apply movement if allowed
        if (canMoveX) x += (int) (dx * currentSpeed);
        if (canMoveY) y += (int) (dy * currentSpeed);

        // Death check
        if (gp.deadCheck.check(this)) {
            dead = true;
            dieFrame = 0;
        }

        // Death animation
        if (dead) {
            if (dieFrame == 0) up1 = die1;
            else if (dieFrame == 1) up1 = die2;
            else if (dieFrame == 2) up1 = die3;
            dieFrame++;
            if (dieFrame > 2) dead = false;
        }

        // Animation updates
        if (!attacking) {
            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }

        // Attack handling
        if (attacking) attacking();

        if(gp.keyHandler.shotKeyPressed && !projectile.alive && shotAvailableCounter==60){
            projectile.set(x,y,facingDirection,true,this);
            gp.projectiles.add(projectile);

            shotAvailableCounter=0;

            gp.sound.playSE(21);
        }

        // Invincibility handling
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if(shotAvailableCounter<60){
            shotAvailableCounter++;
        }
        updateDrunkFromPercent();
    }

    //Attack animation
    public void attacking() {
        attackCounter++;

        if (attackCounter <= 5) {
            spriteNum = 1;
        } else if (attackCounter <= 25) {
            spriteNum = 2;
            if (attackCounter == 15 && !hasHit) {
                checkAttackHit();
                hasHit = true;
            }
        } else {
            spriteNum = 1;
            attacking = false;
            attackCounter = 0;
            hasHit = false;
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

    //Damage Monster
    public void checkAttackHit() {

        Rectangle area = buildAttackArea();
        checkAttackHit(area,attack,false);
    }

    public void checkAttackHit(Rectangle area,int damageValue, boolean fromProjectile){
        for (int i = 0; i < gp.monster.length; i++) {
            Entity mon = gp.monster[i];
            if (mon == null || mon.dying || !mon.alive) continue;

            Rectangle monBox = new Rectangle(mon.x + mon.solidArea.x,
                    mon.y + mon.solidArea.y,
                    mon.solidArea.width,
                    mon.solidArea.height);
            if (!area.intersects(monBox)) continue;

            gp.sound.playSE(16);

            int damage;
            damage=damageValue-mon.defense;
            if(damage<0){
                damage=0;
            }
            mon.health -= damage;

            mon.hpBarOn = true;
            mon.hpBarCounter = 0;
            mon.invincible = true;
            mon.invincibleCounter = 0;
            mon.damageReaction();

            if (mon.health <= 0) {
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

    public void pickUpObj(int i) {
        if (i != 999) {
            if(!gp.obj[i].pickable) {return;}
                String text;
                if (inventory.size() != maxInventorySize) {
                    inventory.add(gp.obj[i]);
                    gp.sound.playSE(2);
                    text = "Picked up " + gp.obj[i].name;

                } else {
                    text = "Inventory Full";
                }
                gp.ui.addMessage(text);
                gp.obj[i] = null;

        }
    }

    public void interactNPC() {
        if (gp.keyHandler.ePressed) {
            int nearestNPC = findNearestNPC();
            if (nearestNPC != 999) {
                gp.gameState = gp.dialogueState;
                gp.npc[nearestNPC].speak();
            }
        }
    }

    private int findNearestNPC() {
        int nearestIndex = 999;
        double shortestDistance = Double.MAX_VALUE;
        int interactionRange = gp.tileSize *2;

        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] != null) {
                double playerCenterX = x + gp.tileSize / 2.0;
                double playerCenterY = y + gp.tileSize / 2.0;
                double npcCenterX = gp.npc[i].x + gp.tileSize / 2.0;
                double npcCenterY = gp.npc[i].y + gp.tileSize / 2.0;

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
        if (i != 999 && !invincible && !gp.monster[i].dying) {
            gp.sound.playSE(18);
            int damage=gp.monster[i].attack-defense;
            if(damage<0){
                damage=1;
            }
            health -= damage;
            if(health<=0){
                health=0;
            }
            invincible = true;
            damageReaction();
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;

        if (dead) {
            image = switch (dieFrame) {
                case 0 -> die1;
                case 1 -> die2;
                default -> die3;
            };
            g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            return;
        }

        if (attacking) {
            switch (facingDirection) {
                case "up", "up_left", "up_right" -> image = (spriteNum == 1) ? attackUp1 : attackUp2;
                case "down", "down_left", "down_right" -> image = (spriteNum == 1) ? attackUDown1 : attackDown2;
                case "left" -> image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                case "right" -> image = (spriteNum == 1) ? attackRight1 : attackRight2;
            }
        } else {
            switch (moveDirection) {
                case "up"-> image = (spriteNum == 1) ? up1 : up2;
                case "down" -> image = (spriteNum == 1) ? down1 : down2;
                case "left" ,"up_left","down_left" -> image = (spriteNum == 1) ? left1 : left2;
                case "right","up_right","down_right" -> image = (spriteNum == 1) ? right1 : right2;
            }

            if (currentSpeed == 0) {
                image = switch (facingDirection) {
                    case "up", "up_left", "up_right" -> idle_up;
                    case "down", "down_left", "down_right" -> idle_down;
                    case "left" -> idle_left;
                    case "right" -> idle_right;
                    default -> image;
                };
            }
        }

        Composite originalComposite = g2d.getComposite();
        if(invincible) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        g2d.setComposite(originalComposite);
    }

    public void damageReaction() {
        int knock = gp.tileSize / 4;
        switch (facingDirection) {
            case "up","up_left","up_right" -> y += knock;
            case "down","down_left","down_right" -> y -= knock;
            case "left" -> x += knock;
            case "right" -> x -= knock;
        }
    }

    public void checkLevelUp(){
        if(exp>=nextLevelExp){
            gp.sound.playSE(11);
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
            gp.gameState=gp.dialogueState;
            gp.ui.currentDialogue="Ahoy, you are at another level,"+level+"!\nStats improved";
        }
    }

    private boolean isEquipped(Entity item){
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
        if(item.gearType==0){ // weapon
            currentWeapon=item;
        } else if(item.gearType==1){ // shield
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
        int itemIndex=gp.ui.getItemIndexSlot();
        if(itemIndex>=inventory.size()) return;

        Entity selectedItem=inventory.get(itemIndex);

        // Consumables (gearType 2) -> use & remove
        if(selectedItem.gearType==2){
            if(selectedItem.alcohol>0){
                consumeDrunk(selectedItem);
            } else {
                selectedItem.use(this);
                inventory.remove(selectedItem);
                gp.ui.addMessage(selectedItem.name+" used");
            }
            return;
        }

        // Equipment toggle
        if(isEquipment(selectedItem)){
            if(isEquipped(selectedItem)){
                unequipItem(selectedItem);
            } else {
                // Equip (replacing old item in that slot automatically)
                equipItem(selectedItem);
            }
        }
    }


    public BufferedImage getCurrentFrame() {
        if (dead) return die3;
        if (attacking) {
            return switch (facingDirection) {
                case "up", "up_left", "up_right" -> (spriteNum == 1) ? attackUp1 : attackUp2;
                case "down", "down_left", "down_right" -> (spriteNum == 1) ? attackUDown1 : attackDown2;
                case "left" -> (spriteNum == 1) ? attackLeft1 : attackLeft2;
                case "right" -> (spriteNum == 1) ? attackRight1 : attackRight2;
                default -> down1;
            };
        }
        BufferedImage img = switch (moveDirection) {
            case "up" -> (spriteNum == 1) ? up1 : up2;
            case "down" -> (spriteNum == 1) ? down1 : down2;
            case "left", "up_left", "down_left" -> (spriteNum == 1) ? left1 : left2;
            case "right", "up_right", "down_right" -> (spriteNum == 1) ? right1 : right2;
            default -> down1;
        };
        if (currentSpeed == 0) {
            img = switch (facingDirection) {
                case "up", "up_left", "up_right" -> idle_up;
                case "down", "down_left", "down_right" -> idle_down;
                case "left" -> idle_left;
                case "right" -> idle_right;
                default -> img;
            };
        }
        return img;
    }
}