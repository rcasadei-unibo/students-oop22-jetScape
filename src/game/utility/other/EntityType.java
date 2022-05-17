package game.utility.other;

import java.util.List;

public enum EntityType {
    UNDEFINED, PLAYER, OBSTACLE, MISSILE, ZAPPER, ZAPPERBASE, ZAPPERRAY, PICKUP, SHIELD, TELEPORT;
    
    public static final List<EntityType> allTypes = List.of(EntityType.PLAYER, EntityType.OBSTACLE, EntityType.ZAPPER, EntityType.ZAPPERBASE, EntityType.ZAPPERRAY, EntityType.MISSILE, EntityType.PICKUP, EntityType.SHIELD, EntityType.TELEPORT);
    public static final List<EntityType> genericTypes = List.of(EntityType.PLAYER, EntityType.ZAPPER, EntityType.MISSILE, EntityType.SHIELD, EntityType.TELEPORT);
    
    public boolean isGenerableEntity() {
        switch(this) {
            case OBSTACLE:
            case MISSILE:
            case ZAPPER:
            case ZAPPERBASE:
            case ZAPPERRAY:
            case PICKUP:
            case SHIELD:
            case TELEPORT:
                return true;
            default:
                break;
        }
        return false;
    }
    
    public boolean isObstacle() {
        switch(this) {
            case OBSTACLE:
            case MISSILE:
            case ZAPPER:
            case ZAPPERBASE:
            case ZAPPERRAY:
                return true;
            default:
                break;
        }
        return false;
    }
    
    public boolean isPickUp() {
        switch(this) {
            case PICKUP:
            case SHIELD:
            case TELEPORT:
                return true;
            default:
                break;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
