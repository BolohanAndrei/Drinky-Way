package Monster;

import Main.GamePanel;
import object.*;

public class MON_BlueSlime extends BaseMonster {

    public MON_BlueSlime(GamePanel gp) {
        super(gp);
        setupStats();
        getImage();
        initAdvancedAnimations();
        setRenderScale(2.0f);
        adjustCollisionBoxForScale();
    }

    private void adjustCollisionBoxForScale(){
        int extra = (int)((renderScale - 1f) * gp.tileSize);
        int shift = extra / 2;
        solidArea.y = Math.max(0, solidArea.y - shift);
        solidArea.x = Math.max(0, solidArea.x - 2);
        solidArea.width = Math.min(gp.tileSize - solidArea.x, solidArea.width + 4);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    private void setupStats(){
        name = "Blue Slime";
        speed = 1;
        maxHealth = 8;
        health = maxHealth;
        attack = 4;
        defense = 1;
        exp = 4;
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
        AIConfig c = new AIConfig();
        c.style = CombatStyle.MELEE;
        c.aggroRange = 7;
        c.disengageRange = 20;
        c.lineOfSightRange = 14;
        c.meleeAttackChance = 88;
        c.meleeAttackCooldown = 30;
        c.meleeRangeTiles = (int) 1.5;
        c.retreatSpeedMultiplier = 1.35f;
        c.alternativePathSpeedMultiplier = 1.2f;
        c.meleeWindupFrames = 14;
        c.meleeActiveFrames = 10;
        return c;
    }

    @Override
    protected void initAdvancedAnimations() {
        assignWalkFrames(0, loadFrames("monsters/Slime2/Walk/slime2_walk_up", 8));
        assignWalkFrames(1, loadFrames("monsters/Slime2/Walk/slime2_walk_down", 8));
        assignWalkFrames(2, loadFrames("monsters/Slime2/Walk/slime2_walk_left", 8));
        assignWalkFrames(3, loadFrames("monsters/Slime2/Walk/slime2_walk_right", 8));

        assignIdleFrames(0, loadFrames("monsters/Slime2/Idle/slime2_idle_up", 6));
        assignIdleFrames(1, loadFrames("monsters/Slime2/Idle/slime2_idle_down", 6));
        assignIdleFrames(2, loadFrames("monsters/Slime2/Idle/slime2_idle_left", 6));
        assignIdleFrames(3, loadFrames("monsters/Slime2/Idle/slime2_idle_right", 6));

        assignAttackFrames(0, loadFrames("monsters/Slime2/Attack/slime2_attack_up", 11));
        assignAttackFrames(1, loadFrames("monsters/Slime2/Attack/slime2_attack_down", 11));
        assignAttackFrames(2, loadFrames("monsters/Slime2/Attack/slime2_attack_left", 11));
        assignAttackFrames(3, loadFrames("monsters/Slime2/Attack/slime2_attack_right", 11));

        deathFrames = loadFrames("monsters/Slime2/Death/slime2_die_down", 10);

        animWalkSpeed = 8;
        animAttackSpeed = 4;
        animIdleSpeed = 25;
        animDeathSpeed = 8;
    }

    @Override
    public void getImage() {
        up1 = setup("monsters/Slime2/Walk/slime2_walk_up1");
        down1 = setup("monsters/Slime2/Walk/slime2_walk_down1");
        left1 = setup("monsters/Slime2/Walk/slime2_walk_left1");
        right1 = setup("monsters/Slime2/Walk/slime2_walk_right1");
        up2 = setup("monsters/Slime2/Walk/slime2_walk_up2");
        down2 = setup("monsters/Slime2/Walk/slime2_walk_down2");
        left2 = setup("monsters/Slime2/Walk/slime2_walk_left2");
        right2 = setup("monsters/Slime2/Walk/slime2_walk_right2");

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
