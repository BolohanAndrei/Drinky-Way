package Entity;

import Main.GamePanel;

import java.util.Random;

public class DrunkNPC extends Entity {
    boolean isIdle = false;
    int idleCounter = 0;
    int idleDuration = 120;

    public DrunkNPC(GamePanel gp) {
        super(gp);
        direction = "right";
        speed = 1;

        getDrunkNPCImage();
    }

    public void getDrunkNPCImage() {
        try {
            up1 = setup("NPC/drunk_up_1");
            up2 = setup("NPC/drunk_up_2");
            down1 = setup("NPC/drunk_down_1");
            down2 = setup("NPC/drunk_down_2");
            left1 = setup("NPC/drunk_left_1");
            left2 = setup("NPC/drunk_left_2");
            right1 = setup("NPC/drunk_right_1");
            right2 = setup("NPC/drunk_right_2");
            idle_1 = setup("NPC/drunk_idle_1");
            idle_2 = setup("NPC/drunk_idle_2");
            idle_3 = setup("NPC/drunk_idle_3");
            idle_4 = setup("NPC/drunk_idle_4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAction() {
        if (isIdle) {
            idleCounter++;

            // Change idle animation every 15 ticks
            if (idleCounter % 30 == 0) {
                spriteNum = (spriteNum % 4) + 1; // cycle 1 → 2 → 3 → 4 → 1 ...
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

            if (rand.nextInt(100) < 30) { // 30% chance to idle
                isIdle = true;
                spriteNum = 1;
                idleCounter = 0; // reset idle counter
            }

            actionLockCounter = 0;
        }
    }

}

