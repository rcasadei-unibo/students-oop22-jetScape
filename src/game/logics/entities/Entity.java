package game.logics.entities;

import java.awt.Graphics2D;

public interface Entity {
		
	static final double yTopLimit = 54.0;
	static final double yLowLimit = 65.0;
	
	double getX();
	
	double getY();
	
	String entityType();
	
	void update();
	
	void draw(Graphics2D g);
}
