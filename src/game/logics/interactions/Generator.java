package game.logics.interactions;

import java.util.function.BiFunction;
import java.util.function.Function;

import game.logics.entities.obstacles.ZapperBase;
import game.logics.entities.obstacles.ZapperRay;
import game.utility.other.Pair;

/**
 * The <code>Generator</code> interface can be used for accessing <code>TileGenerator</code> methods.
 * 
 * The class <code>TileGenerator</code> handles the generation of tiles of
 * obstacles during the game.
 * 
 * <code>TileGenerator</code> works on a separated thread which can be manually
 * controlled by the <code>LogicsHandler</code>.
 * 
 * @author Daniel Pellanda
 */
public interface Generator extends Runnable{
	
	/**
	 * Sets the function for creating <code>ZapperBase</code> objects.
	 * 
	 * @param zapperb a function for creating <code>ZapperBase</code>
	 */
	void setZapperBaseCreator(Function<Pair<Double,Double>,ZapperBase> zapperb);
	
	/**
	 * Sets the function for creating <code>ZapperRay</code> objects.
	 * 
	 * @param zapperb a function for creating <code>ZapperRay</code>
	 */
	void setZapperRayCreator(BiFunction<Pair<ZapperBase,ZapperBase>,Pair<Double,Double>,ZapperRay> zapperr);
	
	/**
	 * @return <code>true</code> if the spawner is working, <code>false</code> if the spawner is paused or not initiated
	 */
	boolean isWorking();
	
	/**
	 * Begins the generation of obstacles [Starts thread].
	 */
	void initialize();
	
	/**
	 * Stops the generation of obstacles [Ends thread].
	 */
	void stop();
	
	/**
	 * Resumes the generation of obstacles [Resumes thread].
	 */
	void start();
}
