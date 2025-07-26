package object;

import Main.GamePanel;

public class Obj_Skull_Door extends SuperObject{
    GamePanel gp;
    public Obj_Skull_Door(GamePanel gp) {
        this.gp = gp;
        name = "Skull_Door";
        collision = true;
        try {
            image1 = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/skull_door.png"));
            utility.scaleImage(image1,gp.tileSize,gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
