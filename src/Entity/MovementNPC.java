package Entity;

import Main.GamePanel;

import java.util.Random;

public class MovementNPC extends Entity {

    public MovementNPC(GamePanel gp) {
        super(gp);
    }

    @Override
    public void setAction(){
        move();
    }
    public void move(){

        if(onPath){
            int endCol;
            int endRow;

            if (this instanceof DrunkNPC1) {
                endCol = 12;
                endRow = 9;
            } else {
                endCol = (gp.player.x + gp.player.solidArea.x) / gp.tileSize;
                endRow = (gp.player.y + gp.player.solidArea.y) / gp.tileSize;
            }
            searchPath(endCol, endRow);
        }else{
            if (isIdle) {
                idleCounter++;

                if (idleCounter % 60 == 0) {
                    spriteNum = (spriteNum % 4) + 1;
                    switch (spriteNum) {
                        case 1: direction="idle_1"; break;
                        case 2: direction="idle_2"; break;
                        case 3: direction="idle_3"; break;
                        case 4: direction="idle_4"; break;
                    }
                }

                if (idleCounter >= idleDuration) {
                    isIdle = false;
                    idleCounter = 0;
                }
                return;
            }

            actionLockCounter++;
            if (actionLockCounter == 240) {
                Random rand = new Random();
                int i = rand.nextInt(100) + 1;
                // Updated to include 8 directions
                if (i <= 12) direction = "up";
                else if (i <= 24) direction = "down";
                else if (i <= 36) direction = "left";
                else if (i <= 48) direction = "right";
                else if (i <= 60) direction = "up_left";
                else if (i <= 72) direction = "up_right";
                else if (i <= 84) direction = "down_left";
                else direction = "down_right";

                if (rand.nextInt(100) < 30) {
                    isIdle = true;
                    spriteNum = 1;
                    idleCounter = 0;
                }

                actionLockCounter = 0;
            }
        }

    }
}
