package game.logics.entities;

import game.logics.interactions.SpeedHandler;

public interface ZapperBase extends Entity{
	
	void setPaired(final ZapperBase zap);
	
	void setRotation(final String rotation);
	
	ZapperBase getPaired();
	
	SpeedHandler getSpeedHandler();
}
