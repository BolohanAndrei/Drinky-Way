package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL =new URL[30];

    // Sound array
    public Sound(){
        soundURL[0]=getClass().getResource("/res/sound/background_1.wav"); // Background music 1
        soundURL[1]=getClass().getResource("/res/sound/background_2.wav"); // Background music 2
        soundURL[13]=getClass().getResource("/res/sound/background_3.wav"); // Background music 3
        soundURL[14]=getClass().getResource("/res/sound/background_4.wav"); // Background music 4
        soundURL[15]=getClass().getResource("/res/sound/background_5.wav"); // Background music 5
        soundURL[2]=getClass().getResource("/res/sound/key_pick_up.wav"); // Key sound
        soundURL[3]=getClass().getResource("/res/sound/door.wav"); // Door open sound
        soundURL[4]=getClass().getResource("/res/sound/chest.wav"); // Chest sound
        soundURL[5]=getClass().getResource("/res/sound/power_up.wav"); // power up sound
        soundURL[6]=getClass().getResource("/res/sound/pirate_jokes_male_voice.wav"); // pirate_jokes_male_voice sound
        soundURL[7]=getClass().getResource("/res/sound/pirate_jokes_male_voice2.wav"); // pirate_jokes_male_voice_2 sound
        soundURL[8]=getClass().getResource("/res/sound/matey.wav"); // matey sound
        soundURL[9]=getClass().getResource("/res/sound/ghost_ship_ambience.wav"); // ghost ship ambience sound
        soundURL[10]=getClass().getResource("/res/sound/bottle_caps.wav"); // bottle caps sound
        soundURL[11]=getClass().getResource("/res/sound/level_up.wav"); // level up sound
        soundURL[12]=getClass().getResource("/res/sound/power_up_regeneration.wav"); // power up regeneration sound


    }

    public void setFile(int i){
        try{
            AudioInputStream ais= AudioSystem.getAudioInputStream(soundURL[i]);
            clip=AudioSystem.getClip();
            clip.open(ais);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void play(){
        clip.start();
    }

    public void stop(){
      clip.stop();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void playAlternatingLoop(int i, int j,int k,int l) {
        new Thread(() -> {
            while (true) {
                playAndWait(i);
                playAndWait(j);
                playAndWait(k);
                playAndWait(l);
            }
        }).start();
    }

    private void playAndWait(int i) {
        setFile(i);
        clip.start();
        try {
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clip.stop();
    }
}
