package object;

import Main.GamePanel;

public class Obj_Chest extends  SuperObject{
    GamePanel gp;
    public Obj_Chest(GamePanel gp) {
        this.gp = gp;
        name = "Chest";
        collision = true;
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/chest.png"));
            utility.scaleImage(image,gp.tileSize,gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
