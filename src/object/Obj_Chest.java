package object;

import Entity.Entity;
import Main.GamePanel;

import java.util.ArrayList;

public class Obj_Chest extends Entity {
    GamePanel gp;
    public Obj_Chest(GamePanel gp) {
        super(gp);
        this.gp=gp;

        obstacle=true;
        pickable=false;
        name = "Chest";
        collision = true;

        image1=setup("objects/chest_closed");
        image2=setup("objects/chest_opened");
        down1=image1;

        solidArea.x=4;
        solidArea.y=16;
        solidArea.width=40;
        solidArea.height=32;
        solidAreaDefaultX=solidArea.x;
        solidAreaDefaultY=solidArea.y;
        setItems();
    }

    public void interact() {

        gp.ui.chest = this;
        gp.keyHandler.previousGameState = gp.gameState;
        gp.gameState = gp.chestState;
        gp.ui.subState = 0;
        down1 = image2;
        gp.se.playSE(31);
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
}
