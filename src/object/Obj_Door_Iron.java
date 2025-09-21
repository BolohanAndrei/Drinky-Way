package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Door_Iron extends Entity {
    GamePanel gp;
    public static final String objName="Door Iron";
    public Obj_Door_Iron(GamePanel gp) {

        super(gp);
        this.gp = gp;
        name = objName;
        pickable=false;
        obstacle=true;
        collision = true;
        down1=setup("objects/door_iron");

        solidArea.x=0;
        solidArea.y=16;
        solidArea.width=48;
        solidArea.height=32;
        solidAreaDefaultX=solidArea.x;
        solidAreaDefaultY=solidArea.y;

        setDialogue();
    }

    public void setDialogue(){
        dialogue[0][0] = "Arrr! This door won’t budge ‘til ye prove yer worth!";

    }
    public void interact(){

        startDialogue(this,0);
        gp.se.playSE(37);
    }
}
