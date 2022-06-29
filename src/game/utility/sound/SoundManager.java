package game.utility.sound;

import java.io.File;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import game.utility.other.MenuOption;
import game.utility.other.Sound;

/**
 * Loads sounds and handles tracks playing stopping and volume changes requests.
 */
public class SoundManager {
    /**
     * maximum and minimum level for audio.
     */
    private static final int MAX_LEVEL = 4;
    private static final int MIN_LEVEL = 0;
    private static final String SEPARATOR = System.getProperty("file.separator");
    /**
     * absolute path of the directory containing all game's sounds.
     */
    public static final String DEFAULT_DIR = System.getProperty("user.dir") + SEPARATOR
            + "res" + SEPARATOR + "game" + SEPARATOR + "sound" + SEPARATOR;
    /**
     * associates a float value to the current volume level.
     */
    private final List<Float> audioSpectum = List.of(-80f, -25f, -15f, -10f, -5f);
    /**
     * associates the sounds to their specific track.
     */
    private final Map<Sound, Clip> clips;
    private final SettingsManager sManager;
    private int volumeLevel;
    private FloatControl fControl;

    /**
     * initializes the class to manage the specified audio setting tracks.
     * @param audioSetting to distinguish music and sound
     */
    public SoundManager(final MenuOption audioSetting) {
        super();
        this.clips = new HashMap<>();
        this.sManager = new SettingsManager(audioSetting);
        this.volumeLevel = this.sManager.getSettingValue();
    }

    /**
     * Plays the specified sound one time.
     * @param sound to play
     */
    public void play(final Sound sound) {
        this.removePlayed();
        setTrack(sound);
        clips.get(sound).start();
    }

    /**
     * Plays the specified sound in loop.
     * @param sound to play
     */
    public void playInLoop(final Sound sound) {
        this.removePlayed();
        setTrack(sound);
        clips.get(sound).loop(Clip.LOOP_CONTINUOUSLY);
        clips.get(sound).start();
    }

    /**
     * Stops the specified sound.
     * @param sound to stop
     */
    public void stop(final Sound sound) {
        if (clips.keySet().contains(sound)) {
            clips.get(sound).stop();
        }
        this.removePlayed();
    }

    /**
     * @return Volume level of this audio setting 
     */
    public int getVolumeLevel() {
        return volumeLevel;
    }

    /**
     * decreases by one the current volume level if 
     * is not already on the minimum allowed.
     */
    public void lowerVolumeLevel() {
        if (volumeLevel > MIN_LEVEL) {
            this.volumeLevel--;
            updateVolume();
            this.sManager.writeSetting(volumeLevel);
        }
    }

    /**
     * increments by one the current volume level if 
     * is not already on the maximum allowed.
     */
    public void raiseVolumeLevel() {
        if (volumeLevel < MAX_LEVEL) {
            this.volumeLevel++;
            updateVolume();
            this.sManager.writeSetting(volumeLevel);
        }
    }

    /**
     * @return the map of the currently playing tracks
     */
    public Map<Sound, Clip> getClipsMap(){
        return Collections.unmodifiableMap(this.clips);
    }

    private void setTrack(final Sound sound) {
        if (!clips.keySet().contains(sound)) {
            try {
                this.clips.put(sound, AudioSystem.getClip());
                this.clips.get(sound).open(AudioSystem.getAudioInputStream(
                    new File(DEFAULT_DIR + sound.getFileName())));
                this.fControl = (FloatControl) this.clips.get(sound)
                    .getControl(FloatControl.Type.MASTER_GAIN);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        updateVolume();
    }

    private void updateVolume() {
        this.fControl.setValue(audioSpectum.get(volumeLevel));
    }

    private void removePlayed() {
        this.clips.entrySet().stream()
            .filter(entry -> !entry.getValue().isRunning())
            .forEach(entry -> entry.getValue().close());
        this.clips.entrySet().removeIf(entry -> !entry.getValue().isRunning());
    }
}
