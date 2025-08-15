package object;

import Entity.Entity;
import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Gold_Key extends Entity {
    public Obj_Gold_Key(GamePanel gp)
    {
        super(gp);
        name="Gold_Key";
        down1=setup("objects/gold_key");
    }
}
