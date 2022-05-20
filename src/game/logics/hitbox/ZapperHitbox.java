package game.logics.hitbox;

import java.util.Set;

import game.logics.entities.obstacles.zapper.ZapperBase;
import game.logics.entities.obstacles.zapper.ZapperRay;
import game.utility.other.Pair;

/**
 * The {@link ZapperHitbox} represents a group of hitbox composed 
 * by 2 {@link ZapperBase} and N {@link ZapperRay}.
 */
public class ZapperHitbox extends HitboxInstance {
   /**
    * initializes the {@link Hitbox} group of specified {@link ZapperBase} and
    * {@link ZapperRay}. 
    * 
    * @param base1
    * @param base2
    * @param rays
    * @param startingPos
    */
    public ZapperHitbox(final ZapperBase base1, final ZapperBase base2,
        final Set<ZapperRay> rays, final Pair<Double, Double> startingPos) {
        super(startingPos);
        rays.forEach(entity -> addHitbox(entity.getHitbox()));
        addHitbox(base1.getHitbox());
        addHitbox(base2.getHitbox());
    }
}
