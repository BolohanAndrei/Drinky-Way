package object;

import Main.GamePanel;
import Entity.Entity;

public class Obj_Door extends Entity {
    GamePanel gp;
    public Obj_Door(GamePanel gp) {

        super(gp);
        this.gp = gp;
        name = "Door";
        pickable=false;
        obstacle=true;
        collision = true;
//        down1=setup("objects/pixel_door");
        down1=setup("objects/door1");

        solidArea.x=0;
        solidArea.y=16;
        solidArea.width=48;
        solidArea.height=32;
        solidAreaDefaultX=solidArea.x;
        solidAreaDefaultY=solidArea.y;

        setDialogue();
    }


    public void setDialogue(){
        dialogue[0][0]= "Arrr! This door won’t budge without a shiny key, matey!";

    }
    public void interact(){

        startDialogue(this,0);
    }
}
