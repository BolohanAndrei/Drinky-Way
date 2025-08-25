package Entity;

import Main.GamePanel;

import java.awt.*;

public class Particle extends Entity{
    Entity generator;
    Color color;
    int xd;
    int yd;
    int size;

    public Particle(GamePanel gp,Entity generator,Color color,int xd,int yd,int size,int speed,int maxLife) {
        super(gp);

        this.generator=generator;
        this.color=color;
        this.xd=xd;
        this.yd=yd;
        this.size=size;
        this.speed=speed;
        this.maxHealth=maxLife;

        health=maxLife;
        int offset=(gp.tileSize/2)-(size/2);
        x=generator.x+offset;
        y=generator.y+offset;
    }

    public void update(){
        health--;
        if(health<maxHealth/3){
            yd++;
        }
        x+=xd*speed;
        y+=yd*speed;
        if(health<=0){
            alive=false;
        }
    }

    public void draw(Graphics2D g){
        int screeX=x-gp.player.x+gp.player.screenX;
        int screeY=y-gp.player.y+gp.player.screenY;

        g.setColor(color);
        g.fillRect(screeX,screeY,size,size);
    }
}
