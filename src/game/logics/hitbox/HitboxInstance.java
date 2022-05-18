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
 * The {@link HitboxInstance} class represents a generic entity's group of hitboxes.
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

    // TODO: change to private and add protected getters and setters.
    /**
     * .
     */
    protected final Set<Rectangle> rectangles;
    // TODO: add javadoc
    /**
     * .
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
    // TODO: complete javadoc parameters.
    /**
     * puts the new entry in the hitbox map.
     * 
     * @param width
     * @param height
     * @param x
     * @param y
     */
    protected void addRectangle(final double x, final double y, final double width, final double height) {
        int startingX = (int) (startingPos.getX() + this.scale(x));
        int startingY = (int) (startingPos.getY() + this.scale(y));
        int scaledWidth  = (int) this.scale(width);
        int scaledHeight = (int) this.scale(height);
        this.hitboxes.put(new Rectangle(startingX, startingY, scaledWidth, scaledHeight),
                new Pair<>(this.scale(x), this.scale(y)));
        this.rectangles.addAll(this.hitboxes.keySet());
    }

    // TODO: complete javadoc
    /**
     * scales the hitbox dimension using the current screen tile's size.
     * 
     * @param x 
     * @return .
     */
    private double scale(final double x) {
        return (GameWindow.GAME_SCREEN.getTileSize() * (x / SPRITE_DIMENSIONS));
    }
}
