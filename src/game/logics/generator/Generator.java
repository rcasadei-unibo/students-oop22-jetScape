package game.logics.generator;

import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.github.cliftonlabs.json_simple.JsonException;

import game.logics.entities.obstacles.missile.Missile;
import game.logics.entities.obstacles.zapper.ZapperBase;
import game.logics.entities.obstacles.zapper.ZapperRay;
import game.logics.entities.pickups.shield.Shield;
import game.logics.entities.pickups.teleport.Teleport;
import game.utility.other.FormatException;
import game.utility.other.Pair;

/**
 * The {@link Generator} interface can be used for accessing {@link TileGenerator} methods.
 * 
 * The class {@link TileGenerator} handles the generation of tiles during the game.
 * 
 * {@link TileGenerator} works on a separated thread which can be manually
 * controlled by the {@link game.logics.handler.LogicsHandler LogicsHandler}.
 * 
 */
public interface Generator extends Runnable {

    /**
     * Sets the function for creating <code>ZapperRay</code> objects.
     * 
     * @param zapperr a {@link BiFunction} for creating <code>ZapperRay</code>
     */
    void setZapperRayCreator(BiFunction<Pair<ZapperBase, ZapperBase>, Pair<Double, Double>, ZapperRay> zapperr);

    /**
     * Sets the function for creating <code>ZapperBase</code> objects.
     * 
     * @param zapperb a function for creating <code>ZapperBase</code>
     */
    void setZapperBaseCreator(Function<Pair<Double, Double>, ZapperBase> zapperb);

    /**
     * Sets the function for creating <code>Missile</code> objects.
     * 
     * @param missile a function for creating <code>Missile</code>
     */
    void setMissileCreator(Function<Pair<Double, Double>, Missile> missile);
    /**
     * Sets the function for creating <code>Shield</code> objects.
     * 
     * @param shield a function for creating <code>Shield</code>
     */
    void setShieldCreator(Function<Pair<Double, Double>, Shield> shield);
    /**
     * Sets the function for creating <code>Teleport</code> objects.
     * 
     * @param teleport a function for creating <code>Teleport</code>
     */
    void setTeleportCreator(Function<Pair<Double, Double>, Teleport> teleport);

    /**
     * Draws a timer that indicates how many milliseconds have to pass before the
     * next obstacle/pickup spawn.
     * 
     * @param g the {@link Graphics2D} used
     */
    void drawNextSpawnTimer(Graphics2D g);

    /**
     * @return <code>true</code> if the spawner is running, <code>false</code> if the spawner is not initiated or has ended his execution
     */
    boolean isRunning();

    /**
     * @return <code>true</code> if the spawner is waiting, <code>false</code> if not
     */
    boolean isWaiting();

    /**
     * Loads up all required information for the generation of entities.
     * 
     * @throws FormatException if parser fails to parse into objects the contents of json file
     * @throws JsonException if json file reading fails
     * @throws FileNotFoundException if json file cannot be found
     */
    void initialize() throws FileNotFoundException, JsonException, FormatException;
    /**
     * Start the generation of obstacles [Starts thread].
     */
    void start();
    /**
     * Terminates the generation of obstacles [Ends thread].
     */
    void terminate();
    /**
     * Stops the generation of obstacles [Interrupts thread].
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
