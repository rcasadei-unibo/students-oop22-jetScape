package game.logics.entities.obstacles.zapper;

import java.util.Set;

import game.logics.entities.generic.Entity;
import game.logics.entities.obstacles.generic.Obstacle;
import game.utility.other.Pair;

/**
 * An Interface for accessing {@link ZapperInstance} methods.
 * 
 * <p>
 * The class {@link ZapperInstance} is used for group up all the part of a zapper ({@link ZapperBase} and {@link ZapperRay}).
 * </p>
 */
public interface Zapper extends Obstacle {

    /**
     * @param z one of the two paired {@link ZapperBase}
     * @return the other {@link ZapperBase} paired
     */
    ZapperBase getPaired(ZapperBase z);

    /**
     * @return the {@link Pair} of {@link ZapperBase} contained in {@link ZapperInstance}
     */
    Pair<ZapperBase, ZapperBase> getBothBases();

    /**
     * @return a {@link Set} contain all the {@link ZapperBase} and {@link ZapperRay} contained in {@link Zapper}
     */
    Set<Entity> getEntitiesSet();
}
