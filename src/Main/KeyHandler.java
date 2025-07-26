package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed,enterPressed,ePressed;
    GamePanel gp;

    public KeyHandler(GamePanel gp) {
       this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code= e.getKeyCode();

        //Title state
        if(gp.gameState==gp.titleState){
            if(gp.ui.titleScreenState==0){
            if(code==KeyEvent.VK_W) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum>2){
                    gp.ui.commandNum=0;
                }
            }
            if(code==KeyEvent.VK_S) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum<0){
                    gp.ui.commandNum=2;
                }
            }
            if(code==KeyEvent.VK_ENTER) {
                switch (gp.ui.commandNum) {
                    case 0 -> {
                        gp.ui.titleScreenState=1;
                    }
                    case 1 -> {
                        System.out.println("Load Game");
                    }
                    case 2 -> {
                        System.exit(0);
                    }
                }
            }}
            else if(gp.ui.titleScreenState==1){
                if(code==KeyEvent.VK_W) {
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum>3){
                        gp.ui.commandNum=0;
                    }
                }
                if(code==KeyEvent.VK_S) {
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum<0){
                        gp.ui.commandNum=3;
                        gp.ui.commandNum=3;
                    }
                }
                if(code==KeyEvent.VK_ENTER) {
                    switch (gp.ui.commandNum) {
                        case 0 -> {

                            gp.stopMusic();
                            gp.gameState=gp.playState;
                            gp.playMusic(15);
                        }
                        case 1 -> {
                            gp.stopMusic();
                            gp.gameState=gp.playState;
                            gp.playMusic(15);
                        }
                        case 2 -> {
                            gp.stopMusic();
                            gp.gameState=gp.playState;
                            gp.playMusic(15);

                        }
                        case 3->{
                            System.out.println(gp.ui.commandNum);
                            gp.ui.titleScreenState=0;
                            gp.ui.commandNum=0;

                        }
                    }
                }
            }
        }

        //Play state
        if(gp.gameState==gp.playState){
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
        }
        //Pause state
       else if(gp.gameState==gp.pauseState) {
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;
            }
        }
        //Dialogue state
        else if(gp.gameState==gp.dialogueState){
            if(code==KeyEvent.VK_ENTER) {
                gp.gameState=gp.playState;
                gp.eventHandler.resetEvent();}


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
    }
}
