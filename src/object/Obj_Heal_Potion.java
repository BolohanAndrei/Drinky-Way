package object;

import Entity.Entity;
import Main.GamePanel;

public class Obj_Heal_Potion extends Entity {
    GamePanel gp;
    public Obj_Heal_Potion(GamePanel gp) {
        super(gp);
        this.gp=gp;
        gearType=2;
        value=50;
        stackable=true;

        name="Heal Potion";
        down1=setup("objects/heal_potion");
        itemDescription = "["+name+"] HP +"+2+"\nLiquid bandage for the soul. Restores health, dignity sold separately.";

        setDialogue();
    }

    public void setDialogue() {
        dialogue[0][0]="Health restored by "+2+"HP!\nMy liver cries, but my heart sings! Now, where’s the tavern?";

    }
    public boolean use(Entity e){
        startDialogue(this,0);
        e.health+=2;
        if(gp.player.health>gp.player.maxHealth){
            gp.player.health=gp.player.maxHealth;
        }
        gp.drinkSystem.soberUp(gp.player);
        gp.se.playSE(20);
        return true;
    }
}
