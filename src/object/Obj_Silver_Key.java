package object;

import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Silver_Key extends SuperObject{
    GamePanel gp;
    public Obj_Silver_Key(GamePanel gp)
    {
        this.gp = gp;
        name="Silver_Key";
        try{
            image= ImageIO.read(getClass().getResourceAsStream("/res/objects/silver_key.png"));
            utility.scaleImage(image,gp.tileSize,gp.tileSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
