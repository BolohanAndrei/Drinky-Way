package Main;

import Entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Random;

public class DrinkSystem {
    private static final boolean ENABLE_VOLATILE = false;
    private static final boolean USE_PREMULTIPLIED = false;
    private static final boolean USE_RADIAL_MASK = true;

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
    private VolatileImage sceneVolatile;
    private boolean useVolatile;

    private BufferedImage overlayCache;
    private BufferedImage radialMask;
    private int lastOverlayBucket = -1;
    private boolean overlayDirty = true;

    public DrinkSystem(GamePanel gp){
        this.gp=gp;
        decayIntervalFrames=600;
        decayAmountPercent=5;
        evaluateVolatileEligibility();
    }

    private void evaluateVolatileEligibility(){
        long pixels = (long) gp.screenWidth * gp.screenHeight;
        useVolatile = ENABLE_VOLATILE && pixels > 400_000;
    }

    public void update(Player p){
        frameCounter++;

        int fpsNow = gp.getCurrentFps();
        boolean lowPerf = fpsNow>0 && fpsNow < 50;
        if(lowPerf){
            if(overlayDirty && frameCounter % 10 != 0){
                // do nothing here
            }
        }

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

            if(p.drinkPercent==0 && (!trail.isEmpty() || p.drunkOriginalTx!=null)){
                soberUp(p);
            }
        }

        if(p.drinkPercent < 30){
            if(lastOverlayBucket != -1){
                lastOverlayBucket = -1;
            }
        } else {
            int bucket = p.drinkPercent / 5;
            if(bucket != lastOverlayBucket){
                lastOverlayBucket = bucket;
                overlayDirty = true;
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
        if(p.drinkPercent < 30) return;
        int fpsNow = gp.getCurrentFps();
        if(fpsNow>0 && fpsNow < 45) return;

        if (overlayCache == null || overlayCache.getWidth() != gp.screenWidth ||
            overlayCache.getHeight() != gp.screenHeight || overlayDirty) {
            generateOverlayCache(p);
            overlayDirty = false;
        }

        g2.drawImage(overlayCache, 0, 0, null);
    }

    private void ensureOverlayCache(){
        int type = USE_PREMULTIPLIED ? BufferedImage.TYPE_INT_ARGB_PRE : BufferedImage.TYPE_INT_ARGB;
        if(overlayCache==null || overlayCache.getWidth()!=gp.screenWidth || overlayCache.getHeight()!=gp.screenHeight || overlayCache.getType()!=type){
            overlayCache = new BufferedImage(gp.screenWidth, gp.screenHeight, type);
            radialMask = null;
        }
    }

    private void buildRadialMask(){
        if(!USE_RADIAL_MASK){ radialMask=null; return; }
        int w = gp.screenWidth;
        int h = gp.screenHeight;
        int type = USE_PREMULTIPLIED ? BufferedImage.TYPE_INT_ARGB_PRE : BufferedImage.TYPE_INT_ARGB;
        radialMask = new BufferedImage(w,h,type);
        int cx = w/2;
        int cy = h/2;
        double maxR = Math.max(w,h);
        double innerR = maxR*0.8;
        double innerR2 = innerR*innerR;
        int[] pixels = ((java.awt.image.DataBufferInt)radialMask.getRaster().getDataBuffer()).getData();
        for(int y=0;y<h;y++){
            int dy = y-cy;
            for(int x=0;x<w;x++){
                int dx = x-cx;
                double d2 = dx*dx + dy*dy;
                double alpha;
                if(d2 <= innerR2){
                    double t = Math.sqrt(d2)/innerR;
                    alpha = t*80.0;
                } else {
                    double t = (Math.sqrt(d2)-innerR)/(maxR-innerR);
                    if(t>1) t=1;
                    alpha = 80.0 + t*80.0;
                }
                int a = (int)Math.round(alpha);
                if(a<0) a=0; else if(a>160) a=160;
                pixels[y*w + x] = (a<<24);
            }
        }
    }

    private void generateOverlayCache(Player p) {
        ensureOverlayCache();
        if(USE_RADIAL_MASK && (radialMask==null || radialMask.getWidth()!=gp.screenWidth || radialMask.getHeight()!=gp.screenHeight)){
            buildRadialMask();
        }
        Graphics2D g2 = overlayCache.createGraphics();
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.setComposite(old);

        float intensity = getIntensity(p);

        float alpha = 0.12f + 0.35f * intensity;
        g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
        g2.setColor(new Color(120, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if(USE_RADIAL_MASK && intensity>0f && radialMask!=null){
             old = g2.getComposite();
            g2.setComposite(AlphaComposite.SrcOver.derive(intensity));
            g2.drawImage(radialMask,0,0,null);
            g2.setComposite(old);
        }
        g2.dispose();
    }

    public void doubleVisionComposite(Graphics2D g2, Image worldImage) {
        Player p = gp.player;
        int fpsNow = gp.getCurrentFps();
        if(fpsNow>0 && fpsNow < 45){
            g2.drawImage(worldImage,0,0,null);
            return;
        }

        if (p.drinkPercent < 40) {
            g2.drawImage(worldImage, 0, 0, null);
            return;
        }

        float intensity = getIntensity(p);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

        g2.drawImage(worldImage, 0, 0, null);

        if (intensity <= 0.3f) return;
        if (frameCounter % 2 != 0) return;

        int off = Math.min(8, (int)(3 + 6 * intensity));
        if (off <= 2) return;

        Composite old = g2.getComposite();
        float alpha = Math.min(0.4f, 0.10f + 0.25f * intensity);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.drawImage(worldImage, off, 0, null);
        g2.drawImage(worldImage, -off, 0, null);
        g2.setComposite(old);
    }

    private void ensureSceneBuffer(){
        int w = gp.screenWidth;
        int h = gp.screenHeight;
        int type = USE_PREMULTIPLIED ? BufferedImage.TYPE_INT_ARGB_PRE : BufferedImage.TYPE_INT_ARGB;
        if(useVolatile){
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();
            if(sceneVolatile==null || sceneVolatile.getWidth()!=w || sceneVolatile.getHeight()!=h){
                if(sceneVolatile!=null){ sceneVolatile.flush(); }
                sceneVolatile = gc.createCompatibleVolatileImage(w,h, Transparency.TRANSLUCENT);
            }
            if(sceneVolatile.validate(gc)==VolatileImage.IMAGE_INCOMPATIBLE){
                sceneVolatile.flush();
                sceneVolatile = gc.createCompatibleVolatileImage(w,h, Transparency.TRANSLUCENT);
            }
        } else if(sceneBuffer==null || sceneBuffer.getWidth()!=w || sceneBuffer.getHeight()!=h || sceneBuffer.getType()!=type){
            sceneBuffer = new BufferedImage(w,h,type);
        }
    }

    public Graphics2D beginWorldBuffer() {
        ensureSceneBuffer();
        if(useVolatile && sceneVolatile!=null){
            Graphics2D g2 = sceneVolatile.createGraphics();
            g2.setComposite(AlphaComposite.Src);
            g2.setColor(new Color(0,0,0,0));
            g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            g2.setClip(0,0,gp.screenWidth,gp.screenHeight);
            return g2;
        } else {
            Graphics2D g2 = sceneBuffer.createGraphics();
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            g2.setClip(0,0,gp.screenWidth,gp.screenHeight);
            return g2;
        }
    }

    public void flushWorldBuffer(Graphics2D g2) {
        if(useVolatile && sceneVolatile!=null){
            doubleVisionComposite(g2, sceneVolatile);
        } else if(sceneBuffer!=null){
            doubleVisionComposite(g2, sceneBuffer);
        }
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
