package Entity;

import Main.GamePanel;



public class DrunkNPC1 extends MovementNPC{

    public DrunkNPC1(GamePanel gp) {
        super(gp);
        direction = "up";
        speed = 1;
        entityType = 1;

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
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }

    public void setDialogue() {
        dialogue[0] = "Ahoy, Matey!";
        dialogue[1] = "Did you try this rum?";
        dialogue[2] = "I think I need to sit down...";
        dialogue[3] = "The world is spinning!";
        dialogue[4] = "Where am I again?";
    }

    public void setAction() {
        super.setAction();
    }

    public void speak() {
        super.speak();
    }

}
