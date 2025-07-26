package Main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX;
    int eventRectDefaultY;

    boolean eventActive = false;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new Rectangle(8, 8, 32, 32);
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent() {
        if (!eventActive) {
            if (hit(27, 16, "right")) {
                damagePit(gp.dialogueState);
                eventActive = true;
            }
            if (hit(23, 12, "up")) {
                healingEvent(gp.dialogueState);
                eventActive = true;
            }
        }
    }

    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;

        gp.player.solidArea.x = gp.player.x + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.y + gp.player.solidArea.y;
        eventRect.x = col * gp.tileSize + eventRectDefaultX;
        eventRect.y = row * gp.tileSize + eventRectDefaultY;

        if (gp.player.solidArea.intersects(eventRect)) {
            if (gp.player.direction.equals(reqDirection) || reqDirection.equals("any")) {
                hit = true;
            }
        }

        // Reset the solid area position
        gp.player.solidArea.x = gp.player.x + gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.y + gp.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return hit;

    }

    public void damagePit(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Arrr! You fell into a pit!";
        gp.player.health -= 2;
    }

    public void healingEvent(int gameState) {
        if(gp.keyHandler.enterPressed){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Arrr! You found a healing drink!";
        gp.player.health += 2;
        if (gp.player.health > gp.player.maxHealth) {
            gp.player.health = gp.player.maxHealth;
        }
    }}

    public void resetEvent() {
        eventActive = false;
    }

    public void draw(Graphics2D g2) {
        // Debug: Draw event areas
        g2.setColor(new Color(255, 0, 0, 80)); // Semi-transparent red

        // Draw damage pit - convert world coordinates to screen coordinates
        int worldX1 = 27 * gp.tileSize + eventRectDefaultX;
        int worldY1 = 16 * gp.tileSize + eventRectDefaultY;
        int screenX1 = worldX1 - gp.player.x + gp.player.screenX;
        int screenY1 = worldY1 - gp.player.y + gp.player.screenY;
        g2.fillRect(screenX1, screenY1, eventRect.width, eventRect.height);


        g2.setColor(new Color(0, 255, 0, 80));
        // Draw healing event - convert world coordinates to screen coordinates
        int worldX2 = 23 * gp.tileSize + eventRectDefaultX;
        int worldY2 = 12 * gp.tileSize + eventRectDefaultY;
        int screenX2 = worldX2 - gp.player.x + gp.player.screenX;
        int screenY2 = worldY2 - gp.player.y + gp.player.screenY;
        g2.fillRect(screenX2, screenY2, eventRect.width, eventRect.height);
    }
}
