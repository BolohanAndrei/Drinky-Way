package Entity;

import Main.GamePanel;
import Main.UI;
import Main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

 protected GamePanel gp;

    // Position and movement
    public int x, y;
    public int speed;
    public int actionLockCounter = 0;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, idle_up, idle_down, idle_left, idle_right, die1, die2, die3, idle_1, idle_2, idle_3, idle_4;
    public BufferedImage attackUp1,attackUp2,attackUDown1,attackDown2,attackLeft1,attackLeft2,attackRight1,attackRight2;
    public String direction="down";
    public boolean attacking=false;

    // Dialogue system
    String[] dialogue = new String[20];
    public int dialogueIndex = 0;

    // Animation control
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // Collision detection
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    //Dead state
    public boolean dead = false;
    public int dieFrame = 0;
   public boolean alive=true;
   public boolean dying=false;

    //Character status
    public int maxHealth;
    public int health;
    public boolean invincible=false;
    public int invincibleCounter=0;
    int dyingCounter=0;
    public boolean hpBarOn=false;
    public int hpBarCounter=0;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public int alcohol;
    public Entity currentWeapon;
    public Entity currentShield;
    public Entity currentHelmet;
    public Entity currentChest;
    public Entity currentBoots;
    public Entity currentDrink;

    //item status
    public int attackValue;
    public int defenseValue;
    public String itemDescription="";



    //NPC
    public BufferedImage image1, image2, image3, image4;
    public String name;
    public boolean collision = false;
    public int type; //0 player 1 npc 2 monster

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
        gp.ui.currentDialogue = dialogue[dialogueIndex];
        ++dialogueIndex;
        if(dialogueIndex<0 || dialogueIndex>dialogue.length-1 || dialogue[dialogueIndex]==null){
            dialogueIndex = 0;
        }

        facePlayer();

    }
    public void facePlayer(){
        switch(gp.player.direction) {
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
            default: direction = "down"; break;
        }
    }

    public boolean isPlayerInRange(){
        int distance=gp.tileSize;

        int entityCenterX=x+gp.tileSize/2;
        int entityCenterY=y+gp.tileSize/2;
        int playerCenterX=gp.player.x+gp.tileSize/2;
        int playerCenterY=gp.player.y+gp.tileSize/2;

        double distX=Math.abs(entityCenterX-playerCenterX);
        double distY=Math.abs(entityCenterY-playerCenterY);

        return distX<=distance&&distY<=distance;

    }

    public void update(){

        collisionOn = false;
        gp.collisionCheck.checkTile(this);
        gp.collisionCheck.checkObj(this, false);
        gp.collisionCheck.checkEntity(this,gp.npc);
        gp.collisionCheck.checkEntity(this,gp.monster);
       boolean contactPlayer= gp.collisionCheck.checkPlayer(this);

       if(this.type==2 && contactPlayer){
           if(!gp.player.invincible){
               gp.playSE(18);

               int damage=attack-gp.player.defense;
               if(damage<0){
                   damage=0;
               }
               gp.player.health-=damage;
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
    }

    public void draw(Graphics2D g) {
        BufferedImage image = null;
        int screenX = x - gp.player.x + gp.player.screenX;
        int screenY = y - gp.player.y + gp.player.screenY;

        if (x + gp.tileSize > gp.player.x - gp.player.screenX &&
                x - gp.tileSize < gp.player.x + gp.player.screenX &&
                y + gp.tileSize > gp.player.y - gp.player.screenY &&
                y - gp.tileSize < gp.player.y + gp.player.screenY) {

            switch (direction) {
                case "up": image = (spriteNum == 1) ? up1 : up2; break;
                case "up_left": case "left": case "down_left": image = (spriteNum == 1) ? left1 : left2; break;
                case "up_right": case "right": case "down_right": image = (spriteNum == 1) ? right1 : right2; break;
                case "down": image = (spriteNum == 1) ? down1 : down2; break;
                case "idle_up": case "idle_up_left": case "idle_up_right": image = idle_up; break;
                case "idle_down": case "idle_down_left": case "idle_down_right": image = idle_down; break;
                case "idle_left": image = idle_left; break;
                case "idle_right": image = idle_right; break;
                case "idle_1": image = idle_1; break;
                case "idle_2": image = idle_2; break;
                case "idle_3": image = idle_3; break;
                case "idle_4": image = idle_4; break;
            }

            if(invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
            }

            //Monster HP bar
            if (type == 2 && hpBarOn) {
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
            }
            if(dying){
                dyingAnimation(g);
            }

            g.drawImage(image, screenX, screenY,gp.tileSize, gp.tileSize, null);

            //reset opacity
            g.setComposite(originalComposite);

//            if(this!=gp.player && type==1 && isPlayerInRange()){
//                drawInteractionPrompt(g,screenX,screenY);
//            }
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
            dying = false;
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g,float alpha){
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    private void drawInteractionPrompt(Graphics2D g, int screenX, int screenY){
        Font originalFont = g.getFont();
        Color originalColor = g.getColor();

        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.WHITE);

        String prompt="Press ENTER to talk";
        int width = g.getFontMetrics().stringWidth(prompt);
        g.setColor(new Color(0, 0, 0, 180)); // Semi-transparent black
        g.fillRoundRect(x + (gp.tileSize/2) - (width/2) - 5, y - 25, width + 10, 20, 5, 5);

        g.setFont(originalFont);
        g.setColor(originalColor);

    }

    public BufferedImage setup(String name) {
        Utility u = new Utility();
        BufferedImage scale = null;
        try {
            scale = ImageIO.read(getClass().getResourceAsStream("/res/" + name + ".png"));
            scale = u.scaleImage(scale, gp.tileSize, gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scale;
    }
}
