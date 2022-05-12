package game.logics.entities.obstacles.zapper;

import game.logics.entities.obstacles.generic.Obstacle;

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
     * Sets the master class where all the zapper entities are managed.
     * 
     * @param zap the <code>ZapperBaseInstance</code> to pair with this object
     */
    void setMaster(Zapper zap);
}
