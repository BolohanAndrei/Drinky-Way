package Gambling;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Dice extends Gamble{
    GamePanel gp;
    public static final String name="DICE O’ FATE";;
    BufferedImage[] img = new BufferedImage[6];

    public int playerDice1 = 1, playerDice2 = 1;
    public int opponentDice1 = 1, opponentDice2 = 1;

    public boolean se=false;

    public Dice(GamePanel gp) {
        super(gp);
        this.gp = gp;
        getImage();
    }

    public void getImage(){
        for(int i = 0; i < 6; i++){
            img[i] = setup("gamble/dice" + (i + 1));
        }
    }

    public void startGame() {
        gamePhase = 0;
        commandNum = 0;
        selectedBetIndex = 0;
        betAmount = betOptions[selectedBetIndex];
        resetDice();
    }

    public void resetDice() {
        playerDice1 = 1;
        playerDice2 = 1;
        opponentDice1 = 1;
        opponentDice2 = 1;
        playerSum = 0;
        opponentSum = 0;
        rollTimer = 0;
        displayTimer = 0;
        isRolling = false;
    }

    @Override
    public void update() {
        if (gamePhase == 1) {
            if (isRolling) {
                rollTimer++;
                if (rollTimer < 300) {
                    if (rollTimer % 10 == 0) {
                        if(!se){
                            gp.se.playSE(41);
                            se=true;
                        }
                        playerDice1 = (int) (Math.random() * 6 + 1);
                        playerDice2 = (int) (Math.random() * 6 + 1);
                    }
                } else {
                    playerDice1 = (int) (Math.random() * 6 + 1);
                    playerDice2 = (int) (Math.random() * 6 + 1);
                    playerSum = playerDice1 + playerDice2;
                    isRolling = false;
                    gp.se.stop();
                    se=false;
                    displayTimer = 0;
                    gamePhase = 2;
                }
            }
        } else if (gamePhase == 2) {
            displayTimer++;
            if (displayTimer >= 300) {
                gamePhase = 3;
                rollTimer = 0;
                isRolling = true;
            }
        } else if (gamePhase == 3) {
            if (isRolling) {
                rollTimer++;
                if (rollTimer < 240) {
                    if (rollTimer % 10 == 0) {
                        if(!se){
                            gp.se.playSE(41);
                            se=true;
                        }
                        opponentDice1 = (int) (Math.random() * 6 + 1);
                        opponentDice2 = (int) (Math.random() * 6 + 1);
                    }
                } else {
                    opponentDice1 = (int) (Math.random() * 6 + 1);
                    opponentDice2 = (int) (Math.random() * 6 + 1);
                    opponentSum = opponentDice1 + opponentDice2;
                    se=false;
                    gp.se.stop();
                    isRolling = false;
                    displayTimer = 0;
                    gamePhase = 4;
                }
            }
        } else if (gamePhase == 4) {
            displayTimer++;
            if (displayTimer >= 240) {
                handleGameResult();
                gamePhase = 0;
                commandNum = 0;
                resetDice();
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
        g2.setColor(Color.WHITE);

        if (gamePhase == 0) {
            drawBetScreen(g2,name);
        } else {
            drawGameScreen(g2);
        }
    }

    public void drawGameScreen(Graphics2D g2) {
        String title = "";
        if (gamePhase == 1) {
            title = "PLAYER ROLLIN'...";
        } else if (gamePhase == 2) {
            title = "PLAYER RESULT: " + playerSum;
        } else if (gamePhase == 3) {
            title = "OPPONENT ROLLIN;...";
        } else if (gamePhase == 4) {
            title = "FINAL BOOTY";
        }

        int x = getXforCenteredText(title, g2);
        int y = gp.tileSize * 2;
        g2.drawString(title, x, y);

        g2.setFont(g2.getFont().deriveFont(20f));
        String betText = "Bet: " + betAmount + " coins";
        x = getXforCenteredText(betText, g2);
        y += 50;
        g2.drawString(betText, x, y);

        y += 80;
        g2.setFont(g2.getFont().deriveFont(24f));
        g2.drawString("PLAYER", getXforCenteredText("PLAYER", g2), y);
        y += 40;

        if (gamePhase >= 1) {
            drawDice(g2, gp.screenWidth / 2 - 80, y, playerDice1);
            drawDice(g2, gp.screenWidth / 2 + 20, y, playerDice2);

            if (gamePhase >= 2) {
                g2.drawString("Sum: " + playerSum, getXforCenteredText("Sum: " + playerSum, g2), y + 100);
            }
        }

        if (gamePhase >= 3) {
            y += 130;
            g2.drawString("OPPONENT", getXforCenteredText("OPPONENT", g2), y);
            y += 30;

            drawDice(g2, gp.screenWidth / 2 - 80, y, opponentDice1);
            drawDice(g2, gp.screenWidth / 2 + 20, y, opponentDice2);

            if (gamePhase >= 4) {
                g2.drawString("Sum: " + opponentSum, getXforCenteredText("Sum: " + opponentSum, g2), y + 100);

                y += 160;
                String result = "";
                if (playerSum > opponentSum) {
                    g2.setColor(Color.GREEN);
                    result = "YE WIN!";
                } else if (playerSum < opponentSum) {
                    g2.setColor(Color.RED);
                    result = "YE LOSE!";
                } else {
                    g2.setColor(Color.YELLOW);
                    result = "TIE!";
                }
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
                g2.drawString(result, getXforCenteredText(result, g2), y);
            }
        }
    }

    private void drawDice(Graphics2D g2, int x, int y, int value) {
        if (img[value - 1] != null) {
            g2.drawImage(img[value - 1], x, y, 60, 60, null);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, 60, 60);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, 60, 60);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
            g2.drawString(String.valueOf(value), x + 25, y + 35);
        }
    }
}
