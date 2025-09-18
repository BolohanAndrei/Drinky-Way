package Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utility {
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        if (original == null) return null;
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage scaled = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return scaled;
    }
}
