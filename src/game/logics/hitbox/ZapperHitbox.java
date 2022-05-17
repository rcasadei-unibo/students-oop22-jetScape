package game.logics.hitbox;

import java.util.Set;

import game.logics.entities.obstacles.zapper.ZapperBase;
import game.logics.entities.obstacles.zapper.ZapperRay;
import game.utility.other.Pair;

public class ZapperHitbox extends HitboxInstance {
	
	public ZapperHitbox(final ZapperBase base1, final ZapperBase base2,
			final Set<ZapperRay> rays, Pair<Double, Double> startingPos) {
		super(startingPos);
		rays.forEach(entity -> super.rectangles.addAll(entity.getHitbox().getRectangles()));
		super.rectangles.addAll(base1.getHitbox().getRectangles());
		super.rectangles.addAll(base2.getHitbox().getRectangles());		
	}

}
