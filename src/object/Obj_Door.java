package object;

import Main.GamePanel;

public class Obj_Door extends SuperObject {
    GamePanel gp;

    public Obj_Door(GamePanel gp) {
        this.gp = gp;
        name = "Door";
        collision = true;
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/pixel_door.png"));
            utility.scaleImage(image,gp.tileSize,gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
