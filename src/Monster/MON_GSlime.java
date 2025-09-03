package Monster;

import Main.GamePanel;
import object.*;

public class MON_GSlime extends BaseMonster {

    public MON_GSlime(GamePanel gp) {
        super(gp);
        setupSlimeProperties();
        getImage();
        initAdvancedAnimations();
    }

    private void setupSlimeProperties() {
        name = "Green Slime";
        speed = 1;
        maxHealth = 4;
        health = maxHealth;
        attack = 3;
        defense = 0;
        exp = 2;
        projectile = new Obj_SlimeProjectile(gp);

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    protected AIConfig createAIConfig() {
        AIConfig config = new AIConfig();
        config.style = CombatStyle.HYBRID;

        config.aggroRange = 6;
        config.disengageRange = 18;
        config.lineOfSightRange = 12;

        config.aggroChance = 75;
        config.disengageChance = 35;
        config.patrolAggroChance = 65;

        config.meleeAttackChance = 90;
        config.rangedAttackChance = 95;
        config.meleeAttackCooldown = 18;
        config.rangedAttackCooldown = 25;
        config.meleeRangeTiles = 1;

        config.stuckThreshold = 60;
        config.noMovementThreshold = 20;
        config.crowdAvoidanceChance = 30;

        return config;
    }

    @Override
    protected void initAdvancedAnimations() {
        java.awt.image.BufferedImage[] up = {up1, up2};
        java.awt.image.BufferedImage[] down = {down1, down2};
        java.awt.image.BufferedImage[] left = {left1, left2};
        java.awt.image.BufferedImage[] right = {right1, right2};
        assignWalkFrames(0, up);
        assignWalkFrames(1, down);
        assignWalkFrames(2, left);
        assignWalkFrames(3, right);

        assignIdleFrames(0, new java.awt.image.BufferedImage[]{up1});
        assignIdleFrames(1, new java.awt.image.BufferedImage[]{down1});
        assignIdleFrames(2, new java.awt.image.BufferedImage[]{left1});
        assignIdleFrames(3, new java.awt.image.BufferedImage[]{right1});

        assignAttackFrames(0, up);
        assignAttackFrames(1, down);
        assignAttackFrames(2, left);
        assignAttackFrames(3, right);
    }

    @Override
    public void getImage() {
        up1 = setup("monsters/Slime_left1");
        up2 = setup("monsters/Slime_right1");
        down1 = setup("monsters/Slime_left1");
        down2 = setup("monsters/Slime_right1");
        left1 = setup("monsters/Slime_left1");
        left2 = setup("monsters/Slime_left2");
        right1 = setup("monsters/Slime_right1");
        right2 = setup("monsters/Slime_right2");
        upLeft1 = up1; upLeft2 = left2;
        upRight1 = right1; upRight2 = right2;
        downLeft1 = left1; downLeft2 = left2;
        downRight1 = right1; downRight2 = right2;
    }

    @Override
    public void checkDrop() {
        int roll = new java.util.Random().nextInt(100);

        if (roll <= 20) {
            dropItem(new Obj_Coin(gp));
        } else if (roll <= 40) {
            dropItem(new Obj_Heal_Potion(gp));
        } else if (roll <= 50) {
            dropItem(new Obj_Cup(gp));
        } else if (roll <= 52) {
            dropItem(new Obj_Amber(gp));
        } else if (roll <= 54) {
            dropItem(new Obj_Amethyst(gp));
        } else if (roll <= 56) {
            dropItem(new Obj_Diamond(gp));
        } else if (roll <= 58) {
            dropItem(new Obj_Emerald(gp));
        } else if (roll <= 60) {
            dropItem(new Obj_Quartz(gp));
        } else if (roll <= 62) {
            dropItem(new Obj_Ruby(gp));
        } else if (roll <= 64) {
            dropItem(new Obj_Sapphire(gp));
        } else if (roll <= 70) {
            dropItem(new Obj_Beer(gp));
        } else if (roll <= 75) {
            dropItem(new Obj_Cigarette(gp));
        } else if (roll <= 78) {
            dropItem(new Obj_Rum(gp));
        } else if (roll <= 81) {
            dropItem(new Obj_Drugs(gp));
        } else if (roll <= 84) {
            dropItem(new Obj_Tequila(gp));
        } else if (roll <= 87) {
            dropItem(new Obj_Whiskey(gp));
        } else if (roll <= 90) {
            dropItem(new Obj_Hook(gp));
        } else if (roll <= 93) {
            dropItem(new Obj_Armour_Helmet_Crusty(gp));
        } else if (roll <= 96) {
            dropItem(new Obj_Armour_Chest_Crusty(gp));
        } else {
            dropItem(new Obj_Armour_Boots_Crusty(gp));
        }
    }
}