package game.logics.entities.basic;

import game.frame.GameWindow;
import game.logics.handler.Logics;
import game.utility.screen.Pair;
import game.utility.screen.Screen;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;


public abstract class EntityInstance implements Entity{
	
	protected final int fps = GameWindow.fpsLimiter;
	protected final Screen screen;
	protected final double yGround;
	protected final double yRoof;
	
	protected Pair<Double, Double> position;
	protected String entityTag;
	
	private final boolean debug;
	private Font debugFont = new Font("Calibri", Font.PLAIN, 10);
	
	protected boolean visible = true;
	private boolean onScreen = true;
	
	protected EntityInstance(final Logics l) {
		this.screen = l.getScreenInfo();
		entityTag = "undefined";
		
		yGround = screen.getHeight() - (yLowLimit + screen.getTileSize());
		yRoof = yTopLimit;
		
		this.debug = l.isDebugModeOn();
	}
	
	protected EntityInstance(final Logics l, final Pair<Double,Double> position) {
		this(l);
		this.position = position;
	}
	
	private void updateOnScreen() {
		if(position.getX() >= -screen.getTileSize() && position.getX() <= screen.getWidth() && position.getY() >= 0 && position.getY() <= screen.getHeight()) {
			onScreen = true;
		} else {
			onScreen = false;
		}
	}
	
	protected boolean isOnScreenBounds() {
		return onScreen;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	protected void setVisibility(final boolean v) {
		visible = v;
	}
	
	protected int round(final double coordinate) {
		return (int)Math.round(coordinate);
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
	
	public void update() {
		updateOnScreen();
	}
	
	public void draw(Graphics2D g) {
		if(this.isVisible()) {
			g.fillRect(round(position.getX()), round(position.getY()), screen.getTileSize(), screen.getTileSize());
			
			if(debug) {
				g.setColor(Color.black);
				g.setFont(debugFont);
				g.drawString("X:" + round(position.getX()) + " Y:" + round(position.getY()), round(position.getX()) + round(screen.getTileSize() / (8 * Screen.tileScaling)), round(position.getY()) + round(screen.getTileSize() / (4 * Screen.tileScaling)));
			}
		}
	}
	
}
