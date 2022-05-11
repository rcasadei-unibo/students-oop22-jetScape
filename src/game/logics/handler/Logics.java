package game.logics.handler;

import java.awt.Graphics2D;
import java.util.Date;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import game.logics.entities.generic.Entity;
import game.utility.other.EntityType;

/**
 * The <code>Logics</code> interface is used for accessing <code>LogicsHandler</code> methods.
 * 
 * <p>
 * The <code>LogicsHandler</code> class helps <class>GameWindow</class> to update
 * and draw logical parts of the game like the Interface, Entities, Collisions, etc....
 * </p>
 * 
 * @author Daniel Pellanda
 */
public interface Logics {
	
	static int getDifficultyLevel() {
		return LogicsHandler.difficultyLevel;
	}
	
	static int getFrameTime() {
		return LogicsHandler.frameTime;
	}
	
	BiConsumer<Predicate<EntityType>,Predicate<Entity>> getEntitiesCleaner();
	
	/**
	 * Updates all the logical objects handled for a frame.
	 */
	void updateAll();
	
	/**
	 * Draws all visible and drawable object handled for a frame.
	 * @param g the graphics drawer
	 */
	void drawAll(Graphics2D g);

	/**
	 * This class models a GameID, a game identifier that is used to refer to the actual game.
	 * @author davide
	 *
	 */
	public class GameID {

		private int gameNumber = 0;
		private Optional<Date> gameDate = Optional.empty();
		private boolean gamePlayed = false;
		
		/**
		 * Builds a new GameID when the game begins.
		 */
		public GameID() {
			
		}
		
		public int getGameID() {
			return this.gameNumber;
		}
		
		public Optional<Date> getGameDate() {
			return this.gameDate;
		}
		
		private int setGameID() {
			return this.gameNumber;
		}
		
		protected void generateNewGameID() {
			this.gameNumber = this.setGameID() + 1;
			this.gameDate = Optional.of(new Date());
		}
		
		public void setGamePlayed() {
			this.gamePlayed = true;
		}
		
		public boolean isGamePlayed() {
			return this.gamePlayed;
		}
	}
}
