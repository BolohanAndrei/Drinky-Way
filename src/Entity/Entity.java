package Entity;

import Main.GamePanel;
import Main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

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
    public String itemDescription = "";
    public int gearType = -1;   // 0 sword, 1 shield, 2 consumable 3 pickUp(goods)
    public int armourType = -1; // 0 helmet, 1 chest, 2 boots
    public boolean pickable = true;
    public int value;

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
    public void checkDrop(){}
    public void dropItem(Entity droppedItem){
        for(int i=0;i<gp.obj.length;i++){
            if(gp.obj[i]==null){
                gp.obj[i]=droppedItem;
                gp.obj[i].x=x;
                gp.obj[i].y=y;
                break;
            }
        }
    }

    public void update(){

        collisionOn = false;
        gp.collisionCheck.checkTile(this);
        gp.collisionCheck.checkObj(this, false);
        gp.collisionCheck.checkEntity(this,gp.npc);
        gp.collisionCheck.checkEntity(this,gp.monster);
        gp.collisionCheck.checkEntity(this,gp.iTile);
       boolean contactPlayer= gp.collisionCheck.checkPlayer(this);

       if(this.entityType==2 && contactPlayer){
           damagePlayer(attack);
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
        if(entityType != 0 && invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){ // 2 seconds at 60 FPS
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if(shotAvailableCounter<60){
            shotAvailableCounter++;
        }
    }

    public void damagePlayer(int attack){
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

    public Color getParticleColor(){
        return null;
    }

    public int getParticleSize(){
        return 0;
    }

    public int getParticleSpeed()
    {
        return 0;
    }

    public int getParticleMaxHealth(){
        return 0;
    }

    public void generateParticle(Entity generator, Entity target) {
        Color base = generator.getParticleColor();
        int baseSize = generator.getParticleSize();
        int baseLife = generator.getParticleMaxHealth();
        int baseSpeed = generator.getParticleSpeed();

        Random rnd = new Random();

        double outwardAngle;
        if (target != null) {
            double dx = (target.x + target.solidArea.x + target.solidArea.width / 2.0)
                    - (generator.x + generator.solidArea.x + generator.solidArea.width / 2.0);
            double dy = (target.y + target.solidArea.y + target.solidArea.height / 2.0)
                    - (generator.y + generator.solidArea.y + generator.solidArea.height / 2.0);
            outwardAngle = Math.atan2(dy, dx);
        } else {
            outwardAngle = rnd.nextDouble() * Math.PI * 2;
        }

        int dustCount = 10 + rnd.nextInt(8);
        int chunkCount = 3 + rnd.nextInt(3);

        java.util.function.Function<Color, Color> varyColor = (c) -> {
            float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
            float sat = Math.min(1f, Math.max(0f, hsb[1] * (0.85f + rnd.nextFloat() * 0.3f)));
            float bri = Math.min(1f, Math.max(0f, hsb[2] * (0.80f + rnd.nextFloat() * 0.4f)));
            int rgb = Color.HSBtoRGB(hsb[0], sat, bri);
            return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        };

        for (int i = 0; i < dustCount; i++) {
            double angleSpread = outwardAngle + (rnd.nextDouble() - 0.5) * Math.toRadians(160);
            int vx = (int) Math.round(Math.cos(angleSpread) * (1 + rnd.nextInt(3)));
            int vy = (int) Math.round(Math.sin(angleSpread) * (1 + rnd.nextInt(3)));
            vy -= rnd.nextInt(2);
            if (vx == 0 && vy == 0) vx = 1;

            int size = Math.max(2, baseSize - 2 + rnd.nextInt(3));
            int speed = Math.max(1, baseSpeed + rnd.nextInt(2));
            int life = Math.max(8, baseLife - 5 + rnd.nextInt(6));
            Color c = varyColor.apply(base);

            gp.particles.add(new Particle(gp, generator, c, vx, vy, size, speed, life));
        }

        for (int i = 0; i < chunkCount; i++) {
            double angleSpread = outwardAngle + (rnd.nextDouble() - 0.5) * Math.toRadians(90);
            int mag = 2 + rnd.nextInt(3);
            int vx = (int) Math.round(Math.cos(angleSpread) * mag);
            int vy = (int) Math.round(Math.sin(angleSpread) * mag) - 1;
            if (vx == 0 && vy == 0) vy = -1;

            int size = baseSize + rnd.nextInt(3);
            int speed = Math.max(1, baseSpeed);
            int life = baseLife + 5 + rnd.nextInt(8);
            Color c = varyColor.apply(base);

            gp.particles.add(new Particle(gp, generator, c, vx, vy, size, speed, life));
        }

        if (rnd.nextInt(100) < 15) {
            double sparkleAngle = outwardAngle + (rnd.nextDouble() - 0.5) * Math.toRadians(45);
            int vx = (int) Math.round(Math.cos(sparkleAngle) * 3);
            int vy = (int) Math.round(Math.sin(sparkleAngle) * 3) - 1;
            Color sparkle = new Color(
                    Math.min(255, base.getRed() + 40),
                    Math.min(255, base.getGreen() + 40),
                    Math.min(255, base.getBlue() + 40)
            );
            gp.particles.add(new Particle(gp, generator, sparkle, vx, vy, Math.max(2, baseSize - 1), baseSpeed + 1, baseLife));
        }
    }
}
