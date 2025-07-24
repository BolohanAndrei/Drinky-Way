package Main;

import Entity.DrunkNPC;
//import Entity.DrunkNPC1;
import Entity.DrunkNPC1;
import object.*;

public class ObjManager {
    GamePanel gp;
    public ObjManager(GamePanel gp) {
        this.gp = gp;
    }
    //initializes the objects in the game
    public void setObj(){

    }

    public void setNPC(){
        gp.npc[0]=new DrunkNPC(gp);
        gp.npc[0].x = 21 * gp.tileSize;
        gp.npc[0].y = 21 * gp.tileSize;
        gp.npc[1]=new DrunkNPC1(gp);
        gp.npc[1].x = 23 * gp.tileSize;
        gp.npc[1].y = 21 * gp.tileSize;

    }
}
