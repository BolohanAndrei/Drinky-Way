package object;

public class Obj_Chest extends  SuperObject{
    public Obj_Chest() {
        name = "Chest";
        collision = true;
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/chest.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
