package Gambling;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BlackJack extends Gamble{
    GamePanel gp;
    public static final String name="BLACKJACK";
    BufferedImage[] img = new BufferedImage[14];

    public List<Integer> playerCards = new ArrayList<>();
    public List<Integer> dealerCards = new ArrayList<>();
    public boolean dealerCardHidden = true;
    public boolean playerTurn = false;
    public boolean gameOver = false;
    public boolean playerBust = false;
    public boolean dealerBust = false;
    public boolean doubleDown = false;
    public boolean canDoubleDown = true;

    public enum GameAction {
        NONE, HIT, STAND, DOUBLE_DOWN
    }
    public GameAction lastAction = GameAction.NONE;

    public int actionSelection = 0; // 0=Hit, 1=Stand, 2=Double Down
    public String[] playerActions = {"HIT", "STAND", "DOUBLE DOWN"};

    public boolean se = false;

    public BlackJack(GamePanel gp) {
        super(gp);
        this.gp = gp;
        getImage();
    }

    public void getImage(){
        for(int i = 0; i < 14; i++){
            img[i] = setup("gamble/card" + (i));
        }
    }

    public void startGame() {
        gamePhase = 0;
        commandNum = 0;
        selectedBetIndex = 0;
        betAmount = betOptions[selectedBetIndex];
        resetGame();
    }

    public void resetGame() {
        playerCards.clear();
        dealerCards.clear();
        dealerCardHidden = true;
        playerTurn = false;
        gameOver = false;
        playerBust = false;
        dealerBust = false;
        doubleDown = false;
        canDoubleDown = true;
        actionSelection = 0;
        lastAction = GameAction.NONE;
        playerSum = 0;
        opponentSum = 0;
        rollTimer = 0;
        displayTimer = 0;
        isRolling = false;
        dealingStep = 0;
    }

    public int dealingStep = 0;

    public void dealInitialCards() {
        switch (dealingStep) {
            case 0:
                dealerCards.add(getRandomCard());
                dealingStep++;
                rollTimer = 0;
                break;
            case 1:
                playerCards.add(getRandomCard());
                playerSum = calculateHandValue(playerCards);
                dealingStep++;
                rollTimer = 0;
                break;
            case 2:
                dealerCards.add(getRandomCard());
                dealingStep++;
                rollTimer = 0;
                break;
            case 3:
                playerCards.add(getRandomCard());
                dealingStep++;
                rollTimer = 0;

                playerSum = calculateHandValue(playerCards);

                if (playerSum == 21) {
                    gamePhase = 4;
                    gameOver = true;
                    dealerCardHidden = false;
                    opponentSum = calculateHandValue(dealerCards);
                } else {
                    playerTurn = true;
                }
                break;
        }
    }

    private int getRandomCard() {
        return (int) (Math.random() * 13 + 1);
    }

    public int calculateHandValue(List<Integer> cards) {
        int value = 0;
        int aces = 0;

        for (int card : cards) {
            if (card == 1) {
                aces++;
                value += 11;
            } else if (card >= 11) {
                value += 10;
            } else {
                value += card;
            }
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    public int calculateSingleCardValue(int card) {
        if (card == 1) {
            return 11;
        } else if (card >= 11) {
            return 10;
        } else {
            return card;
        }
    }

    public void playerHit() {
        if (playerTurn && !gameOver) {
            playerCards.add(getRandomCard());
            playerSum = calculateHandValue(playerCards);
            canDoubleDown = false;

            if (playerSum > 21) {
                playerBust = true;
                gameOver = true;
                playerTurn = false;
                gamePhase = 4;
            } else if (doubleDown) {
                playerStand();
            }
        }
    }

    public void playerStand() {
        if (playerTurn && !gameOver) {
            playerTurn = false;
            dealerCardHidden = false;
            opponentSum = calculateHandValue(dealerCards);
            gamePhase = 3;
            rollTimer = 0;
        }
    }

    public void playerDoubleDown() {
        if (playerTurn && !gameOver && canDoubleDown && gp.player.coin >= betAmount) {
            doubleDown = true;
            betAmount *= 2;
            playerHit();
        }
    }

    public void dealerPlay() {
        opponentSum = calculateHandValue(dealerCards);

        if (opponentSum < 17) {
            dealerCards.add(getRandomCard());
            opponentSum = calculateHandValue(dealerCards);
            rollTimer = 0;

            if (opponentSum > 21) {
                dealerBust = true;
                gameOver = true;
                gamePhase = 4;
            }
        } else {
            gameOver = true;
            gamePhase = 4;
        }
    }

    @Override
    public void update() {
        if (gamePhase == 1) {
            rollTimer++;
            if (rollTimer >= 60) {
                if (dealingStep < 4) {
                    dealInitialCards();
                } else {
                    if (rollTimer >= 120) {
                        if (!gameOver) {
                            gamePhase = 2;
                        }
                        rollTimer = 0;
                    }
                }
            }
        } else if (gamePhase == 2) {
        } else if (gamePhase == 3) {
            rollTimer++;
            if (rollTimer >= 120) {
                dealerPlay();
            }
        } else if (gamePhase == 4) {
            displayTimer++;
            if (displayTimer >= 300) {
                handleGameResult();
                gamePhase = 0;
                commandNum = 0;
                resetGame();
            }
        }
    }

    public void handlePlayerInput(String key) {
        if (gamePhase == 2 && playerTurn) {
            if (key.equals("w") || key.equals("up")) {
                actionSelection = (actionSelection - 1 + playerActions.length) % playerActions.length;
            } else if (key.equals("s") || key.equals("down")) {
                actionSelection = (actionSelection + 1) % playerActions.length;
            } else if (key.equals("enter")) {
                switch (actionSelection) {
                    case 0:
                        playerHit();
                        break;
                    case 1:
                        playerStand();
                        break;
                    case 2:
                        if (canDoubleDown && gp.player.coin >= betAmount) {
                            playerDoubleDown();
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void handleGameResult() {
        if (playerBust) {
            dealerCardHidden = false;
            opponentSum = calculateHandValue(dealerCards);
            gp.player.coin -= betAmount;
            gp.ui.addMessage("BUST! Ye lost! -" + betAmount + " coins");
        } else if (dealerBust) {
            gp.player.coin += betAmount;
            gp.ui.addMessage("Dealer BUST! Ye won! +" + betAmount + " coins");
        } else if (playerSum == 21 && playerCards.size() == 2 && opponentSum != 21) {
            int winnings = (int)(betAmount * 1.5);
            gp.player.coin += winnings;
            gp.ui.addMessage("BLACKJACK! Ye won! +" + winnings + " coins");
        } else if (playerSum > opponentSum) {
            gp.player.coin += betAmount;
            gp.ui.addMessage("Ye won! +" + betAmount + " coins");
        } else if (playerSum < opponentSum) {
            gp.player.coin -= betAmount;
            gp.ui.addMessage("Ye lost! -" + betAmount + " coins");
        } else {
            gp.ui.addMessage("Push! It's a tie!");
        }

        if (doubleDown) {
            betAmount /= 2;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
        g2.setColor(Color.WHITE);

        if (gamePhase == 0) {
            drawBetScreen(g2, name);
        } else {
            drawGameScreen(g2);
        }
    }

    public void drawGameScreen(Graphics2D g2) {
        String title = "";
        if (gamePhase == 1) {
            title = "DEALIN' CARDS...";
        } else if (gamePhase == 2) {
            title = "YER TURN";
        } else if (gamePhase == 3) {
            title = "DEALER'S TURN";
        } else if (gamePhase == 4) {
            if (playerBust) {
                title = "BUST!";
            } else if (dealerBust) {
                title = "DEALER BUST!";
            } else if (playerSum == 21 && playerCards.size() == 2) {
                title = "BLACKJACK!";
            } else {
                title = "FINAL RESULTS";
            }
        }

        int x = getXforCenteredText(title, g2);
        int y = gp.tileSize;
        g2.drawString(title, x, y);

        g2.setFont(g2.getFont().deriveFont(20f));
        String betText = "Bet: " + betAmount + " coins";
        if (doubleDown) {
            betText += " (DOUBLED)";
        }
        x = getXforCenteredText(betText, g2);
        y += 40;
        g2.drawString(betText, x, y);

        y += 60;
        g2.setFont(g2.getFont().deriveFont(24f));
        g2.setColor(Color.WHITE);
        g2.drawString("DEALER", getXforCenteredText("DEALER", g2), y);
        y += 20;

        int cardX = gp.screenWidth / 2 - (dealerCards.size() * 35);
        for (int i = 0; i < dealerCards.size(); i++) {
            if (i == 0 && dealerCardHidden) {
                drawCard(g2, cardX, y, 0);
            } else {
                drawCard(g2, cardX, y, dealerCards.get(i));
            }
            cardX += 70;
        }
        y+=30;

        if (!dealerCardHidden || gamePhase >= 4) {
            g2.setFont(g2.getFont().deriveFont(20f));
            g2.drawString("Value: " + opponentSum, getXforCenteredText("Value: " + opponentSum, g2), y + 80);
        } else if(dealerCardHidden && dealerCards.size() >= 2) {
            g2.setFont(g2.getFont().deriveFont(20f));
            int visibleCardValue = calculateSingleCardValue(dealerCards.get(1));
            g2.drawString("Value: " + visibleCardValue + " + ?", getXforCenteredText("Value: " + visibleCardValue + " + ?", g2), y + 80);
        }

        y += 140;
        g2.setFont(g2.getFont().deriveFont(24f));
        g2.setColor(Color.WHITE);
        g2.drawString("PLAYER", getXforCenteredText("PLAYER", g2), y);
        y += 20;

        cardX = gp.screenWidth / 2 - (playerCards.size() * 35);
        for (int i = 0; i < playerCards.size(); i++) {
            drawCard(g2, cardX, y, playerCards.get(i));
            cardX += 70;
        }

        g2.setFont(g2.getFont().deriveFont(20f));
        g2.drawString("Value: " + playerSum, getXforCenteredText("Value: " + playerSum, g2), y + 110);

        if (gamePhase == 2 && playerTurn) {
            y += 150;
            g2.setFont(g2.getFont().deriveFont(18f));
            for (int i = 0; i < playerActions.length; i++) {
                if (i == 2 && (!canDoubleDown || gp.player.coin < betAmount)) {
                    g2.setColor(Color.GRAY);
                } else if (i == actionSelection) {
                    g2.setColor(Color.YELLOW);
                } else {
                    g2.setColor(Color.WHITE);
                }
                String action = playerActions[i];
                if (i == actionSelection) {
                    action = "> " + action + " <";
                }
                g2.drawString(action, getXforCenteredText(action, g2), y + i * 30);
            }
        }

        if (gamePhase == 4) {
            y += 170;
            String result = "";
            if (playerBust) {
                g2.setColor(Color.RED);
                result = "YE BUST! DEALER WINS!";
            } else if (dealerBust) {
                g2.setColor(Color.GREEN);
                result = "DEALER BUST! YE WIN!";
            } else if (playerSum == 21 && playerCards.size() == 2 && opponentSum != 21) {
                g2.setColor(Color.YELLOW);
                result = "BLACKJACK! YE WIN!";
            } else if (playerSum > opponentSum) {
                g2.setColor(Color.GREEN);
                result = "YE WIN!";
            } else if (playerSum < opponentSum) {
                g2.setColor(Color.RED);
                result = "YE LOSE!";
            } else {
                g2.setColor(Color.YELLOW);
                result = "PUSH! IT'S A TIE!";
            }
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28f));
            g2.drawString(result, getXforCenteredText(result, g2), y);
        }
    }

    private void drawCard(Graphics2D g2, int x, int y, int value) {
        if (value >= 0 && value < img.length && img[value] != null) {
            g2.drawImage(img[value], x, y, 60, 80, null);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, 60, 80);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, 60, 80);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            if (value == 0) {
                g2.drawString("?", x + 25, y + 45);
            } else {
                String cardText = getCardDisplayText(value);
                g2.drawString(cardText, x + 5, y + 45);
            }
        }
    }

    private String getCardDisplayText(int value) {
        switch (value) {
            case 1: return "A";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return String.valueOf(value);
        }
    }
}
