package object;

import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Gold_Key extends SuperObject{
    GamePanel gp;
    public Obj_Gold_Key(GamePanel gp)
    {
        this.gp = gp;
        name="Gold_Key";
        try{
            image= ImageIO.read(getClass().getResourceAsStream("/res/objects/gold_key.png"));
            utility.scaleImage(image,gp.tileSize,gp.tileSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
