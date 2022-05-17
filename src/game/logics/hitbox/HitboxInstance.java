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
 * The <code>HitboxInstance</code> class represents a generic entity's group of hitboxes
 * 
 * @author Giacomo Amadio
 */
public abstract class HitboxInstance implements Hitbox{
    /**
     * sprite default dimension 
     */
    static final int spriteDimensions = 32;
    /**
     * map that associates a rectangle with a pair of 
     * coordinates which origin is the actual sprite position 
     */
    protected final Map<Rectangle, Pair<Double,Double>> hitboxes;
    private final Pair<Double,Double> startingPos;
    protected final Set<Rectangle> rectangles;
    
    public HitboxInstance(Pair<Double, Double> startingPos) {
        super();
        this.hitboxes = new HashMap<>();
        this.startingPos = new Pair<>(startingPos.getX(),startingPos.getY());
        this.rectangles = new HashSet<>();
    }

    public void updatePosition(Pair<Double,Double> newPos) {
        this.hitboxes.forEach((hitbox,scale) -> {
            hitbox.setLocation((int) (newPos.getX()+ scale.getX()),
                    (int) (newPos.getY()+ scale.getY()));
        });
    }
    /**
     * @return set this entity component rectangle 
     */
    public Set<Rectangle> getRectangles() {
        return Collections.unmodifiableSet(this.hitboxes.keySet());
    }
    
    public void draw(Graphics2D g) {
        if(GameWindow.GAME_DEBUGGER.isFeatureEnabled(Debugger.Option.HITBOX)) {
            this.rectangles.forEach(hitbox -> {
                g.setColor(Color.MAGENTA);
                g.draw(hitbox);
            });
        }
    }
    
    /**
     * @param top left corner x
     * @param top left corner y 
     * @param width
     * @param height
     * 
     * puts the new entry in the hitbox map 
     */
    protected void addRectangle(double x, double y, double width, double height) {
        int startingX = (int) (startingPos.getX() + this.scale(x));
        int startingY = (int) (startingPos.getY() + this.scale(y));
        int scaledWidth  = (int) this.scale(width);
        int scaledHeight = (int) this.scale(height);
        this.hitboxes.put(new Rectangle(startingX,startingY,scaledWidth,scaledHeight),
                new Pair<>(this.scale(x), this.scale(y)));
        this.rectangles.addAll(this.hitboxes.keySet());
    }
    
    /**
     * @param double dimension
     * 
     * scales the hitbox dimension using the current screen tile's size
     */
    private double scale (double x) {
        return (GameWindow.GAME_SCREEN.getTileSize()* (x/spriteDimensions));
    }
}
