package object;

import javax.imageio.ImageIO;

public class Obj_Gold_Key extends SuperObject{
    public Obj_Gold_Key()
    {
        name="Gold_Key";
        try{
            image= ImageIO.read(getClass().getResourceAsStream("/res/objects/gold_key.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
