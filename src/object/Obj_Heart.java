package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Heart extends Entity {
    public Obj_Heart(GamePanel gp)
    {
        super(gp);
        name="Heart";
        image1=setup("stats/FullHeart");
        image2=setup("stats/HalfHeart");
        image3=setup("stats/EmptyHeart");
    }
}
