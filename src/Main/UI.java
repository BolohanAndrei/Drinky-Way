package Main;

import object.*;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    Font PublicPixel;

    public boolean MessageOn = false;
    public String message = "";
    public String currentDialogue = "";

    //CONSTRUCTOR
    public UI(GamePanel gp) {
        this.gp = gp;
        try{
            InputStream is=getClass().getResourceAsStream("/res/Fonts/PublicPixel-rv0pA.ttf");
            assert is != null;
            PublicPixel = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String text) {
        message = text;
        MessageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(PublicPixel);
        g2.setColor(Color.white);

        //PLAT STATE
        if(gp.gameState==gp.playState){
            //
        }

        //PAUSE STATE
        if(gp.gameState== gp.pauseState){
            drawPauseScreen();
        }

        //DIALOGUE STATE
        if(gp.gameState==gp.dialogueState){
            drawDialogueScreen();
        }
    }

    public void drawPauseScreen() {
        g2.setFont(PublicPixel);
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen(){
        //WINDOW
        int x,y,width,height;

        x= gp.tileSize * 2;
        y= gp.tileSize / 2;
        width = gp.screenWidth - (gp.tileSize * 4);
        height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));

        //TEXT - Using automatic text wrapping
        x+= gp.tileSize;
        y+= gp.tileSize;

        // Calculate available width for text (subtract padding)
        int availableWidth = width - (gp.tileSize * 2);

        // Get wrapped lines
        ArrayList<String> wrappedLines = wrapText(currentDialogue, availableWidth);

        // Draw each wrapped line
        for(String line : wrappedLines) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public ArrayList<String> wrapText(String text, int maxWidth) {
        ArrayList<String> wrappedLines = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return wrappedLines;
        }

        if (g2.getFontMetrics().stringWidth(text) <= maxWidth) {
            wrappedLines.add(text);
            return wrappedLines;
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;

            if (g2.getFontMetrics().stringWidth(testLine) > maxWidth) {
                if (currentLine.length() > 0) {
                    wrappedLines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    String brokenWord = breakLongWord(word, maxWidth);
                    wrappedLines.add(brokenWord);
                    currentLine = new StringBuilder();
                }
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }

        if (currentLine.length() > 0) {
            wrappedLines.add(currentLine.toString());
        }

        return wrappedLines;
    }

    private String breakLongWord(String word, int maxWidth) {
        if (g2.getFontMetrics().stringWidth(word) <= maxWidth) {
            return word;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            String testString = result.toString() + word.charAt(i);
            if (g2.getFontMetrics().stringWidth(testString) > maxWidth) {
                break;
            }
            result.append(word.charAt(i));
        }

        return result.toString();
    }

    public void drawSubWindow(int x, int y, int width, int height ){

        Color c=new Color(0,0,0, 175);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c= new Color(255, 255, 255,200);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (gp.screenWidth / 2) - (length / 2);
    }
}