package game.logics.hitbox;

import java.util.Set;

import game.logics.entities.obstacles.zapper.ZapperBase;
import game.logics.entities.obstacles.zapper.ZapperRay;
import game.utility.other.Pair;

// TODO: add javadoc
/**
 * .
 */
public class ZapperHitbox extends HitboxInstance {
    // TODO: complete javadoc
   /**
    * .
    * @param base1
    * @param base2
    * @param rays
    * @param startingPos
    */
    public ZapperHitbox(final ZapperBase base1, final ZapperBase base2,
        final Set<ZapperRay> rays, final Pair<Double, Double> startingPos) {
        super(startingPos);
        rays.forEach(entity -> super.rectangles.addAll(entity.getHitbox().getRectangles()));
        super.rectangles.addAll(base1.getHitbox().getRectangles());
        super.rectangles.addAll(base2.getHitbox().getRectangles());
    }
}
