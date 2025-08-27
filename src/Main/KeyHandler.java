package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyHandler implements KeyListener, MouseListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed,enterPressed,ePressed;
    public boolean attackClicked,shotKeyPressed;
    public int previousGameState;
    GamePanel gp;

    public KeyHandler(GamePanel gp) {
       this.gp = gp;
       gp.addMouseListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code= e.getKeyCode();

        if(code==KeyEvent.VK_ESCAPE){
            if(gp.gameState==gp.optionState){
                gp.gameState = previousGameState;
            }
            else if(gp.gameState==gp.gameOverState){
                previousGameState = gp.gameOverState;
                gp.gameState=gp.optionState;
            }
            else if(gp.gameState==gp.playState){
                previousGameState = gp.playState;
                gp.gameState=gp.optionState;
            }
            else if(gp.gameState==gp.titleState){
                previousGameState = gp.titleState;
                gp.gameState=gp.optionState;
            }
            else if(gp.gameState==gp.characterState){
                previousGameState = gp.characterState;
                gp.gameState=gp.optionState;
            }
            else if(gp.gameState==gp.tradeState){
                previousGameState = gp.tradeState;
                gp.gameState=gp.optionState;
            }
        }

        //Title state
        if(gp.gameState==gp.titleState){
            titleState(code);
        }
        //Play state
        if(gp.gameState==gp.playState){
            playState(code);
        }
        //Dialogue state
       else if(gp.gameState==gp.dialogueState){
            dialogueState(code);
        }
        //Character state
       else if(gp.gameState==gp.characterState){
            characterState(code);
        }
       //Option state
       else if(gp.gameState==gp.optionState){
           optionState(code);
        }
       //Game over state
        else if(gp.gameState==gp.gameOverState){
            gameOverState(code);
        }
        else if(gp.gameState==gp.tradeState){
            tradeState(code);
        }
    }

    public void titleState(int code) {
        if (gp.ui.titleScreenState == 0) {
            if (code == KeyEvent.VK_W) {
                gp.se.playSE(19);
                gp.ui.commandNum--;
                if (gp.ui.commandNum <0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.se.playSE(19);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                switch (gp.ui.commandNum) {
                    case 0 -> {
                        gp.ui.titleScreenState = 1;
                    }
                    case 1 -> System.out.println("Load Game");
                    case 2 -> System.exit(0);
                }
            }
        }
    }
    public void playState(int code){
        if(code==KeyEvent.VK_W) {
            upPressed=true;
        }
        if(code==KeyEvent.VK_S) {
            downPressed=true;
        }
        if(code==KeyEvent.VK_A) {
            leftPressed=true;
        }
        if(code==KeyEvent.VK_D) {
            rightPressed=true;
        }
        if(code==KeyEvent.VK_ENTER) {
            enterPressed=true;
        }
        if(code==KeyEvent.VK_E) {
            ePressed = true;
        }
        if(code==KeyEvent.VK_TAB) {
            gp.gameState=gp.characterState;
        }
    }
    public void optionState(int code){
        if(code == KeyEvent.VK_ENTER) {
            enterPressed=true;
        }
        if(gp.ui.subState == 0) {
            if (code == KeyEvent.VK_W) {
                gp.se.playSE(19);
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 4;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.se.playSE(19);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 4) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_A) {
                if(gp.ui.commandNum==0 && gp.music.volumeScale>0){
                    gp.music.volumeScale--;
                    gp.se.playSE(19);
                    gp.music.checkVolume();
                }
                if(gp.ui.commandNum==1 && gp.se.volumeScale>0){
                    gp.se.volumeScale--;
                    gp.se.playSE(19);
                }
            }
            if (code == KeyEvent.VK_D) {
                if(gp.ui.commandNum==0 && gp.music.volumeScale<5){
                    gp.music.volumeScale++;
                    gp.se.playSE(19);
                    gp.music.checkVolume();
                }
                if(gp.ui.commandNum==1 && gp.se.volumeScale<5){
                    gp.se.volumeScale++;
                    gp.se.playSE(19);
                }
            }
        }
        else if(gp.ui.subState == 1) {
            if (code == KeyEvent.VK_W) {
                gp.se.playSE(19);
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0 ) {
                    gp.ui.commandNum =gp.ui.controlLabels.length-1;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.se.playSE(19);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > gp.ui.controlLabels.length-1) {
                    gp.ui.commandNum = 0;
                }
            }
        }
        else if(gp.ui.subState == 2) {
            if (code == KeyEvent.VK_W) {
                gp.se.playSE(19);
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.se.playSE(19);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }
        }
    }
    public void dialogueState(int code){
        if(code==KeyEvent.VK_E) {
            if(previousGameState == gp.tradeState){
                gp.gameState = gp.tradeState;
                gp.ui.subState = 0;
                gp.ui.commandNum = 0;
            } else {
                gp.gameState = gp.playState;
            }
            gp.keyHandler.enterPressed = false;
        }
    }
    public void characterState(int code) {
        if (code == KeyEvent.VK_TAB) {
            gp.gameState = gp.playState;
        }
        playerInventory(code);
        if(code==KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
    }
    public void gameOverState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed=true;
        }
        if(gp.gameState==gp.gameOverState){
            if (code == KeyEvent.VK_W) {
                gp.se.playSE(19);
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.se.playSE(19);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }
        }
    }
    public void tradeState(int code) {
        if(code==KeyEvent.VK_E) {
            if(gp.ui.subState==0) {
                gp.gameState = gp.playState;
                gp.ui.commandNum=0;
                enterPressed=false;
            }else{
                gp.ui.subState = 0;
            }
            return;
        }
        if(code==KeyEvent.VK_ENTER) {
            enterPressed=true;
        }
        if(gp.ui.subState == 0) {
            if (code == KeyEvent.VK_W) {
                gp.se.playSE(19);
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.se.playSE(19);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }
        }
        if(gp.ui.subState == 1) {
            tradeInventory(code);
        }
        if(gp.ui.subState == 2) {
            playerInventory(code);
        }
    }
    public void playerInventory(int code){
        if (code == KeyEvent.VK_W) {
            if (gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
                gp.se.playSE(19);
            }
            else{
                gp.ui.slotRow = 3;
                gp.se.playSE(19);
            }
        }
        if (code == KeyEvent.VK_S) {
            if (gp.ui.slotRow != 3) {
                gp.ui.slotRow++;
                gp.se.playSE(19);
            }
            else{
                gp.ui.slotRow = 0;
                gp.se.playSE(19);
            }
        }
        if (code == KeyEvent.VK_A) {
            if (gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
                gp.se.playSE(19);
            }
            else{
                gp.ui.slotCol = 4;
                gp.se.playSE(19);
            }
        }
        if (code == KeyEvent.VK_D) {
            if (gp.ui.slotCol != 4) {
                gp.ui.slotCol++;
                gp.se.playSE(19);
            }else{
                gp.ui.slotCol = 0;
                gp.se.playSE(19);
            }
        }
    }
    public void tradeInventory(int code){
        if (code == KeyEvent.VK_W) {
            if (gp.ui.tradeSlotRow != 0) {
                gp.ui.tradeSlotRow--;
                gp.se.playSE(19);
            }
            else{
                gp.ui.tradeSlotRow = 3;
                gp.se.playSE(19);
            }
        }
        if (code == KeyEvent.VK_S) {
            if (gp.ui.tradeSlotRow != 3) {
                gp.ui.tradeSlotRow++;
                gp.se.playSE(19);
            }
            else{
                gp.ui.tradeSlotRow = 0;
                gp.se.playSE(19);
            }
        }
        if (code == KeyEvent.VK_A) {
            if (gp.ui.tradeSlotCol != 0) {
                gp.ui.tradeSlotCol--;
                gp.se.playSE(19);
            }
            else{
                gp.ui.tradeSlotCol = 4;
                gp.se.playSE(19);
            }
        }
        if (code == KeyEvent.VK_D) {
            if (gp.ui.tradeSlotCol != 4) {
                gp.ui.tradeSlotCol++;
                gp.se.playSE(19);
            }else{
                gp.ui.tradeSlotCol = 0;
                gp.se.playSE(19);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code= e.getKeyCode();
        if(code==KeyEvent.VK_W) {
            upPressed=false;
        }
        if(code==KeyEvent.VK_S) {
            downPressed=false;
        }
        if(code==KeyEvent.VK_A) {
            leftPressed=false;
        }
        if(code==KeyEvent.VK_D) {
            rightPressed=false;
        }
        if(code==KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if(code==KeyEvent.VK_E) {
            ePressed = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gp.gameState == gp.playState) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                attackClicked = true; // left click
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                shotKeyPressed = true; // right click
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            attackClicked = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            shotKeyPressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
