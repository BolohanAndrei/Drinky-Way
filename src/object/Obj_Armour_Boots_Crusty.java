package object;

import Main.GamePanel;

public class Obj_Armour_Boots_Crusty extends SuperObject{
    GamePanel gp;
    public Obj_Armour_Boots_Crusty(GamePanel gp) {
        this.gp = gp;
        name = "Crusty Boots";
        try {
            image1 = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/armour_boots_crusty.png"));
            utility.scaleImage(image1,gp.tileSize,gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
}}}
