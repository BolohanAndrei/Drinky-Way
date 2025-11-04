package Main;


import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    private Clip clip;
    private final URL[] soundURL = new URL[100];
    FloatControl fc;
    int volumeScale=3;
    float volume;

    public Sound() {
        String base = "/res/sound/";
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
        soundURL[28] = load(base + "game_over.wav");
        soundURL[29] = load(base + "pirate_laugh.wav");
        soundURL[30] = load(base + "move_item.wav");
        soundURL[31] = load(base + "chest_open.wav");
        soundURL[32] = load(base + "chest_close.wav");
        soundURL[33] = load(base + "guard.wav");
        soundURL[34] = load(base + "perry.wav");
        soundURL[35] = load(base + "pirate_tavern.wav");
        soundURL[36] = load(base + "pickaxe.wav");
        soundURL[37] = load(base + "metal_door_bang.wav");
        soundURL[38] = load(base + "iron_plate.wav");
        soundURL[39] = load(base + "door_iron_open.wav");
        soundURL[40] = load(base + "dice.wav");
        soundURL[41] = load(base + "dice_long.wav");

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
            fc=(FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
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


    public void checkVolume(){
        switch (volumeScale){
            case 0: volume=fc.getMinimum(); break;
            case 1: volume=-40f; break;
            case 2: volume=-30f; break;
            case 3: volume=-20f; break;
            case 4: volume=0f; break;
            case 5: volume=fc.getMaximum(); break;
        }
        fc.setValue(volume);
    }

}