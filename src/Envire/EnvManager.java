package Envire;

import Main.GamePanel;

import java.awt.*;

public class EnvManager {
    GamePanel gp;
    public Light light;

    public EnvManager(GamePanel gp) {
        this.gp = gp;

    }
    public void setup(){
        light=new Light(gp,400);
    }
    public void draw(Graphics2D g2){
        light.draw(g2);
    }
    public void update(){
        light.update(gp, 400);
    }
}
