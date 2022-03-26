package game.logics.entities.basic;

import java.awt.Graphics2D;

public interface Entity {
		
	static final double yTopLimit = 0.0;
	static final double yLowLimit = 64.0;
	
	boolean isVisible();
	
	double getX();
	
	double getY();
	
	String entityType();
	
	void update();
	
	void draw(Graphics2D g);
}
