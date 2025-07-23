package object;

public class Obj_Emerald_Key extends SuperObject {
    public Obj_Emerald_Key() {
        name = "Emerald_Key";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/emerald_key.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
