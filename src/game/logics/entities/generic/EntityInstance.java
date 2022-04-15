package game.logics.entities.generic;

import game.frame.GameWindow;
import game.logics.handler.Logics;
import game.utility.debug.Debugger;
import game.utility.other.Pair;
import game.utility.screen.Screen;
import game.utility.sprites.DrawManager;
import game.utility.sprites.Drawer;

import java.awt.Graphics2D;

/**
 * The abstract class <code>EntityInstance</code> is used to define all the common parts of each entity
 * like their position, entity relationship, visibility, on screen presence, etc...
 * 
 * @author Daniel Pellanda
 */
public abstract class EntityInstance implements Entity{
	
	protected final int maximumFPS = GameWindow.fpsLimit;
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
	protected String entityTag;
	
	/**
	 * Decides if the entity has to be shown on screen.
	 */
	protected boolean visible = true;
	/**
	 * A flag that automatically updates and tells if the entity's position between screen bounds.
	 */
	private boolean onScreen = false;
	
	/**
	 * Manages the sprites of the object.
	 */
	protected final Drawer spritesMgr;
	protected final Screen screen;
	private final Debugger debugger;
	
	/**
	 * Constructor that sets up entity default values (picked up from 
	 * <code>Logics</code>) and defines it's bounds in the environment.
	 * 
	 * @param l the logics handler which the entity is linked to
	 */
	protected EntityInstance(final Logics l) {
		this.screen = l.getScreenInfo();
		this.debugger = l.getDebugger();
		entityTag = "undefined";
		
		spritesMgr = new DrawManager();
		yGround = screen.getHeight() - (yLowLimit + screen.getTileSize());
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
	
	public Pair<Double,Double> getPosition(){
		return position;
	}
	
	public double getX(){
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
	
	public String entityType() {
		return entityTag;
	}
	
	public void reset() {
		position.setX(startPos.getX());
		position.setY(startPos.getY());
	}
	
	/**
	 * Updates the entity's flags.
	 */
	private void updateFlags() {
		if(position.getX() >= -screen.getTileSize() && position.getX() <= screen.getWidth() && position.getY() >= 0 && position.getY() <= screen.getHeight()) {
			onScreen = true;
		} else {
			onScreen = false;
		}
	}
	
	public void update() {
		currentFPS = debugger.fps();
		updateFlags();
	}
	
	public void draw(final Graphics2D g) {
		spritesMgr.drawSprite(g, position, screen.getTileSize());
	}
}
