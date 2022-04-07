package game.logics.entities.obstacles;

/**
 * The <code>ZapperRay</code> interface is used for accessing <code>ZapperRayInstance</code> methods.
 * 
 * The class <code>ZapperRayInstance</code> represents one part of the most common
 * type of obstacle that can be encountered during the game.
 * 
 * <code>ZapperRay</code> is one of the central parts of a Zapper obstacle, an electric trap
 * that can be get the player killed when he hits it.
 * Each Zapper is composed by 2 <code>ZapperBase</code> and as many <code>ZapperRay</code> as
 * the size of the trap.
 * 
 * Each <code>ZapperRayInstance</code> needs to be paired to the 2 <code>ZapperBaseInstance</code> that compose
 * the trap.
 * 
 * @author Daniel Pellanda
 */
public interface ZapperRay extends Obstacle{
	
}
