package Entity;

import Gambling.BlackJack;
import Main.GamePanel;

public class BlackJackGamblerNPC extends Entity {
    public BlackJackGamblerNPC(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1;
        entityType = 1;

        blackJackGame = new BlackJack(gp);

        getBlackJackGamblerNPCImage();
    }

    public void getBlackJackGamblerNPCImage() {
        try {
            down1 = setup("NPC/bald");
            down2 = down1;

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
        gp.gameState = gp.blackJackGambleState;
        gp.ui.blackJackGame = this.blackJackGame;
        blackJackGame.startGame();
    }
}
