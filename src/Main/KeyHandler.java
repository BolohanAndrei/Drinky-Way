package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyHandler implements KeyListener, MouseListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed,enterPressed,ePressed;
    public boolean attackClicked,shotKeyPressed;
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

        //Title state
        if(gp.gameState==gp.titleState){
            titleState(code);
        }
        //Play state
        if(gp.gameState==gp.playState){
            playState(code);
        }
        //Pause state
      else if(gp.gameState==gp.pauseState) {
            pauseState(code);
        }
        //Dialogue state
       else if(gp.gameState==gp.dialogueState){
            dialogueState(code);
        }
        //Character state
       else if(gp.gameState==gp.characterState){
            characterState(code);
        }
    }

    public void titleState(int code) {
        if (gp.ui.titleScreenState == 0) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                switch (gp.ui.commandNum) {
                    case 0 -> gp.ui.titleScreenState = 1;
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
        if(code==KeyEvent.VK_ESCAPE) {
            gp.gameState=gp.pauseState;
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
    public void pauseState(int code){
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
    }
    public void dialogueState(int code){
        if(code==KeyEvent.VK_E) {
            gp.gameState = gp.playState;
        }
    }
    public void characterState(int code) {
        if (code == KeyEvent.VK_TAB) {
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_W) {
            if (gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
                gp.sound.playSE(19);
            }
            else{
                gp.ui.slotRow = 3;
                gp.sound.playSE(19);
            }
        }
        if (code == KeyEvent.VK_S) {
            if (gp.ui.slotRow != 3) {
                gp.ui.slotRow++;
                gp.sound.playSE(19);
            }
            else{
                gp.ui.slotRow = 0;
                gp.sound.playSE(19);
            }
        }
        if (code == KeyEvent.VK_A) {
            if (gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
                gp.sound.playSE(19);
            }
            else{
                gp.ui.slotCol = 4;
                gp.sound.playSE(19);
            }
        }
        if (code == KeyEvent.VK_D) {
            if (gp.ui.slotCol != 4) {
                gp.ui.slotCol++;
                gp.sound.playSE(19);
            }else{
                gp.ui.slotCol = 0;
                gp.sound.playSE(19);
            }
        }
        if(code==KeyEvent.VK_ENTER) {
            gp.player.selectItem();
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
