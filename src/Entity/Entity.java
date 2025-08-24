package Entity;

import Main.GamePanel;
import Main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity {

 protected GamePanel gp;

    // ========== 1. Identity & Classification ==========
    public String name;
    public int entityType = -1; // 0 player, 1 npc, 2 monster

    // ========== 2. Position & Movement ==========
    public int x, y;
    public int speed;
    public String direction = "down";
    public int actionLockCounter = 0;
    public boolean attacking = false;

    // ========== 3. Rendering Assets ==========
    // --- 3.1 Movement Sprites ---
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    // --- 3.2 Attack Sprites ---
    public BufferedImage attackUp1, attackUp2, attackUDown1, attackDown2,
            attackLeft1, attackLeft2, attackRight1, attackRight2;

    // --- 3.3 Idle / Death / Extra Sprites ---
    public BufferedImage idle_up, idle_down, idle_left, idle_right;
    public BufferedImage die1, die2, die3;
    public BufferedImage idle_1, idle_2, idle_3, idle_4;
    public BufferedImage image1, image2, image3;

    // ========== 4. Animation State ==========
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // ========== 5. Dialogue System ==========
    String[] dialogue = new String[20];
    public int dialogueIndex = 0;

    // ========== 6. Collision & Spatial Bounds ==========
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean collision = false;

    // ========== 7. Life / Existence State ==========
    public boolean alive = true;
    public boolean dead = false;
    public boolean dying = false;
    public int dieFrame = 0;
    int dyingCounter = 0;

    // ========== 8. Health & Damage Feedback ==========
    public int maxHealth;
    public int health;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public boolean hpBarOn = false;
    public int hpBarCounter = 0;

    // ========== 9. Progression & Core Stats ==========
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public int alcohol;

    // ========== 10. Equipment References ==========
    public Entity currentWeapon;
    public Entity currentShield;
    public Entity currentHelmet;
    public Entity currentChest;
    public Entity currentBoots;

    // ========== 11. Active Projectile ==========
    public Projectile projectile;
    public int shotAvailableCounter=0;

    // ========== 12. Item Metadata & Gear Attributes ==========
    public int attackValue;
    public int defenseValue;
    public int strengthBonus;
    public int dexterityBonus;
    public int attackFlatBonus;
    public int defenseFlatBonus;
    public int useCost;
    public String itemDescription = "";
    public int gearType = -1;   // 0 sword, 1 shield, 2 consumable
    public int armourType = -1; // 0 helmet, 1 chest, 2 boots
    public boolean pickable = true;

    // ========== 13. NPC Idle Behavior ==========
    boolean isIdle = false;
    int idleCounter = 0;
    int idleDuration = 120;
    public Entity(GamePanel gp) {
        this.gp = gp;

    }

    public void setAction(){

    }

    public void damageReaction(){
    }

    public void speak(){
    if(dialogueIndex<0 || dialogueIndex>dialogue.length-1 || dialogue[dialogueIndex]==null){
        dialogueIndex = 0;
    }
        String line=dialogue[dialogueIndex];
    line=gp.drinkSystem.slurIfNeeded(line);
        gp.ui.currentDialogue = line;
        ++dialogueIndex;
        if(dialogueIndex<0 || dialogueIndex>dialogue.length-1 || dialogue[dialogueIndex]==null){
            dialogueIndex = 0;
        }

        facePlayer();

    }
    public void facePlayer(){
        switch(gp.player.direction) {
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
            default: direction = "down"; break;
        }
    }

    public void use(Entity e){}

    public void update(){

        collisionOn = false;
        gp.collisionCheck.checkTile(this);
        gp.collisionCheck.checkObj(this, false);
        gp.collisionCheck.checkEntity(this,gp.npc);
        gp.collisionCheck.checkEntity(this,gp.monster);
       boolean contactPlayer= gp.collisionCheck.checkPlayer(this);

       if(this.entityType==2 && contactPlayer){
           if(!gp.player.invincible){
               gp.sound.playSE(18);

               int damage=attack-gp.player.defense;
               if(damage<0){
                   damage=1;
               }
               gp.player.health-=damage;
               if(gp.player.health<0){
                   gp.player.health=0;
               }
               gp.player.invincible=true;
           }
       }
        setAction();

        if(!collisionOn){
            switch (direction) {
                case "up": y -= speed; break;
                case "down": y += speed; break;
                case "left": x -= speed; break;
                case "right": x += speed; break;
            }
        }
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        // Added: handle invincibility countdown for non-player entities
        if(entityType != 0 && invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){ // 2 seconds at 60 FPS
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    private BufferedImage fallbackImage() {
        if (down1 != null) return down1;
        if (idle_down != null) return idle_down;
        if (up1 != null) return up1;
        if (left1 != null) return left1;
        if (right1 != null) return right1;
        if (idle_1 != null) return idle_1;
        return new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
    }

    public void draw(Graphics2D g) {
        int screenX = x - gp.player.x + gp.player.screenX;
        int screenY = y - gp.player.y + gp.player.screenY;

        if (((x + gp.tileSize) > (gp.player.x - gp.player.screenX)) &&
                ((x - gp.tileSize) < (gp.player.x + gp.player.screenX)) &&
                ((y + gp.tileSize) > (gp.player.y - gp.player.screenY)) &&
                ((y - gp.tileSize) < (gp.player.y + gp.player.screenY))) {
            BufferedImage image = fallbackImage();

            image = switch (direction) {
                case "up" -> (spriteNum == 1) ? up1 : up2;
                case "up_left", "left", "down_left" -> (spriteNum == 1) ? left1 : left2;
                case "up_right", "right", "down_right" -> (spriteNum == 1) ? right1 : right2;
                case "down" -> (spriteNum == 1) ? down1 : down2;
                case "idle_up", "idle_up_left", "idle_up_right" -> idle_up;
                case "idle_down", "idle_down_left", "idle_down_right" -> idle_down;
                case "idle_left" -> idle_left;
                case "idle_right" -> idle_right;
                case "idle_1" -> idle_1;
                case "idle_2" -> idle_2;
                case "idle_3" -> idle_3;
                case "idle_4" -> idle_4;
                default -> image;
            };

            if(invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
            }

            //Monster HP bar
            if (entityType == 2 && hpBarOn) {
                double oneScale=(double)gp.tileSize/maxHealth;
                double hpBarValue=oneScale*health;

                g.setColor(new Color(35,35,35));
                g.fillRect(screenX-1,screenY-16,gp.tileSize+2,12);
                g.setColor(new Color(213, 0, 23, 255));
                g.fillRect(screenX,screenY-15,(int)hpBarValue,10);

                hpBarCounter++;
                if(hpBarCounter>600){
                    hpBarCounter=0;
                    hpBarOn=false;
                }
            }

            Composite originalComposite = g.getComposite();
            if(invincible){
                hpBarOn=true;
                hpBarCounter=0;
                changeAlpha(g,0.5f);
            } else if (!dying) {
                // Reset alpha if not invincible or dying
                changeAlpha(g, 1f);
            }
            if(dying){
                dyingAnimation(g);
            }

            g.drawImage(image, screenX, screenY,gp.tileSize, gp.tileSize, null);

            //reset opacity
            g.setComposite(originalComposite);

        }
    }

    public void dyingAnimation(Graphics2D g) {
        dyingCounter++;
        int i=5;
        if (dyingCounter <= i) {
            changeAlpha(g, 0f);
        }
        if (dyingCounter > i && dyingCounter <= i*2) {
            changeAlpha(g, 1f);
        }
        if (dyingCounter > i*2 && dyingCounter <= i*3) {
            changeAlpha(g, 0f);
        }
        if (dyingCounter > i*3 && dyingCounter <= i*4) {
            changeAlpha(g, 1f);
        }
        if (dyingCounter > i*4 && dyingCounter <= i*5) {
            changeAlpha(g, 0f);
        }
        if (dyingCounter > i*5 && dyingCounter <= i*6) {
            changeAlpha(g, 1f);
        }
        if (dyingCounter > i*6 && dyingCounter <= i*7) {
            changeAlpha(g, 0f);
        }
        if (dyingCounter > i*7 && dyingCounter <= i*8) {
            changeAlpha(g,1f);
        }
        if (dyingCounter > i*8) {
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g,float alpha){
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    public BufferedImage setup(String name) {
        Utility u = new Utility();
        BufferedImage scale = null;
        try {
            scale = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/" + name + ".png")));
            scale = u.scaleImage(scale, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.getStackTrace();
        }
        return scale;
    }
}
