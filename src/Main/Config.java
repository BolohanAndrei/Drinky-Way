package Main;

import java.io.*;

public class Config {
    GamePanel gp;
    public Config(GamePanel gp) { this.gp = gp; }

    public void saveConfig(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("src/config.txt"))) {
            bw.write(String.valueOf(gp.music.volumeScale)); bw.newLine();
            bw.write(String.valueOf(gp.se.volumeScale)); bw.newLine();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void loadConfig(){
        try(BufferedReader br = new BufferedReader(new FileReader("src/config.txt"))) {
            String s = br.readLine(); if(s!=null) gp.music.volumeScale = safeParseInt(s, gp.music.volumeScale);
            s = br.readLine(); if(s!=null) gp.se.volumeScale = safeParseInt(s, gp.se.volumeScale);
        } catch (IOException e) { saveConfig(); }
    }

    private int safeParseInt(String v, int fallback){
        try { return Integer.parseInt(v.trim()); } catch(Exception ex){ return fallback; }
    }
}
