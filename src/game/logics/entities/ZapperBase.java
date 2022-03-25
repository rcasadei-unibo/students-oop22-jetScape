package game.logics.entities;

public interface ZapperBase extends Obstacle{
	
	void setPaired(final ZapperBase zap);
	
	//void setRotation(final String rotation);
	
	ZapperBase getPaired();
}
