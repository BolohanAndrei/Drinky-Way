package Entity;

import Main.GamePanel;

import java.util.Random;

public class MovementNPC extends Entity {

    public MovementNPC(GamePanel gp) {
        super(gp);
    }

    @Override
    public void setAction(){
        move();
    }
    public void move(){
        if (isIdle) {
            idleCounter++;

            if (idleCounter % 30 == 0) {
                spriteNum = (spriteNum % 4) + 1;
                switch (spriteNum) {
                    case 1: direction="idle_1"; break;
                    case 2: direction="idle_2"; break;
                    case 3: direction="idle_3"; break;
                    case 4: direction="idle_4"; break;
                }
            }

            if (idleCounter >= idleDuration) {
                isIdle = false;
                idleCounter = 0;
            }
            return;
        }

        // NPC is moving
        actionLockCounter++;
        if (actionLockCounter == 120) {
            Random rand = new Random();
            int i = rand.nextInt(100) + 1;
            if (i <= 25) direction = "up";
            else if (i <= 50) direction = "down";
            else if (i <= 75) direction = "left";
            else direction = "right";

            if (rand.nextInt(100) < 30) {
                isIdle = true;
                spriteNum = 1;
                idleCounter = 0;
            }

            actionLockCounter = 0;
        }
    }
}
