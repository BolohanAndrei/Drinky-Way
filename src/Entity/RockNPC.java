package Entity;

import Main.GamePanel;
import object.Obj_Door_Iron;
import tiles_interactive.IT_MetalPlate;
import tiles_interactive.InteractiveTiles;

import java.awt.*;
import java.util.ArrayList;

public class RockNPC extends Entity {
    public static final String npcName="Rock";
    public RockNPC(GamePanel gp) {
        super(gp);
        direction = "down";
        name=npcName;
        speed = 2;
        entityType = 1;

        solidArea = new Rectangle(2, 6, 44, 40);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        dialogueSet=-1;

        getDrunkNPCImage();
        setDialogue();
    }

    public void getDrunkNPCImage() {
        try {
            up1 = setup("NPC/bigrock");
            up2 = setup("NPC/bigrock");
            down1 = setup("NPC/bigrock");
            down2 = setup("NPC/bigrock");
            left1 = setup("NPC/bigrock");
            left2 = setup("NPC/bigrock");
            right1 = setup("NPC/bigrock");
            right2 = setup("NPC/bigrock");

            upLeft1 = up1; upLeft2 = left2;
            upRight1 = right1; upRight2 = right2;
            downLeft1 = left1; downLeft2 = left2;
            downRight1 = right1; downRight2 = right2;

            idle_1 = setup("NPC/bigrock");
            idle_2 = setup("NPC/bigrock");
            idle_3 = setup("NPC/bigrock");
            idle_4 = setup("NPC/bigrock");
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }

    public void setDialogue() {
        dialogue[0][0] = "Ahoy, Captain! I bet ye can't move me";
    }

    public void setAction() {
    }
    public void update() {
    }
    public void speak(){
        facePlayer();
        startDialogue(this,dialogueSet);
        dialogueSet++;
        if(dialogue[dialogueSet][0]==null){
            dialogueSet--;
        }
    }
    public void move(String direction) {
        this.direction = direction;
        checkCollision();
        if(!collisionOn){
            switch(direction){
                case "down": y += speed; break;
                case "up": y -= speed; break;
                case "left": x -= speed; break;
                case "right": x += speed; break;
            }
        }
        detectPlate();
    }

    public void detectPlate(){
        ArrayList<InteractiveTiles> plateList=new ArrayList<>();
        ArrayList<Entity> rockList=new ArrayList<>();

        for(int i=0;i<gp.iTile[gp.currentMap].length;i++){
            if(gp.iTile[gp.currentMap][i]!=null && gp.iTile[gp.currentMap][i].name!=null && gp.iTile[gp.currentMap][i].name.equals(IT_MetalPlate.itName)){
                plateList.add(gp.iTile[gp.currentMap][i]);
            }
        }
        for(int i=0;i<gp.npc[gp.currentMap].length;i++){
            if(gp.npc[gp.currentMap][i]!=null && gp.npc[gp.currentMap][i].name!=null && gp.npc[gp.currentMap][i].name.equals(RockNPC.npcName)){
                rockList.add(gp.npc[gp.currentMap][i]);
            }
        }

        int count=0;
        for(int i=0;i<plateList.size();i++){
            int xDistance=Math.abs(x-plateList.get(i).x),yDistance=Math.abs(y-plateList.get(i).y);
            int distance=Math.max(xDistance,yDistance);
            if(distance<8){
                if (linkedEntity == null) {
                    linkedEntity=plateList.get(i);
                    gp.se.playSE(38);
                }
            }else{
                if(linkedEntity == plateList.get(i)) {
                    linkedEntity=null;
                }
            }
        }
        for(int i=0;i<rockList.size();i++){
            if(rockList.get(i).linkedEntity!=null){
                count++;
            }
        }
        if(count==rockList.size()){
            for(int i=0;i<gp.obj[gp.currentMap].length;i++){
                if(gp.obj[gp.currentMap][i]!=null && gp.obj[gp.currentMap][i] instanceof Obj_Door_Iron){
                    gp.obj[gp.currentMap][i]=null;
                    gp.se.playSE(39);
                }
            }
        }
    }
}
