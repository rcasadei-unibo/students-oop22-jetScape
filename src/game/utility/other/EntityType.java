package game.utility.other;

public enum EntityType {
	UNDEFINED, PLAYER, OBSTACLE, ZAPPER, ZAPPERBASE, ZAPPERRAY, MISSILE;
	
	public boolean isMovingEntity() {
		return this.ordinal() > 1;
	}
	
	public boolean isObstacle() {
		return this.ordinal() > 1;
	}
	
	public boolean isZapperDependent() {
		return this.ordinal() > 2 && this.ordinal() < 6;
	}
	
	public String toString() {
		switch(this) {
			case PLAYER:
				return "player";
			case OBSTACLE:
				return "obstacle";
			case ZAPPER:
				return "zapper";
			case ZAPPERBASE:
				return "zapperbase";
			case ZAPPERRAY:
				return "zapperray";
			case MISSILE:
				return "missile";
			default:
				break;
		}
		return "undefined";
	}
}
