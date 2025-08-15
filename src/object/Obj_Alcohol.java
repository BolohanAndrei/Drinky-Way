package object;

import Entity.Entity;
import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Alcohol extends Entity {

    public Obj_Alcohol(GamePanel gp)
    {
        super(gp);
        name="Heart";
        image1=setup("stats/FullBottle");
        image2=setup("stats/HalfBottle");
        image3=setup("stats/EmptyBottle");
    }
}
