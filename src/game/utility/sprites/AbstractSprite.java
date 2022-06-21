package game.utility.sprites;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.utility.other.Pair;

/**
 * This abstract class contains some useful static variables and methods.
 *
 */
public abstract class AbstractSprite implements Sprite {

    /**
     * The type of file separator the system uses.
     */
    private static final String SEPARATOR = System.getProperty("file.separator");

    /**
     * Default directory where are all sprites are located.
     */
    private static final String DEFAULT_SPRITE_DIR = System.getProperty("user.dir") + SEPARATOR + "res" + SEPARATOR + "game" + SEPARATOR + "sprites" + SEPARATOR;

    /**
     * @return the type of file separator the system uses
     */
    public static String getSeparator() {
        return AbstractSprite.SEPARATOR;
    }

    /**
     * @return the type of file separator the system uses
     */
    public static String getDefaultSpriteDirectory() {
        return AbstractSprite.DEFAULT_SPRITE_DIR;
    }

    /**
     * Loads a {@link BufferedImage} of a sprite and returns it.
     * 
     * @param path the image path
     * @return a {@link BufferedImage} containing the sprite
     */
    public static BufferedImage load(final String path) {
        BufferedImage loaded = null;
        try {
            loaded = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loaded;
    }

    /**
     * {@inheritDoc}
     */
    public abstract String getName();

    /**
     * {@inheritDoc}
     */
    public abstract void draw(Graphics2D g, Pair<Double, Double> pos, int... sizes);
}
