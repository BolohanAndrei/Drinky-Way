package Entity;

import Gambling.Dice;
import Main.GamePanel;

public class DiceGamberNPC extends Entity{

    public DiceGamberNPC(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1;
        entityType = 1;

        diceGame = new Dice(gp);

        getDiceGamberNPCImage();
    }

    public void getDiceGamberNPCImage() {
        try {
            down1 = setup("NPC/steal_down1");
            down2 = setup("NPC/steal_down2");

            up1 = down1;
            up2 = down2;
            left1 = down1;
            left2 = down2;
            right1 = down1;
            right2 = down2;
            idle_1 = down1;
            idle_2 = down2;
            idle_3 = down1;
            idle_4 = down2;
        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    @Override
    public void setAction() {
        direction = "down";
    }

    @Override
    public void update() {
        setAction();

        spriteCounter++;
        if (spriteCounter > 60) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    @Override
    public void speak() {
        gp.keyHandler.previousGameState = gp.gameState;
        gp.gameState = gp.diceGambleState;
        gp.ui.diceGame = this.diceGame;
        diceGame.startGame();
    }
}
