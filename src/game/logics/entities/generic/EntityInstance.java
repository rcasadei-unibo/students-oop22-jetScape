package game.logics.entities.generic;

import game.frame.GameWindow;
import game.logics.handler.Logics;
import game.logics.hitbox.Hitbox;
import game.utility.debug.Debugger;
import game.utility.other.EntityType;
import game.utility.other.Pair;
import game.utility.screen.Screen;
import game.utility.sprites.DrawManager;
import game.utility.sprites.Drawer;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * The abstract class <code>EntityInstance</code> is used to define all the common parts of each entity
 * like their position, entity relationship, visibility, on screen presence, etc...
 * 
 * @author Daniel Pellanda
 */
public abstract class EntityInstance implements Entity{
	
	protected int currentFPS = 0;
	
	/**
	 * An utility variable used to correct the higher draw bound of the entity.
	 */
	protected final double yRoof;
	/**
	 * An utility variable used to correct the lower draw bound of the entity.
	 */
	protected final double yGround;
	
	/**
	 * Defines the entity's position on the game environment. 
	 */
	protected Pair<Double, Double> position;
	/**
	 * Defines the entity's starting position.
	 */
	private Pair<Double, Double> startPos;
	/**
	 * Defines the entity's type category.
	 */
	protected EntityType entityTag;
	
	/// FLAGS ///
	/**
	 * Decides if the entity has to be shown on screen.
	 */
	private boolean visible = true;
	/**
	 * A flag that automatically updates and tells if the entity's position between screen bounds.
	 */
	private boolean onScreen = false;
	/**
	 * A flag that automatically updates and tells if the entity is on the "clear area".
	 */
	private boolean onClearArea = false;
	/**
	 * A flag that automatically updates and tells if the entity is on the "spawn area".
	 */
	private boolean onSpawnArea = true;
	
	protected Hitbox hitbox ;
	protected Set<Hitbox> hitboxSet = new HashSet<>();
	
	/**
	 * Manages the sprites of the object.
	 */
	protected final Drawer spritesMgr;
	protected final Screen screen;
	protected final BiConsumer<Predicate<EntityType>,Predicate<Entity>> cleaner;
	
	/**
	 * Constructor that sets up entity default values (picked up from 
	 * <code>Logics</code>) and defines it's bounds in the environment.
	 * 
	 * @param l the logics handler which the entity is linked to
	 */
	protected EntityInstance(final Logics l) {
		this.screen = GameWindow.gameScreen;
		this.cleaner = l.getEntitiesCleaner();
		entityTag = EntityType.UNDEFINED;
		
		spritesMgr = new DrawManager();
		yGround = screen.getHeight() - (yLowLimit + screen.getTileSize() * 2);
		yRoof = yTopLimit;
	}
	
	/**
	 * Constructor that sets up entity default values (picked up from 
	 * <code>Logics</code>), defines it's bounds in the environment and allows to set it's
	 * starting position.
	 * 
	 * @param l the logics handler which the entity is linked to
	 * @param position the starting position of the entity in the environment
	 */
	protected EntityInstance(final Logics l, final Pair<Double,Double> position) {
		this(l);
		this.position = position;
		this.startPos = position.clone();
	}
	
	/**
	 * Allows to set the entity visibility.
	 * 
	 * @param v <code>true</code> if entity has to be shown, <code>false</code> if entity has to be hidden
	 */
	protected void setVisibility(final boolean v) {
		visible = v;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public boolean isOnScreenBounds() {
		return onScreen;
	}
	
	public boolean isOnClearArea() {
		return this.onClearArea;
	}
	
	public boolean isOnSpawnArea() {
		return this.onSpawnArea;
	}
	
	public Pair<Double,Double> getPosition(){
		return position;
	}
	
	public double getX(){
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
	
	public Set<Hitbox> getHitbox() {
		return this.hitboxSet;
	}
	
	public EntityType entityType() {
		return entityTag;
	}
	
	public void reset() {
		position.setX(startPos.getX());
		position.setY(startPos.getY());
	}
	
	public void clean() {
		this.reset();
		cleaner.accept(t -> this.entityType() == t, e -> this == e);
	}
	
	/**
	 * Updates the entity's flags.
	 */
	private void updateFlags() {
		if(position.getX() >= -screen.getTileSize() && position.getX() <= screen.getWidth() && position.getY() >= 0 && position.getY() <= screen.getHeight()) {
			onScreen = true;
			onClearArea = false;
			onSpawnArea = false;
		} else {
			if(position.getX() < -screen.getTileSize()) {
				onClearArea = true;
				onSpawnArea = false;
			} else if(position.getX() >= screen.getWidth()){
				onClearArea = false;
				onSpawnArea = true;
			} else {
				onClearArea = false;
				onSpawnArea = false;
			}
			onScreen = false;
		}
	}
	
	public void update() {
		currentFPS = GameWindow.fps;
		updateFlags();
	}
	
	public void draw(final Graphics2D g) {
		if(this.isVisible()) {
			spritesMgr.drawCurrentSprite(g, position, screen.getTileSize());
		}
	}
	
	public void drawCoordinates(final Graphics2D g) {
		if(GameWindow.debugger.isFeatureEnabled(Debugger.Option.ENTITY_COORDINATES) && this.isVisible()) {
			g.setColor(Debugger.debugColor);
			g.setFont(Debugger.debugFont);
			g.drawString("X:" + Math.round(this.getX()), Math.round(this.getX()) + Math.round(screen.getTileSize() * 0.88), Math.round(this.getY()) + Math.round(screen.getTileSize()));
			g.drawString("Y:" + Math.round(this.getY()), Math.round(this.getX()) + Math.round(screen.getTileSize() * 0.88), 10 + Math.round(this.getY()) + Math.round(screen.getTileSize()));
		}
	}
	
	public String toString() {
		return entityType().toString() + "[X:" + (int)getX() + "-Y:" + (int)getY() + "]";
	}
}
