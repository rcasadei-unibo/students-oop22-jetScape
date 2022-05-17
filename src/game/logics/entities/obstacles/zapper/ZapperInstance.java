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

public class ZapperInstance implements Zapper{
    
    private final ZapperBase base1;
    private final ZapperBase base2;
    private final Set<ZapperRay> rays;
    private final Hitbox hitbox;
    
    public ZapperInstance(final ZapperBase base1, final ZapperBase base2, final Set<ZapperRay> rays) {
        this.base1 = base1;
        this.base2 = base2;
        this.rays = rays;
        this.hitbox = new ZapperHitbox(base1, base2, rays, base1.getPosition());
    }
    
    public ZapperBase getPaired(final ZapperBase z) {
        if(z.equals(base1)) {
            return base2;
        }
        return base1;
    }
    
    public Pair<ZapperBase,ZapperBase> getBothBases(){
        return new Pair<>(base1,base2);
    }
    
    public Set<Entity> getEntitiesSet(){
        Set<Entity> entities = new HashSet<>(rays);
        entities.add(base1);
        entities.add(base2);
        return entities;
    }

    @Override
    public SpeedHandler getSpeedHandler() {
        return base1.getSpeedHandler();
    }
    

    @Override
    public Pair<Double, Double> getPosition() {
        return base1.getPosition();
    }
    
    @Override
    public double getX() {
        return base1.getX();
    }

    @Override
    public double getY() {
        return base1.getY();
    }

    @Override
    public EntityType entityType() {
        return EntityType.ZAPPER;
    }
    
    @Override
    public boolean isOnClearArea() {
        if(!base1.isOnClearArea() || !base2.isOnClearArea()) {
            return false;
        }
        for(ZapperRay r : rays) {
            if(!r.isOnClearArea()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isOnSpawnArea() {
        if(!base1.isOnSpawnArea() || !base2.isOnSpawnArea()) {
            return false;
        }
        for(ZapperRay r : rays) {
            if(!r.isOnSpawnArea()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isOnScreenBounds() {
        if(!base1.isOnScreenBounds() || !base2.isOnScreenBounds()){
            return false;
        }
        for(ZapperRay r : rays) {
            if(!r.isOnScreenBounds()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isVisible() {
        if(!base1.isVisible() || !base2.isVisible()) {
            return false;
        }
        for(ZapperRay r : rays) {
            if(!r.isVisible()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public Hitbox getHitbox() {
        return this.hitbox;
    }    
    
    public void clean() {
        base1.clean();
        base2.clean();
        rays.forEach(r -> r.clean());
    }

    @Override
    public void reset() {
        base1.reset();
        base2.reset();
        rays.forEach(r -> r.reset());
    }

    @Override
    public void update() {
        base1.update();
        base2.update();
        rays.forEach(r -> r.update());
    }

    @Override
    public void draw(Graphics2D g) {
        base1.draw(g);
        base2.draw(g);
        rays.forEach(r -> r.draw(g));
    }

    @Override
    public void drawCoordinates(Graphics2D g) {
        base1.drawCoordinates(g);
        base2.drawCoordinates(g);
        rays.forEach(r -> r.drawCoordinates(g));
    }

    public String toString() {
        return entityType().toString() + "[X:"+(int)base1.getX()+"-Y:"+(int)base1.getY()+"]";
    }
}
