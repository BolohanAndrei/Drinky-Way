package Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utility {
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaleImage=new BufferedImage(width,height,original.getType());
        Graphics2D g2d =  scaleImage.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);
        return  scaleImage;
    }
}
