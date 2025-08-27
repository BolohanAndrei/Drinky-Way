package object;

import Entity.Entity;
import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Gold_Key extends Entity {
    public Obj_Gold_Key(GamePanel gp)
    {
        super(gp);
        name="Gold Key";
        gearType=2;
        down1=setup("objects/gold_key");
        value=500;
        itemDescription = "["+name+"]\nOpens chests, doors, and maybe your ex’s heart.\nShiny enough to blind a sober man.";

    }
}
