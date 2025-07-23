package object;

public class Obj_Skull_Door extends SuperObject{
    public Obj_Skull_Door() {
        name = "Skull_Door";
        collision = true;
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/skull_door.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
