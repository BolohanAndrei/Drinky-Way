package object;

import Entity.Entity;
import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Gold_Key extends Entity {
    GamePanel gp;
    public Obj_Gold_Key(GamePanel gp)
    {
        super(gp);
        this.gp=gp;
        name="Gold Key";
        gearType=2;
        stackable=true;
        down1=setup("objects/gold_key");
        value=500;
        itemDescription = "["+name+"]\nOpens chests, doors, and maybe your ex’s heart.\nShiny enough to blind a sober man.";

    }

    public boolean use(Entity entity) {
        gp.gameState=gp.dialogueState;

        int objIndex=getDetected(entity,gp.obj,"Door");
        if(objIndex!=999){
            gp.ui.currentDialogue = "Arrr! The gold key be turnin’, and the door gives way!";
            gp.se.playSE(3);
            gp.obj[gp.currentMap][objIndex]=null;
            return true;
        }else{
            gp.ui.currentDialogue = "What in the seven seas are ye tryin’ to unlock, ye drunken fool?";
        }
        return  false;
    }
}
