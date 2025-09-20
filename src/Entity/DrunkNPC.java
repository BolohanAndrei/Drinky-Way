package Entity;

import Main.GamePanel;

public class DrunkNPC extends MovementNPC {

    public DrunkNPC(GamePanel gp) {
        super(gp);
        direction = "right";
        speed = 1;
        entityType = 1;

        dialogueSet=-1;

        getDrunkNPCImage();
        setDialogue();
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

            upLeft1 = up1; upLeft2 = left2;
            upRight1 = right1; upRight2 = right2;
            downLeft1 = left1; downLeft2 = left2;
            downRight1 = right1; downRight2 = right2;

            idle_1 = setup("NPC/drunk_idle_1");
            idle_2 = setup("NPC/drunk_idle_2");
            idle_3 = setup("NPC/drunk_idle_3");
            idle_4 = setup("NPC/drunk_idle_4");
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }

    public void setDialogue() {
        dialogue[0][0] = "Ahoy, Captain!";
        dialogue[0][1] = "Did you try this magic potion?";
        dialogue[0][2] = "Where am I again?";

        dialogue[1][0] = "Whoooo...are you?";
        dialogue[1][1] = "The world is spinning!";
        dialogue[1][2] = "Let's do this";

        dialogue[2][0] = "Where are you going?";
        dialogue[2][1] = "I'm going to drink!";
        dialogue[2][2] = "Where are you coming?";
    }

    public void setAction() {
        super.setAction();
    }

    public void speak(){
        facePlayer();
        startDialogue(this,dialogueSet);
        dialogueSet++;
        if(dialogue[dialogueSet][0]==null){
            dialogueSet--;
        }
    }
}

