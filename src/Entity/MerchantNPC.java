// File: src/Entity/MerchantNPC.java
package Entity;

import Main.GamePanel;

public class MerchantNPC extends MovementNPC {

    public MerchantNPC(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1;
        entityType = 1;

        getMerchantNPCImage();
        setDialogue();
        setItems();
    }

    public void getMerchantNPCImage() {
        try {
            down1 = setup("NPC/b_down1");
            down2 = setup("NPC/b_down2");

            up1 = down1;
            up2 = down2;
            left1 = down1;
            left2 = down2;
            right1 = down1;
            right2 = down2;
            idle_1 = down1;
            idle_2 = down2;
            idle_3 = down1;
            idle_4 = down2;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setAction() {
        direction = "down";
    }

    @Override
    public void update() {
        setAction();

        spriteCounter++;
        if (spriteCounter > 45) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void setDialogue() {
        dialogue[0] = "Arrr, welcome ye scallywag! Got items to TRADE enough to sink a whale. What be ticklin’ yer fancy today?";
    }

    public void setItems() {
        inventory.add(new object.Obj_Drugs(gp));
        inventory.add(new object.Obj_Rum(gp));
        inventory.add(new object.Obj_Gold_Key(gp));
        inventory.add(new object.Obj_Silver_Key(gp));
        inventory.add(new object.Obj_Emerald_Key(gp));
        inventory.add(new object.Obj_Iron_Sword(gp));
        inventory.add(new object.Obj_Beer(gp));
    }

    @Override
    public void speak() {
        super.speak();
        gp.keyHandler.previousGameState = gp.gameState;
        gp.gameState = gp.tradeState;
        gp.ui.subState = 0;
        gp.ui.commandNum = 0;
        gp.ui.trade = this;
    }
}