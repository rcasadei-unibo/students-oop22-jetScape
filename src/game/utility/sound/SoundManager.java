package game.utility.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import game.utility.other.Sound;

public class SoundManager {
    /**
     * maximum and minimum level for audio
     */
    public static final int MAX_LEVEL = 4;
    public static final int MIN_LEVEL = 0;
    private static final String SEPARATOR = System.getProperty("file.separator");
    /**
     * absolute path of the directory containing all game's fonts.
     */
    public static final String DEFAULT_DIR = System.getProperty("user.dir") + SEPARATOR
            + "res" + SEPARATOR + "game" + SEPARATOR + "sound"+ SEPARATOR;

    private Clip clip;
    private FloatControl fControl;
    private int volumeLevel = 2;
    private float volume;
    private Sound currentSound;


    public SoundManager(Sound currentSound) {
        super();
        this.currentSound = currentSound;
        this.setTrack(currentSound);
    }

    private void setTrack(Sound sound) {
        try {
            this.clip = AudioSystem.getClip();
            this.clip.open(AudioSystem.getAudioInputStream(
                    new File(DEFAULT_DIR + sound.getFileName())));
            this.fControl = (FloatControl)this.clip.getControl(FloatControl.Type.MASTER_GAIN);
            updateVolume();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play(Sound sound) {
        if(currentSound.equals(sound)) {
            setTrack(sound);
        }
        clip.start();
    }

    public void playInLoop(Sound sound) {
        if(currentSound.equals(sound)) {
            setTrack(sound);
        }
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

    private void updateVolume() {
        switch (this.volumeLevel) {
            case 0: volume = -80f; break;
            case 1: volume = -25f; break;
            case 2: volume = -15f; break;
            case 3: volume = -10f; break;
            case 4: volume = -5f; break;
            default: break;
        }
        this.fControl.setValue(volume);
    }
}
