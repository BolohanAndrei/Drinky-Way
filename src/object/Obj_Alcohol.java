package object;

import Main.GamePanel;

import javax.imageio.ImageIO;

public class Obj_Alcohol extends SuperObject{
    GamePanel gp;
    public Obj_Alcohol(GamePanel gp)
    {
        this.gp = gp;
        name="Heart";
        try{
            image1= ImageIO.read(getClass().getResourceAsStream("/res/stats/FullBottle.png"));
            image2= ImageIO.read(getClass().getResourceAsStream("/res/stats/HalfBottle.png"));
            image3= ImageIO.read(getClass().getResourceAsStream("/res/stats/EmptyBottle.png"));
            image1=utility.scaleImage(image1,gp.tileSize,gp.tileSize);
            image2= utility.scaleImage(image2,gp.tileSize,gp.tileSize);
            image3=utility.scaleImage(image3,gp.tileSize,gp.tileSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
