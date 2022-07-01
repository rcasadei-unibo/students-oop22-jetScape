package game.utility.sound;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import game.utility.other.MenuOption;

/**
 * loads settings values and writes changes to Json files.
 */
public final class SettingsManager {
    private static final String SEPARATOR = System.getProperty("file.separator");
    /**
     * absolute path of the directory containing game's settings.
     */
    public static final String PATH = System.getProperty("user.dir") + SEPARATOR
            + "res" + SEPARATOR + "game" + SEPARATOR + "data" + SEPARATOR + "settings.json";
    private static final long DEFAULT_SETTING = 2;

    private final File file;
    private JsonObject settings;
    private final MenuOption option;

    /**
     * initializes a manager for the given setting.
     * @param option setting to read and write
     */
    public SettingsManager(final MenuOption option) {
        this.file = new File(PATH);
        this.option = option;
    }

    /**
     * writes new value of the associated setting on file.
     * @param value to write
     */
    public void writeSetting(final int value) {
        this.update();
        this.settings.replace(option.toString(), value);
        this.write();
    }

    /**
     * reads the value of the associated setting from file.
     * @return value read
     */
    public int getSettingValue() {
        this.update();
        return ((BigDecimal) settings.get(option.toString())).intValue();
    }

    private void write() {
        try (FileWriter file = new FileWriter(this.file)) {
            file.write(settings.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        if (!file.exists()) {
            this.buildDefault();
            this.write();
        }

        try (Reader reader = new FileReader(this.file)) {
            this.settings = (JsonObject) Jsoner.deserialize(reader);
        } catch (IOException | JsonException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void buildDefault() {
        this.settings = new JsonObject();

        this.settings.put(MenuOption.MUSIC.toString(),
                BigDecimal.valueOf(DEFAULT_SETTING));
        this.settings.put(MenuOption.SOUND.toString(),
                BigDecimal.valueOf(DEFAULT_SETTING));
    }
}
