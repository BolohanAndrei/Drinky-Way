package Entity;

import Main.GamePanel;

import java.util.Random;

public class DrunkNPC1 extends Entity {

    boolean isIdle = false;
    int idleCounter = 0;
    int idleDuration = 120;

    public DrunkNPC1(GamePanel gp) {
        super(gp);
        direction = "up";
        speed = 1;

       getDrunkNPCImage();
       setDialogue();
    }

    public void getDrunkNPCImage() {
        try {
            up1 = setup("NPCsprite/up_1");
            up2 = setup("NPCsprite/up_2");
            down1 = setup("NPCsprite/down_1");
            down2 = setup("NPCsprite/down_2");
            left1 = setup("NPCsprite/left_1");
            left2 = setup("NPCsprite/left_2");
            right1 = setup("NPCsprite/right_1");
            right2 = setup("NPCsprite/right_2");
            idle_1 = setup("NPCsprite/left_1");
            idle_2 = setup("NPCsprite/left_2");
            idle_3 = setup("NPCsprite/right_1");
            idle_4 = setup("NPCsprite/right_2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDialogue() {
        dialogue[0] = "Ahoy, Matey!";
        dialogue[1] = "Did you try this rom?";
        dialogue[2] = "I think I need to sit down...";
        dialogue[3] = "The world is spinning!";
        dialogue[4] = "Where am I again?";
    }

    public void setAction() {
        if (isIdle) {
            idleCounter++;

            // Change idle animation every 15 ticks
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

    public void speak() {
        super.speak();
    }

}
