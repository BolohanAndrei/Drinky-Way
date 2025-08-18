package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    private double currentSpeed = 0;
    private final double acceleration = 0.2;
    private final double maxSpeed = 4.0;

    private String moveDirection = "down";
    private String facingDirection = "down";
    private boolean hasHit = false;

    private int attackCounter = 0;


    public int maxDrunk;
    public int drunk;

    public ArrayList<Entity> inventory=new ArrayList<>();
    //public final int maxinventorySize=20;

    public Player(GamePanel gp, KeyHandler kh) {
        super(gp);
        this.keyHandler = kh;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

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

        //Status
        maxHealth = 10;
        health = maxHealth;
        maxDrunk = 6;
        drunk = 0;

        level = 1;
        strength=1;
        dexterity = 1;
        exp=0;
        nextLevelExp=10;
        coin=0;
        currentWeapon=new Obj_Weapon(gp);
        currentShield=new Obj_Shield(gp);
        attack=getAttack();
        defense=getDefense();

    }

    public int getAttack(){
        return attack=strength*currentWeapon.attackValue;
    }

    public int getDefense(){
        return defense=dexterity*currentShield.defenseValue;
    }

    public void setItems(){
        inventory.add(currentWeapon); //wooden Sword
        inventory.add(currentShield); //wooden Shield
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
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
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
            gp.playSE(17);
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
                currentSpeed = Math.min(currentSpeed + acceleration, maxSpeed);
            } else {
                currentSpeed = 0;
            }


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
        int npcIndex = gp.collisionCheck.checkEntity(this, gp.npc);
        interactNPC(npcIndex);

        // Monster collision
        int monsterIndex = gp.collisionCheck.checkEntity(this, gp.monster);
        contactMonster(monsterIndex);

        // Check events
        gp.eventHandler.checkEvent();


        gp.keyHandler.ePressed=false;



        // Apply movement if allowed
        if (canMoveX) x += dx * currentSpeed;
        if (canMoveY) y += dy * currentSpeed;

        // Monster contact


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

        // Invincibility handling
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 120) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
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

    //Damage Monster
    public void checkAttackHit() {
        Rectangle attackArea = new Rectangle(x, y, 36, 36);
        int attackRange = gp.tileSize;

        switch (facingDirection) {
            case "up":
                attackArea.x = x - (attackRange / 2);
                attackArea.y = y - attackRange;
                attackArea.width = gp.tileSize + attackRange;
                attackArea.height = attackRange;
                break;
            case "down":
                attackArea.x = x - (attackRange / 2);
                attackArea.y = y + gp.tileSize;
                attackArea.width = gp.tileSize + attackRange;
                attackArea.height = attackRange;
                break;
            case "left":
                attackArea.x = x - attackRange;
                attackArea.y = y;
                attackArea.width = attackRange;
                attackArea.height = gp.tileSize;
                break;
            case "right":
                attackArea.x = x + gp.tileSize;
                attackArea.y = y;
                attackArea.width = attackRange;
                attackArea.height = gp.tileSize;
                break;
            case "up_left":
                attackArea.x = x - attackRange;
                attackArea.y = y - attackRange;
                attackArea.width = attackRange;
                attackArea.height = attackRange;
                break;
            case "up_right":
                attackArea.x = x + gp.tileSize;
                attackArea.y = y - attackRange;
                attackArea.width = attackRange;
                attackArea.height = attackRange;
                break;
            case "down_left":
                attackArea.x = x - attackRange;
                attackArea.y = y + gp.tileSize;
                attackArea.width = attackRange;
                attackArea.height = attackRange;
                break;
            case "down_right":
                attackArea.x = x + gp.tileSize;
                attackArea.y = y + gp.tileSize;
                attackArea.width = attackRange;
                attackArea.height = attackRange;
                break;
        }

        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] != null) {
                Rectangle monsterArea = new Rectangle(
                        gp.monster[i].x,
                        gp.monster[i].y,
                        gp.tileSize,
                        gp.tileSize
                );

                if (attackArea.intersects(monsterArea)) {
                    gp.playSE(16);
                    Entity mon = gp.monster[i];
                    int damage=attack-mon.defense;
                    if(damage<0){
                        damage=0;
                    }
                    mon.health-=damage;

                    mon.hpBarOn = true;
                    mon.hpBarCounter = 0;
                    mon.invincible = true;
                    mon.invincibleCounter = 0;
                    mon.damageReaction();
                    if (mon.health <= 0) {

                        gp.ui.addMessage(mon.exp+" EXP");
                        exp+=mon.exp;
                        checkLevelUp();
                        mon.dying = true;
                    }
                }
            }
        }
    }

    public void pickUpObj(int i) {
        if (i != 999) {
            // add logic here
        }
    }

    public void interactNPC(int i) {
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
        if (i != 999 && !invincible) {
            gp.playSE(18);
            int damage=gp.monster[i].attack-defense;
            if(damage<0){
                damage=0;
            }
            health -= damage;
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
                case 2 -> die3;
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
            gp.playSE(11);
            level++;
            nextLevelExp=nextLevelExp*2;
            maxHealth+=2;
            strength++;
            dexterity++;
            coin+=exp;
            attack=getAttack();
            defense=getDefense();
            exp=0;
            gp.gameState=gp.dialogueState;
            gp.ui.currentDialogue="Ahoy, you are at another level,"+level+"!\nStats improved";
        }
    }
}