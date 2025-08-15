package object;

import Entity.Entity;
import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Silver_Key extends Entity {
    public Obj_Silver_Key(GamePanel gp)
    {
        super(gp);
        name="Silver_Key";
        down1=setup("objects/silver_key");

    }
}
