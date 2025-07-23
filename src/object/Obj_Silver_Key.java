package object;

import javax.imageio.ImageIO;

public class Obj_Silver_Key extends SuperObject{
    public Obj_Silver_Key()
    {
        name="Silver_Key";
        try{
            image= ImageIO.read(getClass().getResourceAsStream("/res/objects/silver_key.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
