package Main;


import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    private Clip clip;
    private final URL[] soundURL = new URL[30];

    public Sound() {
        String base = "/sound/";
        soundURL[0]  = load(base + "background_1.wav");
        soundURL[1]  = load(base + "background_2.wav");
        soundURL[2]  = load(base + "key_pick_up.wav");
        soundURL[3]  = load(base + "door.wav");
        soundURL[4]  = load(base + "chest.wav");
        soundURL[5]  = load(base + "power_up.wav");
        soundURL[6]  = load(base + "pirate_jokes_male_voice.wav");
        soundURL[7]  = load(base + "pirate_jokes_male_voice_2.wav");
        soundURL[8]  = load(base + "matey.wav");
        soundURL[9]  = load(base + "ghost_ship_ambience.wav");
        soundURL[10] = load(base + "bottle_caps.wav");
        soundURL[11] = load(base + "level_up.wav");
        soundURL[12] = load(base + "power_up_regeneration.wav");
        soundURL[13] = load(base + "background_3.wav");
        soundURL[14] = load(base + "background_4.wav");
        soundURL[15] = load(base + "background_5.wav");
        soundURL[16] = load(base + "hit.wav");
        soundURL[17] = load(base + "swing.wav");
        soundURL[18] = load(base + "take_damage.wav");
        soundURL[19] = load(base + "cursor.wav");
        soundURL[20] = load(base + "drink.wav");
        soundURL[21] = load(base + "throw.wav");
        soundURL[22] = load(base + "slime_walk.wav");
        soundURL[23] = load(base + "slime_death.wav");
        soundURL[24] = load(base + "slime_atk.wav");
        soundURL[25] = load(base + "coin.wav");
        soundURL[26] = load(base + "axe_cut.wav");
        soundURL[27] = load(base + "wrong_choice.wav");
    }

    private URL load(String path) {
        URL u = getClass().getResource(path);
        if (u == null) {
            System.err.println("Missing audio resource: " + path);
        }
        return u;
    }

    private void setFile(int i) {
        if (i < 0 || i >= soundURL.length) {
            System.err.println("Sound index out of range: " + i);
            return;
        }
        URL url = soundURL[i];
        if (url == null) {
            System.err.println("Sound not loaded (null URL) at index: " + i);
            return;
        }
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(url)) {
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.err.println("Failed to load sound index " + i + ": " + e.getMessage());
        }
    }

    public void play() {
        if (clip != null) clip.start();
    }

    public void stop() {
        if (clip != null && clip.isRunning()) clip.stop();
    }

    public void loop() {
        if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playMusic(int i) {
        setFile(i);
        play();
        loop();
    }

    public void stopMusic() {
        stop();
    }

    public void playSE(int i) {
        setFile(i);
        play();
    }
}