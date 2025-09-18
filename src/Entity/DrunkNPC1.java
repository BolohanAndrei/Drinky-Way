package Entity;

import Main.GamePanel;

public class DrunkNPC1 extends MovementNPC{
    GamePanel gp;
    private int facePlayerTimer = 0;
    private boolean shouldFacePlayer = false;

    public DrunkNPC1(GamePanel gp) {
        super(gp);
        this.gp = gp;
        direction = "up";
        speed = 1;
        entityType = 1;
        dialogueSet = -1;

       getDrunkNPCImage();
       setDialogue();
    }

    public void getDrunkNPCImage() {
        try {
            up1 = setup("NPC/up_1");
            up2 = setup("NPC/up_2");
            down1 = setup("NPC/down_1");
            down2 = setup("NPC/down_2");
            left1 = setup("NPC/left_1");
            left2 = setup("NPC/left_2");
            right1 = setup("NPC/right_1");
            right2 = setup("NPC/right_2");

            upLeft1 = up1; upLeft2 = left2;
            upRight1 = right1; upRight2 = right2;
            downLeft1 = left1; downLeft2 = left2;
            downRight1 = right1; downRight2 = right2;

            idle_1 = setup("NPC/left_1");
            idle_2 = setup("NPC/left_2");
            idle_3 = setup("NPC/right_1");
            idle_4 = setup("NPC/right_2");
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }

    public void setDialogue() {
        dialogue[0][0] = "Ahoy, Matey!";
        dialogue[0][1] = "The world is spinning!";
        dialogue[0][2] = "I think I need to sit down...";
        dialogue[1][0] = "Come after me matey";
    }

    public void setAction() {
        if (shouldFacePlayer) {
            facePlayerTimer++;
            if (facePlayerTimer >= 60) {
                shouldFacePlayer = false;
                facePlayerTimer = 0;
                onPath = true;
            }
        } else {
            super.setAction();
        }
    }

    public void speak() {
        facePlayer();
        startDialogue(this, dialogueSet);
        dialogueSet++;

        if(dialogueSet >= 0 && dialogueSet < 2){
            shouldFacePlayer = true;
            facePlayerTimer = 0;
            onPath = false;
        }

        if(dialogue[dialogueSet][0] == null){
            dialogueSet--;
        }

    }
}
