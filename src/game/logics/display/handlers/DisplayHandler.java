package game.logics.display.handlers;

import game.utility.other.GameState;
import game.utility.other.Pair;

public interface DisplayHandler {
	
	public GameState handle() ;
	
	public Pair<String,Integer> getSelectedOption();
}
