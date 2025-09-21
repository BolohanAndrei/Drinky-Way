package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyHandler implements KeyListener, MouseListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed,enterPressed,ePressed,spacePressed,qPressed;
    public boolean attackClicked,shotKeyPressed;
    public int previousGameState;
    GamePanel gp;

    public KeyHandler(GamePanel gp) {
       this.gp = gp;
       gp.addMouseListener(this);
       previousGameState = gp.playState;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code= e.getKeyCode();
        if(code==KeyEvent.VK_SPACE){
            spacePressed = true;
        }
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
            else if(gp.gameState==gp.chestState){
                previousGameState = gp.chestState;
                gp.gameState=gp.optionState;
            }
            else if(gp.gameState==gp.diceGambleState){
                previousGameState = gp.diceGambleState;
                gp.gameState = gp.optionState;
            }
            else if(gp.gameState==gp.sleepState){
                previousGameState = gp.sleepState;
                gp.gameState=gp.optionState;
            }
            else if(gp.gameState==gp.mapState){
                previousGameState = gp.mapState;
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
        //Trade State
        else if(gp.gameState==gp.tradeState){
            tradeState(code);
        }
        //Chest State
        else if(gp.gameState==gp.chestState){
            chestState(code);
        }
        //Dice Gambling State
        else if(gp.gameState==gp.diceGambleState){
            diceGambleState(code);
        }
        //BlackJack Gambling State
        else if(gp.gameState==gp.blackJackGambleState){
            blackJackGambleState(code);
        }
        //Map Stat
        else if(gp.gameState==gp.mapState){
            mapState(code);
        }
        //Save State
        else if(gp.gameState==gp.saveState){
            saveState(code);
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
                    case 0 -> gp.ui.titleScreenState = 1;
                    case 1 -> {
                        gp.saveload.load();
                        gp.music.stop();
                        gp.gameState = gp.playState;
                        gp.music.playMusic(15);
                    }
                    case 2 -> System.exit(0);
                }
            }
        }
    }
    public void playState(int code){
        if(code==KeyEvent.VK_Q){
            qPressed = true;
        }
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
        if(code==KeyEvent.VK_M) {
            gp.gameState=gp.mapState;
        }
        if(code==KeyEvent.VK_N) {
            gp.map.miniMapOn= !gp.map.miniMapOn;
        }
        if(code==KeyEvent.VK_F3){
            gp.toggleDebugHitboxes();
        }
    }
    public void optionState(int code){
        if(code == KeyEvent.VK_ENTER) {
            enterPressed=true;
        }
        if(gp.ui.subState == 0) {
            int max = gp.ui.getOptionMenuItemCount() - 1;
            if (code == KeyEvent.VK_W) {
                gp.se.playSE(19);
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = max;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.se.playSE(19);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > max) {
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
        if(gp.gameState!=gp.dialogueState){
            previousGameState = gp.gameState;
            gp.gameState=gp.dialogueState;
        }
        if(code==KeyEvent.VK_E) {
            if(previousGameState == gp.tradeState){
                gp.gameState = gp.playState;
                gp.ui.subState = 0;
                gp.ui.commandNum = 0;
            } else {
                ePressed = true;
            }
            enterPressed = false;
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
    public void chestInventory(int code){
        if (code == KeyEvent.VK_W) {
            if (gp.ui.chestSlotRow != 0) {
                gp.ui.chestSlotRow--;
                gp.se.playSE(19);
            }
            else{
                gp.ui.chestSlotRow = 3;
                gp.se.playSE(19);
            }
        }
        if (code == KeyEvent.VK_S) {
            if (gp.ui.chestSlotRow != 3) {
                gp.ui.chestSlotRow++;
                gp.se.playSE(19);
            }
            else{
                gp.ui.chestSlotRow = 0;
                gp.se.playSE(19);
            }
        }
        if (code == KeyEvent.VK_A) {
            if (gp.ui.chestSlotCol != 0) {
                gp.ui.chestSlotCol--;
                gp.se.playSE(19);
            }
            else{
                gp.ui.chestSlotCol = 4;
                gp.se.playSE(19);
            }
        }
        if (code == KeyEvent.VK_D) {
            if (gp.ui.chestSlotCol != 4) {
                gp.ui.chestSlotCol++;
                gp.se.playSE(19);
            }else{
                gp.ui.chestSlotCol = 0;
                gp.se.playSE(19);
            }
        }
    }
    public void chestState(int code) {
        if(code==KeyEvent.VK_E) {

            gp.gameState = gp.playState;
            enterPressed = false;
            ePressed = false;
            gp.ui.chest.down1=gp.ui.chest.image1;
            gp.se.playSE(32);
            return;
        }

        if(code==KeyEvent.VK_ENTER) {
            enterPressed=true;
        }
        if(gp.ui.chestFocusPlayer ){
            playerInventory(code);
        }else{
            chestInventory(code);
        }

    }
    public void mapState(int code) {
        if(code==KeyEvent.VK_M) {
            gp.gameState = gp.playState;
        }
    }
    public void saveState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed=true;
        }
        if(gp.gameState==gp.saveState){
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
    public void diceGambleState(int code) {
        if (gp.ui.diceGame != null) {
            if (gp.ui.diceGame.gamePhase == 0) {
                if(code == KeyEvent.VK_E) {
                    gp.gameState = gp.playState;
                }
                if (code == KeyEvent.VK_W) {
                    gp.se.playSE(19);
                    gp.ui.diceGame.decreaseBet();
                }
                if (code == KeyEvent.VK_S) {
                    gp.se.playSE(19);
                    gp.ui.diceGame.increaseBet();
                }
                if (code == KeyEvent.VK_ENTER) {
                    gp.se.playSE(19);
                    gp.ui.diceGame.confirmBet();
                }
            }
        }
    }
    public void blackJackGambleState(int code) {
        if (gp.ui.blackJackGame != null) {
            if (gp.ui.blackJackGame.gamePhase == 0) {
                // Bet selection phase
                if(code == KeyEvent.VK_E) {
                    gp.gameState = gp.playState;
                }
                if (code == KeyEvent.VK_W) {
                    gp.se.playSE(19);
                    gp.ui.blackJackGame.decreaseBet();
                }
                if (code == KeyEvent.VK_S) {
                    gp.se.playSE(19);
                    gp.ui.blackJackGame.increaseBet();
                }
                if (code == KeyEvent.VK_ENTER) {
                    gp.se.playSE(19);
                    gp.ui.blackJackGame.confirmBet();
                }
            } else if (gp.ui.blackJackGame.gamePhase == 2) {
                // Player's turn - handle action selection
                if (code == KeyEvent.VK_W) {
                    gp.se.playSE(19);
                    gp.ui.blackJackGame.handlePlayerInput("w");
                }
                if (code == KeyEvent.VK_S) {
                    gp.se.playSE(19);
                    gp.ui.blackJackGame.handlePlayerInput("s");
                }
                if (code == KeyEvent.VK_ENTER) {
                    gp.se.playSE(19);
                    gp.ui.blackJackGame.handlePlayerInput("enter");
                }
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
        if(code==KeyEvent.VK_SPACE){
            spacePressed = false;
        }
        if(code==KeyEvent.VK_Q){
            qPressed=false;
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
