package game.logics.entities.obstacles.zapper;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import game.logics.entities.generic.Entity;
import game.logics.hitbox.Hitbox;
import game.logics.hitbox.ZapperHitbox;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The class {@link ZapperInstance} is used for group up all the part of a zapper ({@link ZapperBase} and {@link ZapperRay}).
 */
public class ZapperInstance implements Zapper {

    private final ZapperBase base1;
    private final ZapperBase base2;
    private final Set<ZapperRay> rays;

    private final Hitbox hitbox;

    /**
     * @param base1 the first {@link ZapperBase} of the zapper
     * @param base2 the second {@link ZapperBase} of the zapper
     * @param rays the rest of {@link ZapperRay} of the zapper
     */
    public ZapperInstance(final ZapperBase base1, final ZapperBase base2, final Set<ZapperRay> rays) {
        this.base1 = base1;
        this.base2 = base2;
        this.rays = rays;
        this.hitbox = new ZapperHitbox(base1, base2, rays, base1.getPosition());
    }

    /**
     * {@inheritDoc}
     */
    public ZapperBase getPaired(final ZapperBase z) {
        if (z.equals(base1)) {
            return base2;
        }
        return base1;
    }

    /**
     * {@inheritDoc}
     */
    public Pair<ZapperBase, ZapperBase> getBothBases() {
        return new Pair<>(base1, base2);
    }
    /**
     * {@inheritDoc}
     */
    public Set<Entity> getEntitiesSet() {
        final Set<Entity> entities = new HashSet<>(rays);
        entities.add(base1);
        entities.add(base2);
        return entities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpeedHandler getSpeedHandler() {
        return base1.getSpeedHandler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getPosition() {
        return base1.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType entityType() {
        return EntityType.ZAPPER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnClearArea() {
        if (!base1.isOnClearArea() || !base2.isOnClearArea()) {
            return false;
        }
        for (final ZapperRay r : rays) {
            if (!r.isOnClearArea()) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnSpawnArea() {
        if (!base1.isOnSpawnArea() || !base2.isOnSpawnArea()) {
            return false;
        }
        for (final ZapperRay r : rays) {
            if (!r.isOnSpawnArea()) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnScreenBounds() {
        if (!base1.isOnScreenBounds() || !base2.isOnScreenBounds()) {
            return false;
        }
        for (final ZapperRay r : rays) {
            if (!r.isOnScreenBounds()) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVisible() {
        if (!base1.isVisible() || !base2.isVisible()) {
            return false;
        }
        for (final ZapperRay r : rays) {
            if (!r.isVisible()) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hitbox getHitbox() {
        return this.hitbox;
    }

    /**
    * {@inheritDoc}
    */
    public void clean() {
        base1.clean();
        base2.clean();
        rays.forEach(r -> r.clean());
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        base1.reset();
        base2.reset();
        rays.forEach(r -> r.reset());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        base1.update();
        base2.update();
        rays.forEach(r -> r.update());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(final Graphics2D g) {
        base1.draw(g);
        base2.draw(g);
        rays.forEach(r -> r.draw(g));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawCoordinates(final Graphics2D g) {
        base1.drawCoordinates(g);
        base2.drawCoordinates(g);
        rays.forEach(r -> r.drawCoordinates(g));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return entityType().toString() + "[X:" + Math.round(base1.getPosition().getX()) + "-Y:" + Math.round(base1.getPosition().getY()) + "]";
    }
}
