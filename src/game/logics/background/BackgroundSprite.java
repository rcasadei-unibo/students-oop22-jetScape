package game.logics.background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import game.utility.other.Pair;
import game.utility.sprites.AbstractSprite;
import game.utility.sprites.Sprite;

/**
 * This class is a specialised version of SpriteImpl used for loading
 * and the background sprite
 * 
 * This class is an implementation of a {@link Sprite} handler.
 * 
 * If the image cannot be loaded or any path isn't specified, a rectangle of
 * the color set by <code>{@link BackgroundController#PLACE_HOLDER}</code> will be drawn.
 */
public class BackgroundSprite extends AbstractSprite implements Sprite {

    /**
     * The image that will be drawn as sprite.
     */
    private Optional<BufferedImage> image = Optional.empty();

    /**
     * The color of the rectangle drawn as place holder.
     */
    private final Color placeHolder;

    private String name = "unknown";

    /**
     * Constructor that creates a {@link Sprite} object 
     * without any image path specified.
     * 
     * @param name the sprite name identifier
     * @param placeHolder the color of the rectangle drawn as place holder
     */
    public BackgroundSprite(final String name, final Color placeHolder) {
        this.name = name;
        this.placeHolder = placeHolder;
    }

    /**
     * Constructor that creates a {@link Sprite} object 
     * with a image path specified.
     * 
     * @param name the sprite name identifier
     * @param placeHolder the color of the rectangle drawn as place holder
     * @param path the path of the image to load
     */
    public BackgroundSprite(final String name, final Color placeHolder, final String path) {
        this(name, placeHolder);
        image = Optional.ofNullable(BackgroundSprite.load(AbstractSprite.getDefaultSpriteDirectory() + path));
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
     * @return the sprite name identifier
     */
    public String getName() {
        return name;
    }

    /**
     * Draws the loaded image or a square place holder if the
     * image hasn't been loaded.
     * 
     * @param g the graphics drawer
     * @param pos the position to draw the image
     * @param sizes the length of a side of the squared image
     */
    public void draw(final Graphics2D g, final Pair<Double, Double> pos, final int... sizes) {
        final int height = sizes[0];
        final int width;
        if (sizes.length == 2) {
            width = sizes[1];
        } else {
            width = height;
        }
        if (this.image.isPresent()) {
            g.drawImage(image.get(), (int) Math.round(pos.getX()), (int) Math.round(pos.getY()), width, height, null);
        } else {
            g.setColor(placeHolder);
            g.fillRect((int) Math.round(pos.getX()), (int) Math.round(pos.getY()), width, height);
        }
    }
}
