package game.utility.other;

import java.util.List;

public enum EntityType {
	UNDEFINED, PLAYER, OBSTACLE, ZAPPER, ZAPPERBASE, ZAPPERRAY, MISSILE, PICKUP, SHIELD, TELEPORT;
	
	public static final List<EntityType> allTypes = List.of(EntityType.PLAYER, EntityType.OBSTACLE, EntityType.ZAPPER, EntityType.ZAPPERBASE, EntityType.ZAPPERRAY, EntityType.MISSILE, EntityType.PICKUP, EntityType.SHIELD, EntityType.TELEPORT);
	public static final List<EntityType> concreteTypes = List.of(EntityType.PLAYER, EntityType.ZAPPERBASE, EntityType.ZAPPERRAY, EntityType.MISSILE, EntityType.SHIELD, EntityType.TELEPORT);
	public static final List<EntityType> concreteGenericTypes = List.of(EntityType.PLAYER, EntityType.ZAPPER, EntityType.MISSILE, EntityType.SHIELD, EntityType.TELEPORT);
	
	public boolean isGenerableEntity() {
		return this.ordinal() > 1;
	}
	
	public boolean isObstacle() {
		return this.ordinal() > 1 && this.ordinal() < 7;
	}
	
	public boolean isPickUp() {
		return this.ordinal() > 6;
	}
	
	public boolean isZapperDependent() {
		return this.ordinal() > 2 && this.ordinal() < 6;
	}
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}
