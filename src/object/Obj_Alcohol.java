package object;

import Entity.Entity;
import Main.GamePanel;


public class Obj_Alcohol extends Entity {

    public static final String objName="Alcohol";

    public Obj_Alcohol(GamePanel gp)
    {
        super(gp);
        name=objName;
        image1=setup("stats/FullBottle");
        image2=setup("stats/HalfBottle");
        image3=setup("stats/EmptyBottle");
    }
}
