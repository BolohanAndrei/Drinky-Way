package Main;

import java.io.*;
import java.nio.file.*;

public class Config {
    GamePanel gp;
    private static final String CONFIG_FILE = "config.txt";

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();
            bw.write(String.valueOf(gp.se.volumeScale));
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig() {
        File externalConfig = new File(CONFIG_FILE);
        if (externalConfig.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(externalConfig))) {
                readConfig(br);
                return;
            } catch (IOException e) {
            }
        }

        try (InputStream is = getClass().getResourceAsStream("res/config.txt")) {
            if (is != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    readConfig(br);
                }
            } else {
                saveConfig();
            }
        } catch (IOException e) {
            saveConfig();
        }
    }

    private void readConfig(BufferedReader br) throws IOException {
        String s = br.readLine();
        if (s != null) gp.music.volumeScale = safeParseInt(s, gp.music.volumeScale);
        s = br.readLine();
        if (s != null) gp.se.volumeScale = safeParseInt(s, gp.se.volumeScale);
    }

    private int safeParseInt(String v, int fallback){
        try { return Integer.parseInt(v.trim()); } catch(Exception ex){ return fallback; }
    }
}
