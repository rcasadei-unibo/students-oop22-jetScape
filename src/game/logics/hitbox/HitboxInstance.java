package game.logics.hitbox;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

import game.frame.GameWindow;
import game.utility.debug.Debugger;
import game.utility.other.Pair;

/**
 * The {@link HitboxInstance} class represents a 
 * {@link game.logics.entities.generic.Entity Entity} hitbox.
 */
public abstract class HitboxInstance implements Hitbox {
    /**
     * sprite default dimension.
     */
    static final int SPRITE_DIMENSIONS = 32;
    /**
     * map that associates a rectangle with a pair of 
     * coordinates which origin is the actual sprite position .
     */
    private final Map<Rectangle, Pair<Double, Double>> hitboxes;
    private final Pair<Double, Double> startingPos;
    /**
     * used to represent safely group of {@link Hitbox}, forcing to update
     * each entity of such individually.
     */
    private final Set<Rectangle> rectangles;

    /**
     * initializes a {@link game.logics.entities.generic.Entity Entity} hitbox.
     * @param startingPos
     */
    public HitboxInstance(final Pair<Double, Double> startingPos) {
        super();
        this.hitboxes = new HashMap<>();
        this.startingPos = new Pair<>(startingPos.getX(), startingPos.getY());
        this.rectangles = new HashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    public void updatePosition(final Pair<Double, Double> newPos) {
        this.hitboxes.forEach((hitbox, scale) -> {
            hitbox.setLocation((int) (newPos.getX() + scale.getX()),
                    (int) (newPos.getY() + scale.getY()));
        });
    }

    /**
     * @return set this entity component rectangle 
     */
    public Set<Rectangle> getRectangles() {
        return Collections.unmodifiableSet(this.rectangles);
    }

    /**
     * {@inheritDoc}
     */
    public void draw(final Graphics2D g) {
        if (GameWindow.GAME_DEBUGGER.isFeatureEnabled(Debugger.Option.HITBOX)) {
            this.rectangles.forEach(hitbox -> {
                g.setColor(Color.MAGENTA);
                g.draw(hitbox);
            });
        }
    }

    /**
     * adds the rectangle of the specified dimensions in the hitbox.
     * 
     * @param width
     * @param height
     * @param x
     * @param y
     */
    protected void addRectangle(final double x, final double y, final double width, final double height) {
        final int startingX = (int) (startingPos.getX() + this.scale(x));
        final int startingY = (int) (startingPos.getY() + this.scale(y));
        final int scaledWidth  = (int) this.scale(width);
        final int scaledHeight = (int) this.scale(height);
        this.hitboxes.put(new Rectangle(startingX, startingY, scaledWidth, scaledHeight),
                new Pair<>(this.scale(x), this.scale(y)));
        this.rectangles.addAll(this.hitboxes.keySet());
    }

    /**
     * scales the hitbox dimension using the current screen tile's size.
     * 
     * @param x 
     * @return scaled x
     */
    private double scale(final double x) {
        return GameWindow.GAME_SCREEN.getTileSize() * (x / SPRITE_DIMENSIONS);
    }

    /**
     * adds the specified hitbox to the group (to use only with groups of entities).
     * 
     * @param hitbox
     */
    protected void addHitbox(final Hitbox hitbox) {
        this.rectangles.addAll(hitbox.getRectangles());
    }
}
