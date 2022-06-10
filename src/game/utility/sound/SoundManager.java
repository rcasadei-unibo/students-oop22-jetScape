package game.utility.sound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    private Map<Sound,Clip> clips;
    private FloatControl fControl;
    private int volumeLevel = 2;
    private float volume;

    public SoundManager(Sound currentSound) {
        super();
        this.clips = new HashMap<>();
        this.setTrack(currentSound);
    }

    private void setTrack(Sound sound) {
        try {
            this.clips.put(sound, AudioSystem.getClip());
            this.clips.get(sound).open(AudioSystem.getAudioInputStream(
                    new File(DEFAULT_DIR + sound.getFileName())));
            this.fControl = (FloatControl)this.clips.get(sound)
                    .getControl(FloatControl.Type.MASTER_GAIN);
            updateVolume();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play(Sound sound) {
        setTrack(sound);
        clips.get(sound).start();
    }

    public void playInLoop(Sound sound) {
        setTrack(sound);
        clips.get(sound).loop(Clip.LOOP_CONTINUOUSLY);
        clips.get(sound).start();

    }

    public void stop(Sound sound) {
        if(clips.keySet().contains(sound)) {
            clips.get(sound).stop();
            this.removePlayed();
        }
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
    
    private void removePlayed() {
        this.clips.entrySet().stream()
            .filter(entry -> !entry.getValue().isRunning())
            .forEach(entry -> entry.getValue().close());
        this.clips.entrySet().removeIf(entry -> !entry.getValue().isRunning());
    }
}
