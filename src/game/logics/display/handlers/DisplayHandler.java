package game.logics.display.handlers;

import game.utility.other.Pair;

public interface DisplayHandler {
	
	public void handle() ;
	
	public Pair<String,Integer> getSelectedOption();
}
