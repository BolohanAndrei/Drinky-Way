package object;

import Main.GamePanel;

public class Obj_Emerald_Key extends SuperObject {
    GamePanel gp;
    public Obj_Emerald_Key(GamePanel gp) {
        this.gp = gp;
        name = "Emerald_Key";
        try {
            image1= javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/emerald_key.png"));
            utility.scaleImage(image1,gp.tileSize,gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
