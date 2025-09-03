package Monster;

import Main.GamePanel;
import object.*;

public class MON_Orc extends BaseMonster {

    public MON_Orc(GamePanel gp) {
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
        name = "Orc";
        speed = 1;
        maxHealth = 20;
        health = maxHealth;
        attack = 10;
        defense = 4;
        exp = 20;

        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = 44;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    protected AIConfig createAIConfig() {
        AIConfig c = new AIConfig();
        c.style = CombatStyle.MELEE;
        c.aggroRange = 10;
        c.disengageRange = 20;
        c.lineOfSightRange = 15;
        c.meleeAttackChance = 95;
        c.meleeAttackCooldown = 10;
        c.meleeRangeTiles = (int) 1.8;
        c.retreatSpeedMultiplier = 1.2f;
        c.alternativePathSpeedMultiplier = 1.4f;
        c.meleeWindupFrames = 15;
        c.meleeActiveFrames = 12;

        c.aggroChance = 85;
        c.disengageChance = 55;
        c.patrolAggroChance = 75;

        c.stuckThreshold = 70;
        c.noMovementThreshold = 30;
        c.crowdAvoidanceChance = 60;
        return c;
    }

    @Override
    protected void initAdvancedAnimations() {
        assignWalkFrames(0, loadFrames("monsters/Orc3/Walk/orc_walk_up", 6));
        assignWalkFrames(1, loadFrames("monsters/Orc3/Walk/orc_walk_down", 6));
        assignWalkFrames(2, loadFrames("monsters/Orc3/Walk/orc_walk_left", 6));
        assignWalkFrames(3, loadFrames("monsters/Orc3/Walk/orc_walk_right", 6));

        assignIdleFrames(0, loadFrames("monsters/Orc3/Idle/orc_idle_up", 4));
        assignIdleFrames(1, loadFrames("monsters/Orc3/Idle/orc_idle_down", 4));
        assignIdleFrames(2, loadFrames("monsters/Orc3/Idle/orc_idle_left", 4));
        assignIdleFrames(3, loadFrames("monsters/Orc3/Idle/orc_idle_right", 4));

        assignAttackFrames(0, loadFrames("monsters/Orc3/Attack/orc_attack_up", 8));
        assignAttackFrames(1, loadFrames("monsters/Orc3/Attack/orc_attack_down", 8));
        assignAttackFrames(2, loadFrames("monsters/Orc3/Attack/orc_attack_left", 8));
        assignAttackFrames(3, loadFrames("monsters/Orc3/Attack/orc_attack_right", 8));

        deathFrames = loadFrames("monsters/Orc3/Death/orc_die_down", 8);

        animWalkSpeed = 12;
        animAttackSpeed = 4;
        animIdleSpeed = 8;
        animDeathSpeed = 8;
    }

    @Override
    public void getImage() {
        up1 = setup("monsters/Orc3/Walk/orc_walk_up1");
        down1 = setup("monsters/Orc3/Walk/orc_walk_down1");
        left1 = setup("monsters/Orc3/Walk/orc_walk_left1");
        right1 = setup("monsters/Orc3/Walk/orc_walk_right1");
        up2 = setup("monsters/Orc3/Walk/orc_walk_up2");
        down2 = setup("monsters/Orc3/Walk/orc_walk_down2");
        left2 = setup("monsters/Orc3/Walk/orc_walk_left2");
        right2 = setup("monsters/Orc3/Walk/orc_walk_right2");
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
