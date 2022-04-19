package game.logics.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.json.simple.parser.ParseException;

import game.logics.entities.obstacles.missile.Missile;
import game.logics.entities.obstacles.zapper.ZapperBase;
import game.logics.entities.obstacles.zapper.ZapperRay;
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
	 * Sets the function for creating <code>ZapperRay</code> objects.
	 * 
	 * @param zapperb a function for creating <code>ZapperRay</code>
	 */
	void setZapperRayCreator(BiFunction<Pair<ZapperBase,ZapperBase>,Pair<Double,Double>,ZapperRay> zapperr);
	
	/**
	 * Sets the function for creating <code>ZapperBase</code> objects.
	 * 
	 * @param zapperb a function for creating <code>ZapperBase</code>
	 */
	void setZapperBaseCreator(Function<Pair<Double,Double>,ZapperBase> zapperb);
	
	/**
	 * Sets the function for creating <code>Missile</code> objects.
	 * 
	 * @param missile a function for creating <code>Missile</code>
	 */
	void setMissileCreator(Function<Pair<Double,Double>,Missile> missile);
	
	/**
	 * Removes all entities that are on the "clear area" [x < -tile size].
	 */
	void cleanTiles();
	
	/**
	 * @return <code>true</code> if the spawner is running, <code>false</code> if the spawner is not initiated or has ended his execution
	 */
	boolean isRunning();
	
	/**
	 * @return <code>true</code> if the spawner is waiting, <code>false</code> if not
	 */
	boolean isWaiting();
	
	/**
	 * Loads up all required information for the generation of obstacles.
	 * 
	 * @throws ParseException if parser fails to parse into objects the contents of json file
	 * @throws IOException if json file reading fails
	 * @throws FileNotFoundException if json file cannot be found
	 */
	void initialize() throws FileNotFoundException, IOException, ParseException;
	
	/**
	 * Start the generation of obstacles [Starts thread].
	 */
	void start();
	
	/**
	 * Stops the generation of obstacles [Ends thread].
	 */
	void stop();
	
	/**
	 * Pauses the generation of obstacles [Interrupts thread].
	 */
	void pause();
	
	/**
	 * Resumes the generation of obstacles if it was paused before [Resumes thread].
	 */
	void resume();
	
}
