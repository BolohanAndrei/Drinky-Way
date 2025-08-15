package Monster;

import Entity.Entity;
import Main.GamePanel;

import java.util.Random;

public class MON_GSlime extends Entity {


    public MON_GSlime(GamePanel gp) {
        super(gp);
        name="Green Slime";
        type=2;
        speed=1;
        maxHealth=4;
        health=maxHealth;

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

    public void setAction(){

    // monster is moving
    actionLockCounter++;
    if(actionLockCounter==120) {
        Random rand = new Random();
        int i = rand.nextInt(100) + 1;
        if (i <= 25) {direction = "up";actionLockCounter=0;}
        else if (i <= 50){ direction = "down";actionLockCounter=0;}
        else if (i <= 75){ direction = "left";actionLockCounter=0;}
        else direction = "right";actionLockCounter=0;
    }
    }

    public void damageReaction(){
        actionLockCounter=0;
        direction=gp.player.direction;
    }
}
