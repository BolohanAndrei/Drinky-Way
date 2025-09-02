package Monster;

import Main.GamePanel;
import object.*;

public class MON_GSlime extends BaseMonster {

    public MON_GSlime(GamePanel gp) {
        super(gp);
        setupSlimeProperties();
        getImage();
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

        // Slime-specific AI tuning
        config.aggroRange = 6;
        config.disengageRange = 18;
        config.lineOfSightRange = 12;

        // Slimes are moderately aggressive
        config.aggroChance = 75;
        config.disengageChance = 35;
        config.patrolAggroChance = 65;

        // Combat behavior - slimes prefer ranged attacks
        config.meleeAttackChance = 90;
        config.rangedAttackChance = 95;
        config.meleeAttackCooldown = 18;
        config.rangedAttackCooldown = 25;

        // Movement behavior
        config.stuckThreshold = 60;
        config.noMovementThreshold = 20;
        config.crowdAvoidanceChance = 30;

        return config;
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
        upLeft1 = setup("monsters/Slime_left1");
        upLeft2 = setup("monsters/Slime_left2");
        upRight1 = setup("monsters/Slime_right1");
        upRight2 = setup("monsters/Slime_right2");
        downLeft1 = setup("monsters/Slime_left1");
        downLeft2 = setup("monsters/Slime_left2");
        downRight1 = setup("monsters/Slime_right1");
        downRight2 = setup("monsters/Slime_right2");
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