package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Heal_Potion extends Entity {
    GamePanel gp;
    int value=2;
    public Obj_Heal_Potion(GamePanel gp) {
        super(gp);
        this.gp=gp;
        gearType=2;
        name="Heal Potion";
        down1=setup("objects/heal_potion");
        itemDescription = "["+name+"] HP +"+value+"\nLiquid bandage for the soul. Restores health, dignity sold separately.";
    }

    public void use(Entity e){
        gp.gameState=gp.dialogueState;
        gp.ui.currentDialogue="Health restored by "+value+"HP!\nMy liver cries, but my heart sings! Now, where’s the tavern?";
        e.health+=value;
        if(gp.player.health>gp.player.maxHealth){
            gp.player.health=gp.player.maxHealth;
        }
        gp.playSE(20);
    }
}
