package Envire;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Light {
    BufferedImage darknessFilter;
    public int dayCounter;
   public  float filterAlpha;
    public final int day=0;
    public final int dusk=1;
    public final int night=2;
    public final int dawn=3;
   public  int dayState=day;


    public Light(GamePanel gp,int circle) {
        darknessFilter=new BufferedImage(gp.screenWidth,gp.screenHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2=(Graphics2D)darknessFilter.getGraphics();
        int centerX=gp.player.screenX+gp.tileSize/2;
        int centerY=gp.player.screenY+gp.tileSize/2;

        Color[] color = new Color[12];
        float[] fraction = new float[12];

        for (int i = 0; i < 12; i++) {
            float alpha = 0.1f + 0.9f * (1 - (float)Math.exp(-0.3 * i));
            color[i] = new Color(0, 0, 0, Math.min(alpha, 0.98f));

            if (i == 0) {
                fraction[i] = 0f;
            } else if (i == 11) {
                fraction[i] = 1f;
            } else {
                fraction[i] = 0.4f + 0.6f * (1 - (float)Math.exp(-0.25 * (i - 1)));
            }
        }

        RadialGradientPaint gradientPaint=new RadialGradientPaint(centerX,centerY, (float) circle /2,fraction,color);
        g2.setPaint(gradientPaint);
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.dispose();
    }
    public void update(){
        if(dayState==day){
            dayCounter++;
            if(dayCounter>36000){
                dayState=dusk;
                dayCounter=0;
            }
        }
        if(dayState==dusk){
            filterAlpha+=0.0001f;
            if(filterAlpha>1f){
                filterAlpha=1f;
                dayState=night;
            }
        }
        if(dayState==night){
            dayCounter++;
            if(dayCounter>36000){
                dayState=dawn;
                dayCounter=0;
            }
        }
        if(dayState==dawn){
            filterAlpha-=0.0001f;
            if(filterAlpha<0f){
                filterAlpha=0f;
                dayState=day;
            }
        }
    }
    public void draw(Graphics2D g2){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,filterAlpha));
        g2.drawImage(darknessFilter,0,0,null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
    }
}
