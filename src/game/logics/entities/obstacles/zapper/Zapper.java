package game.logics.entities.obstacles.zapper;

import java.util.Set;

import game.logics.entities.generic.Entity;
import game.logics.entities.obstacles.generic.Obstacle;
import game.utility.other.Pair;

public interface Zapper extends Obstacle{
	
	ZapperBase getPaired(ZapperBase z);
	
	Pair<ZapperBase,ZapperBase> getBothBases();
	
	Set<Entity> getEntitiesSet();
}
