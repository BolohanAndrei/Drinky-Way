package object;

public class Obj_Armour_Boots_Crusty extends SuperObject{
    public Obj_Armour_Boots_Crusty() {
        name = "Crusty Boots";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/objects/armour_boots_crusty.png"));
        } catch (Exception e) {
            e.printStackTrace();
}}}
