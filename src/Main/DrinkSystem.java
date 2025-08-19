package Main;

import Entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Random;

public class DrinkSystem {
    private final GamePanel gp;
    private long frameCounter;
    private final int decayIntervalFrames;
    private final int decayAmountPercent;

    private static class AfterImage{
        int x,y,life;
        AfterImage(int x,int y,int life){
            this.x=x;this.y=y;this.life=life;
        }
    }

    private final ArrayDeque<AfterImage> trail = new ArrayDeque<>();
    private final Random rand=new Random();

    private BufferedImage sceneBuffer;

    public DrinkSystem(GamePanel gp){
        this.gp=gp;
        decayIntervalFrames=600;
        decayAmountPercent=5;
    }

    public void update(Player p){
        frameCounter++;

        if(p.drinkPercent>0 && frameCounter%decayIntervalFrames==0){
            p.drinkPercent=Math.max(0,p.drinkPercent-decayAmountPercent);

            if(p.currentSpeed>0 && p.drinkPercent>=70 && frameCounter%6==0){
                trail.add(new AfterImage(p.x,p.y,30));
                while(trail.size()>40){
                    trail.pollFirst();
                }
            }
            Iterator<AfterImage> itr=trail.iterator();
            while(itr.hasNext()){
                AfterImage temp=itr.next();
                temp.life--;
                if(temp.life==0){
                    itr.remove();
                }
            }

            if(p.drinkPercent>=95){
                if(rand.nextInt(10)<=5){
                    gp.sound.playSE(6);
                }
                else {
                    gp.sound.playSE(7);
                }
            }

            if(p.drinkPercent==0 && (!trail.isEmpty() || p.drunkOriginalTx!=null)){
                soberUp(p);
            }
        }
    }

    public void distortInput(Player p,double[] dxy){
        float intensity=getIntensity(p);
        if(p.drinkPercent>=50){
            double wobble=intensity*0.4;
            dxy[0]+=wobble*(rand.nextDouble()*2-1);
            dxy[1]+=wobble*(rand.nextDouble()*2-1);
        }
        if(p.drinkPercent>=60){
            double chance=0.001+intensity*0.002;
            if(rand.nextDouble()<chance){
                dxy[0]+=(rand.nextDouble()*2-1)*(0.8*intensity);
                dxy[1]+=(rand.nextDouble()*2-1)*(0.8*intensity);
            }
        }
    }

    public void preWorldTransform(Graphics2D g2){
        Player p=gp.player;
        float intensity=getIntensity(p);

        if(p.drunkOriginalTx==null){
            p.drunkOriginalTx=g2.getTransform();
        }

        if(intensity<=0f) return;

        double t=gp.playerTick();
        double swayAmp=8*intensity;
        double swayX=Math.sin(t*0.07)*swayAmp;
        double swayY=Math.sin(t*0.055 +1.1)*swayAmp*0.7;

        double rot = (p.drinkPercent >= 20) ? Math.sin(t * 0.04) * 0.02 * intensity : 0;
        double scale = 1.0 + ((p.drinkPercent >= 20) ? Math.sin(t * 0.05) * 0.02 * intensity : 0);

        double cx=gp.screenWidth/2.0;
        double cy=gp.screenHeight/2.0;
        g2.translate(cx+swayX,cy+swayY);
        g2.rotate(rot);
        g2.scale(scale,scale);
        g2.translate(-cx,-cy);
    }

    public void postWorldTransform(Graphics2D g2){
        if(gp.player.drunkOriginalTx!=null){
            g2.setTransform(gp.player.drunkOriginalTx);
            gp.player.drunkOriginalTx=null;
        }
    }

    public void overlay(Graphics2D g2){
        Player p=gp.player;
        int percent=p.drinkPercent;
        if(percent<30) return;

        float intensity=getIntensity(p);
        Composite old=g2.getComposite();

        float alpha = 0.12f + 0.35f * intensity;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(120,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        RadialGradientPaint rg=new RadialGradientPaint(
                gp.screenWidth/2f,
                gp.screenHeight/2f,
                Math.max(gp.screenWidth,gp.screenHeight),
                new float[]{0f,0.8f,1f},
                new Color[]{
                        new Color(0, 0, 0, 0),
                        new Color(0, 0, 0, (int)(80 * intensity)),
                        new Color(0, 0, 0, (int)(160 * intensity))
                }
        );

        g2.setPaint(rg);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        g2.setComposite(old);

    }

    public void doubleVisionComposite(Graphics2D g2, BufferedImage worldImage) {
        Player p = gp.player;
        if (p.drinkPercent < 40) {
            g2.drawImage(worldImage, 0, 0, null);
            return;
        }
        float intensity = getIntensity(p);
        g2.drawImage(worldImage, 0, 0, null); // base
        Composite old = g2.getComposite();
        float alpha = 0.10f + 0.25f * intensity;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        int off = (int)(3 + 6 * intensity);
        g2.drawImage(worldImage, off, 0, null);
        g2.drawImage(worldImage, -off, 0, null);
        g2.setComposite(old);
    }

    public Graphics2D beginWorldBuffer() {
        if (sceneBuffer == null ||
                sceneBuffer.getWidth() != gp.screenWidth ||
                sceneBuffer.getHeight() != gp.screenHeight) {
            sceneBuffer = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g2 = sceneBuffer.createGraphics();
        g2.setClip(0,0,gp.screenWidth,gp.screenHeight);
        return g2;
    }

    public void flushWorldBuffer(Graphics2D g2) {
        doubleVisionComposite(g2, sceneBuffer);
    }

    public void drawAfterImages(Graphics2D g2) {
        if (gp.player.drinkPercent < 70) return;
        float intensity = getIntensity(gp.player);
        for (AfterImage a : trail) {
            int age = a.life;
            float alpha = Math.max(0, Math.min(1f, age / 30f)) * (0.5f * intensity);
            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            int screenX = a.x - gp.player.x + gp.player.screenX;
            int screenY = a.y - gp.player.y + gp.player.screenY;
            g2.drawImage(gp.player.getCurrentFrame(), screenX, screenY, gp.tileSize, gp.tileSize, null);
            g2.setComposite(old);
        }
    }

    public String slurIfNeeded(String original) {
        if (gp.player.drinkPercent < 90 || original == null) return original;
        StringBuilder sb = new StringBuilder();
        for (char c : original.toCharArray()) {
            sb.append(c);
            if ("aeiouAEIOU".indexOf(c) >= 0 && rand.nextFloat() < 0.35f) {
                sb.append(c);
            }
            if (rand.nextFloat() < 0.05f) sb.append('-');
        }
        return sb.toString();
    }

    private float getIntensity(Player p) {
        return Math.min(1f, p.drinkPercent / 100f);
    }

    public void soberUp(Player p){
        p.drinkPercent =0;
        trail.clear();
    }

}
