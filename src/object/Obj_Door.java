package object;

public class Obj_Door extends SuperObject {

    public Obj_Door() {
        name = "Door";
        collision = true;
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/pixel_door.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
