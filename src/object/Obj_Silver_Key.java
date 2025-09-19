package object;

import Entity.Entity;
import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Silver_Key extends Entity {
    public static final String objName="Silver_Key";
    public Obj_Silver_Key(GamePanel gp)
    {
        super(gp);
        name=objName;
        stackable=true;
        down1=setup("objects/silver_key");
        value=200;

    }
}
