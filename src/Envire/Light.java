package Envire;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Light {
    BufferedImage darknessFilter;
    public int dayCounter;
    public float filterAlpha;
    public final int day=0;
    public final int dusk=1;
    public final int night=2;
    public final int dawn=3;
    public int dayState=day;

    private int lastPlayerX = -1;
    private int lastPlayerY = -1;
    private static final int MOVEMENT_THRESHOLD = 32;
    private int framesSinceLastUpdate = 0;
    private static final int MIN_FRAMES_BETWEEN_UPDATES = 10;

    private int previousMap = -1;


    public Light(GamePanel gp,int circle) {
        generateDarknessFilter(gp, circle);
    }

    private void generateDarknessFilter(GamePanel gp, int circle) {
        darknessFilter=new BufferedImage(gp.screenWidth,gp.screenHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2=(Graphics2D)darknessFilter.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

        int centerX=gp.player.screenX+gp.tileSize/2;
        int centerY=gp.player.screenY+gp.tileSize/2;

        Color[] color = new Color[8];
        float[] fraction = new float[8];

        for (int i = 0; i < 8; i++) {
            float alpha = 0.1f + 0.9f * (1 - (float)Math.exp(-0.4 * i));
            color[i] = new Color(0, 0, 0, Math.min(alpha, 0.98f));

            if (i == 0) {
                fraction[i] = 0f;
            } else if (i == 7) {
                fraction[i] = 1f;
            } else {
                fraction[i] = 0.4f + 0.6f * (1 - (float)Math.exp(-0.3 * (i - 1)));
            }
        }

        RadialGradientPaint gradientPaint=new RadialGradientPaint(centerX,centerY, (float) circle /2,fraction,color);
        g2.setPaint(gradientPaint);
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.dispose();

        lastPlayerX = gp.player.x;
        lastPlayerY = gp.player.y;
    }

    public void update(GamePanel gp, int circle){
        int playerX = gp.player.x;
        int playerY = gp.player.y;

        if (Math.abs(playerX - lastPlayerX) > MOVEMENT_THRESHOLD ||
            Math.abs(playerY - lastPlayerY) > MOVEMENT_THRESHOLD) {
            framesSinceLastUpdate++;
            if (framesSinceLastUpdate >= MIN_FRAMES_BETWEEN_UPDATES) {
                generateDarknessFilter(gp, circle);
                framesSinceLastUpdate = 0;
            }
        }



        if (previousMap != gp.currentMap) {
            handleMapTransition(gp.currentMap, previousMap);
            previousMap = gp.currentMap;
            return;
        }

        if (gp.currentMap == 2 || gp.currentMap == 3) {
            filterAlpha = 1f;
        } else {
            updateDayNightCycle();
        }
    }

    private void handleMapTransition(int currentMap, int prevMap) {
        if (currentMap == 2 || currentMap == 3) {
            filterAlpha = 1f;
        }
        else if ((prevMap == 2 || prevMap == 3) && (currentMap == 0 || currentMap == 1)) {
            resetToOverworldLighting();
        }
    }

    private void resetToOverworldLighting() {
        switch (dayState) {
            case day:
                filterAlpha = 0f;
                break;
            case dusk:
                filterAlpha = 0f;
                break;
            case night:
                filterAlpha = 1f;
                break;
            case dawn:
                filterAlpha = 1f;
                break;
        }
    }

    private void updateDayNightCycle() {
        if (dayState == day) {
            dayCounter++;
            if (dayCounter > 36000) {
                dayState = dusk;
                filterAlpha = 0f;
                dayCounter = 0;
            }
        }
        if (dayState == dusk) {
            filterAlpha += 0.0001f;
            if (filterAlpha > 1f) {
                filterAlpha = 1f;
                dayState = night;
            }
        }
        if (dayState == night) {
            dayCounter++;
            if (dayCounter > 36000) {
                filterAlpha = 1f;
                dayState = dawn;
                dayCounter = 0;
            }
        }
        if (dayState == dawn) {
            filterAlpha -= 0.0001f;
            if (filterAlpha < 0f) {
                filterAlpha = 0f;
                dayState = day;
            }
        }
    }

    public void draw(Graphics2D g2){
        if (filterAlpha > 0.01f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,filterAlpha));
            g2.drawImage(darknessFilter,0,0,null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
        }
    }
}
