package object;

import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Heart extends SuperObject{
    GamePanel gp;
    public Obj_Heart(GamePanel gp)
    {
        this.gp = gp;
        name="Heart";
        try{
            image1= ImageIO.read(getClass().getResourceAsStream("/res/stats/FullHeart.png"));
            image2= ImageIO.read(getClass().getResourceAsStream("/res/stats/HalfHeart.png"));
            image3= ImageIO.read(getClass().getResourceAsStream("/res/stats/EmptyHeart.png"));
            image1=utility.scaleImage(image1,gp.tileSize,gp.tileSize);
            image2= utility.scaleImage(image2,gp.tileSize,gp.tileSize);
            image3=utility.scaleImage(image3,gp.tileSize,gp.tileSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
