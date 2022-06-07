package game.utility.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
    private static final String SEPARATOR = System.getProperty("file.separator");
    /**
     * absolute path of the directory containing all game's fonts.
     */
    public static final String DEFAULT_DIR = System.getProperty("user.dir") + SEPARATOR
            + "res" + SEPARATOR + "game" + SEPARATOR + "music" + SEPARATOR + "MainTheme.wav";

    Clip clip;
    FloatControl fControl;
    public static final int MAX_LEVEL = 4;
    public static final int MIN_LEVEL = 0;
    private int volumeLevel = 2;
    float volume;

    private void setTrack() {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(DEFAULT_DIR)));
            fControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            updateVolume();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playMainTheme() {
        setTrack();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    public void stop() {
        clip.stop();
    }

    public int getVolumeLevel() {
        return volumeLevel;
    }

    public void lowerVolumeLevel() {
        if (volumeLevel > MIN_LEVEL) {
            this.volumeLevel--;
            updateVolume();
        }
    }

    public void raiseVolumeLevel() {
        if (volumeLevel < MAX_LEVEL) {
            this.volumeLevel++;
            updateVolume();
        }
    }

    public void updateVolume() {
        switch (this.volumeLevel) {
            case 0: volume = -80f; break;
            case 1: volume = -30f; break;
            case 2: volume = -20f; break;
            case 3: volume = -10f; break;
            case 4: volume = -5f; break;
            default: break;
        }
        fControl.setValue(volume);
    }
}
