package Gambling;

import Entity.Entity;
import Main.GamePanel;

import java.awt.*;

public class Gamble extends Entity {
    public int[] betOptions = {10, 25, 50, 100, 250};
    public int selectedBetIndex = 0;
    public int playerSum = 0, opponentSum = 0;
    public int betAmount = 10;
    public int rollTimer = 0;
    public int displayTimer = 0;
    public boolean isRolling = false;
    public int gamePhase = 0;
    public int commandNum = 0;
    public Gamble(GamePanel gp) {
        super(gp);
    }

    public void drawBetScreen(Graphics2D g2,String name) {
        String text = name;
        int x = getXforCenteredText(text, g2);
        int y = gp.tileSize*2;
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(24f));
        text = "Place yer wager:";
        x = getXforCenteredText(text, g2);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);

        for (int i = 0; i < betOptions.length; i++) {
            if (i == selectedBetIndex) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> " + betOptions[i] + " coins <", getXforCenteredText(betOptions[i] + " coins", g2) - 30, y + (i + 1) * 40);
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString(betOptions[i] + " coins", getXforCenteredText(betOptions[i] + " coins", g2), y + (i + 1) * 40);
            }
        }

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(16f));
        text = "Yer booty: " + gp.player.coin;
        x = getXforCenteredText(text, g2);
        y += (betOptions.length + 2) * 40;
        g2.drawString(text, x, y);

        text = "Use W/S to select, ENTER to confirm, E to exit";
        x = getXforCenteredText(text, g2);
        y += 40;
        g2.drawString(text, x, y);
    }
    public void handleGameResult() {
        if (playerSum > opponentSum) {
            gp.player.coin += betAmount;
            gp.ui.addMessage("Ye won! +" + betAmount + " coins");
        } else if (playerSum < opponentSum) {
            gp.player.coin -= betAmount;
            gp.ui.addMessage("Ye lost! -" + betAmount + " coins");
        } else {
            gp.ui.addMessage("Draw...");
        }
    }

    public void confirmBet() {
        if (gp.player.coin >= betAmount) {
            gamePhase = 1;
            isRolling = true;
            rollTimer = 0;
        } else {
            gp.ui.addMessage("Too few coins, landlubber!");
        }
    }

    public void increaseBet() {
        selectedBetIndex = (selectedBetIndex + 1) % betOptions.length;
        betAmount = betOptions[selectedBetIndex];
    }

    public void decreaseBet() {
        selectedBetIndex = (selectedBetIndex - 1 + betOptions.length) % betOptions.length;
        betAmount = betOptions[selectedBetIndex];
    }

    public int getXforCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}
