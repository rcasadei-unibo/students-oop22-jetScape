package game.logics.entities;

import java.awt.Graphics2D;

public abstract interface Entity {
		
	void update();
	
	void draw(Graphics2D g);
}
