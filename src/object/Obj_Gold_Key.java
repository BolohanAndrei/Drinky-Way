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

        setDialogue();
    }

    public void setDialogue()
    {
        dialogue[0][0] = "Arrr! The gold key be turnin’, and the door gives way!";
        dialogue[1][0] = "What in the seven seas are ye tryin’ to unlock, ye drunken fool?";

    }
    public boolean use(Entity entity) {
        int objIndex=getDetected(entity,gp.obj,"Door");
        if(objIndex!=999){
            startDialogue(this,0);
            gp.se.playSE(3);
            gp.obj[gp.currentMap][objIndex]=null;
            return true;
        }else{
            startDialogue(this,1);
        }
        return  false;
    }
}
