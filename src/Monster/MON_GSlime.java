package Monster;

import Entity.Entity;
import Main.GamePanel;
import object.*;

import java.util.Random;

public class MON_GSlime extends Entity {


    public MON_GSlime(GamePanel gp) {
        super(gp);
        name="Green Slime";
        entityType=2;
        speed=1;
        maxHealth=4;
        health=maxHealth;
        attack=3;
        defense=0;
        exp=2;
        projectile=new Obj_SlimeProjectile(gp);

        solidArea.x=3;
        solidArea.y=18;
        solidArea.width=42;
        solidArea.height=30;
        solidAreaDefaultX=solidArea.x;
        solidAreaDefaultY=solidArea.y;

        getImage();

    }
    public void getImage(){
        up1=setup("monsters/Slime_1");
        up2=setup("monsters/Slime_2");
        down1=setup("monsters/Slime_1");
        down2=setup("monsters/Slime_2");
        left1=setup("monsters/Slime_1");
        left2=setup("monsters/Slime_2");
        right1=setup("monsters/Slime_1");
        right2=setup("monsters/Slime_2");
    }

    public void setAction() {

        // monster is moving
        actionLockCounter++;
        if (actionLockCounter >= 120) {
            Random rand = new Random();
            int i = rand.nextInt(100) + 1;
            if (i <= 25) {
                direction = "up";
            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else direction = "right";
            actionLockCounter = 0;
        }
        int i=new Random().nextInt(100)+1;
        if(i>99 && !projectile.alive && shotAvailableCounter>=30){
            projectile.set(x,y,direction,true,this);
            gp.projectiles.add(projectile);
            shotAvailableCounter=0;
        }

    }

    public void damageReaction(){
        actionLockCounter=0;
        direction=gp.player.direction;
    }

    @Override
    public void checkDrop() {
        int roll = new java.util.Random().nextInt(100); // 0..99

        if (roll <= 20) {                           // 0-20 coin
            dropItem(new Obj_Coin(gp));
        } else if (roll <= 40) {                    // 21-40 heal potion
            dropItem(new Obj_Heal_Potion(gp));      // <-- ensure class exists
        } else if (roll <= 50) {                    // 41-50 cup
            dropItem(new Obj_Cup(gp));
        } else if (roll <= 52) {                    // 51-52 amber
            dropItem(new Obj_Amber(gp));
        } else if (roll <= 54) {                    // 53-54 amethyst
            dropItem(new Obj_Amethyst(gp));
        } else if (roll <= 56) {                    // 55-56 diamond
            dropItem(new Obj_Diamond(gp));
        } else if (roll <= 58) {                    // 57-58 emerald
            dropItem(new Obj_Emerald(gp));
        } else if (roll <= 60) {                    // 59-60 quartz
            dropItem(new Obj_Quartz(gp));
        } else if (roll <= 62) {                    // 61-62 ruby
            dropItem(new Obj_Ruby(gp));
        } else if (roll <= 64) {                    // 63-64 sapphire
            dropItem(new Obj_Sapphire(gp));
        } else if (roll <= 70) {                    // 65-70 beer
            dropItem(new Obj_Beer(gp));
        } else if (roll <= 75) {                    // 71-75 cigarette
            dropItem(new Obj_Cigarette(gp));
        } else if (roll <= 78) {                    // 76-78 rum
            dropItem(new Obj_Rum(gp));
        } else if (roll <= 81) {                    // 79-81 drugs
            dropItem(new Obj_Drugs(gp));
        } else if (roll <= 84) {                    // 82-84 tequila
            dropItem(new Obj_Tequila(gp));
        } else if (roll <= 87) {                    // 85-87 whiskey
            dropItem(new Obj_Whiskey(gp));
        } else if (roll <= 90) {                    // 88-90 hook
            dropItem(new Obj_Hook(gp));
        } else if (roll <= 93) {                    // 91-93 helmet
            dropItem(new Obj_Armour_Helmet_Crusty(gp));
        } else if (roll <= 96) {                    // 94-96 chest
            dropItem(new Obj_Armour_Chest_Crusty(gp));
        } else {                                    // 97-99 boots
            dropItem(new Obj_Armour_Boots_Crusty(gp));
        }
    }
}
