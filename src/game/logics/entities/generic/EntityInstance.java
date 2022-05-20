package game.logics.entities.generic;

import game.frame.GameWindow;
import game.logics.handler.Logics;
import game.logics.hitbox.Hitbox;
import game.utility.debug.Debugger;
import game.utility.other.EntityType;
import game.utility.other.Pair;
import game.utility.sprites.DrawManager;
import game.utility.sprites.Drawer;

import java.awt.Graphics2D;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * The abstract class {@link EntityInstance} is used to define all the common parts of each entity
 * like their position, entity relationship, visibility, on screen presence, etc...
 */
public abstract class EntityInstance implements Entity {
    /**
     * Defines the entity's position on the game environment. 
     */
    private final Pair<Double, Double> position;
    /**
     * Defines the entity's starting position.
     */
    private final Pair<Double, Double> startPos;
    /**
     * Defines the entity's type category.
     */
    private final EntityType entityTag;

    /// FLAGS ///
    private boolean visible;
    private boolean onScreen;
    private boolean onClearArea;
    private boolean onSpawnArea = true;

    private Hitbox hitbox;

    private final Drawer spritesMgr = new DrawManager();
    private final BiConsumer<Predicate<EntityType>, Predicate<Entity>> cleaner;

    /**
     * Constructor that sets up entity default values (picked up from 
     * {@link Logics}), defines it's bounds in the environment and allows to set it's
     * starting position.
     * 
     * @param l the logics handler which the entity is linked to
     * @param position the starting position of the entity in the environment
     * @param type the type of entity to create
     */
    protected EntityInstance(final Logics l, final Pair<Double, Double> position, final EntityType type) {
        this.cleaner = l.getEntitiesCleaner();
        this.position = position;
        this.startPos = position.copy();
        this.entityTag = type;

        this.setVisibility(true);
    }

    /**
     * Allows to set the entity visibility.
     * 
     * @param v <code>true</code> if entity has to be shown, <code>false</code> if entity has to be hidden
     */
    protected final void setVisibility(final boolean v) {
        visible = v;
    }

    /**
     * Allows to set the main {@link Hitbox} of the entity.
     * 
     * @param hitb the {@link Hitbox} object to assign 
     */
    protected void setHitbox(final Hitbox hitb) {
        this.hitbox = hitb;
    }
    /**
     * @return a {@link Drawer} object that manages the sprites of the entity.
     */
    protected Drawer getSpriteManager() {
        return spritesMgr;
    }
    /**
     * @return a {@link BiConsumer} function that cleans all the entities who respect both {@link Predicate}.
     */
    protected BiConsumer<Predicate<EntityType>, Predicate<Entity>> getCleaner() {
        return cleaner;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isVisible() {
        return visible;
    }
    /**
     * {@inheritDoc}
     */
    public boolean isOnScreenBounds() {
        return onScreen;
    }
    /**
     * {@inheritDoc}
     */
    public boolean isOnClearArea() {
        return this.onClearArea;
    }
    /**
     * {@inheritDoc}
     */
    public boolean isOnSpawnArea() {
        return this.onSpawnArea;
    }
    /**
     * {@inheritDoc}
     */
    public Pair<Double, Double> getPosition() {
        return position;
    }
    /**
     * {@inheritDoc}
     */
    public Hitbox getHitbox() {
        return this.hitbox;
    }
    /**
     * {@inheritDoc}
     */
    public EntityType entityType() {
        return entityTag;
    }
    /**
     * {@inheritDoc}
     */
    public void reset() {
        position.setX(startPos.getX());
        position.setY(startPos.getY());
    }
    /**
     * {@inheritDoc}
     */
    public void clean() {
        this.reset();
        cleaner.accept(t -> this.entityType() == t, e -> this == e);
    }

    /**
     * Updates the entity's flags.
     */
    private void updateFlags() {
        if (position.getX() >= -GameWindow.GAME_SCREEN.getTileSize() && position.getX() <= GameWindow.GAME_SCREEN.getWidth() && position.getY() >= 0 && position.getY() <= GameWindow.GAME_SCREEN.getHeight()) {
            onScreen = true;
            onClearArea = false;
            onSpawnArea = false;
        } else {
            if (position.getX() < -GameWindow.GAME_SCREEN.getTileSize()) {
                onClearArea = true;
                onSpawnArea = false;
            } else if (position.getX() >= GameWindow.GAME_SCREEN.getWidth()) {
                onClearArea = false;
                onSpawnArea = true;
            } else {
                onClearArea = false;
                onSpawnArea = false;
            }
            onScreen = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void update() {
        updateFlags();
    }
    /**
     * {@inheritDoc}
     */
    public void draw(final Graphics2D g) {
        if (this.isVisible()) {
            spritesMgr.drawCurrentSprite(g, position, GameWindow.GAME_SCREEN.getTileSize());
        }
    }
    /**
     * {@inheritDoc}
     */
    public void drawCoordinates(final Graphics2D g) {
        final int xShift = (int) Math.round(position.getX()) + (int) Math.round(GameWindow.GAME_SCREEN.getTileSize() * 0.88);
        final int yShiftDrawnX = (int) Math.round(position.getY()) + GameWindow.GAME_SCREEN.getTileSize();
        final int yShiftDrawnY = yShiftDrawnX + 10;

        if (GameWindow.GAME_DEBUGGER.isFeatureEnabled(Debugger.Option.ENTITY_COORDINATES) && this.isVisible()) {
            g.setColor(Debugger.DEBUG_COLOR);
            g.setFont(Debugger.DEBUG_FONT);

            g.drawString("X:" + Math.round(position.getX()), xShift, yShiftDrawnX);
            g.drawString("Y:" + Math.round(position.getY()), xShift, yShiftDrawnY);
        }
    }

    /**
     * @return a string representing the type of entity with his coordinates in the environment
     */
    public String toString() {
        return entityType().toString() + "[X:" + Math.round(position.getX()) + "-Y:" + Math.round(position.getY()) + "]";
    }
}
