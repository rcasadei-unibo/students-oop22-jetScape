package game.logics.handler;

import java.awt.Graphics2D;

import game.utility.other.GameState;

public interface DisplayHandler {
	
	public GameState handle() ;
	
	public void draw(Graphics2D g);
}
