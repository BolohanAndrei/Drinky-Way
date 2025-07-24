package object;

import Main.GamePanel;

public class Obj_Armour_Boots_Crusty extends SuperObject{
    GamePanel gp;
    public Obj_Armour_Boots_Crusty(GamePanel gp) {
        this.gp = gp;
        name = "Crusty Boots";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/armour_boots_crusty.png"));
            utility.scaleImage(image,gp.tileSize,gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
}}}
