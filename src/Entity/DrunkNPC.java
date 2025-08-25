package Entity;

import Main.GamePanel;

public class DrunkNPC extends MovementNPC {

    public DrunkNPC(GamePanel gp) {
        super(gp);
        direction = "right";
        speed = 1;
        entityType = 1;

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
            idle_1 = setup("NPC/drunk_idle_1");
            idle_2 = setup("NPC/drunk_idle_2");
            idle_3 = setup("NPC/drunk_idle_3");
            idle_4 = setup("NPC/drunk_idle_4");
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }

    public void setDialogue() {
        dialogue[0] = "Ahoy, Captain!";
        dialogue[1] = "Did you try this magic potion?";
        dialogue[2] = "Whoooo...are you?";
        dialogue[3] = "The world is spinning!";
        dialogue[4] = "Where am I again?";
    }

    public void setAction() {
        super.setAction();
    }

    public void speak(){
        super.speak();

    }
}

