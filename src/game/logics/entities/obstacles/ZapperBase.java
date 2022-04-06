package game.logics.entities.obstacles;

/**
 * The <code>ZapperBase</code> interface is used for accessing <code>ZapperBaseInstance</code> methods.
 * 
 * The class <code>ZapperBaseInstance</code> represents one part of the most common
 * type of obstacle that can be encountered during the game.
 * 
 * <code>ZapperBase</code> is one of the two farthest point of a Zapper obstacle, an electric trap
 * that can be get the player killed when he hits it.
 * Each Zapper is composed by 2 <code>ZapperBase</code> and as many <code>ZapperRay</code> as
 * the size of the trap.
 * 
 * Each <code>ZapperBaseInstance</code> needs to be paired to another <code>ZapperBaseInstance</code>.
 * 
 * @author Daniel Pellanda
 */
public interface ZapperBase extends Obstacle{
	
	/**
	 * Sets the paired <code>ZapperBaseInstance</code> if
	 * it hasn't been set yet.
	 * 
	 * @param zap the <code>ZapperBaseInstance</code> to pair with this object
	 */
	void setPaired(final ZapperBase zap);
	
	//void setRotation(final String rotation);
	
//	/**
//	 * Gets the <code>ZapperBaseInstance</code> of which this object is
//	 * paired with.
//	 * 
//	 * @return the paired <code>ZapperBaseInstance</code>
//	 */
//	ZapperBase getPaired();
}
