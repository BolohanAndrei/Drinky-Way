package object;

import Entity.Entity;
import Main.GamePanel;

import java.util.ArrayList;

public class Obj_Chest extends Entity {
    GamePanel gp;
    public static final String objName="Chest";
    public Obj_Chest(GamePanel gp) {
        super(gp);
        this.gp=gp;

        obstacle=true;
        pickable=false;
        name = objName;
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
        setDialogue();
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
        inventory.add(new object.Obj_Gold_Key(gp));
        inventory.add(new object.Obj_Silver_Key(gp));
    }

    public void setDialogue() {
        dialogue[0][0]="Arrr, ye can't drop the steel on yer back, ye drunken fool! Unequip it first!";
        dialogue[1][0]="Chest Full";
        dialogue[2][0]="Inventory Full";

    }
}
