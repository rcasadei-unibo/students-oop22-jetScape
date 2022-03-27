package game.logics.interactions;

import java.util.function.BiFunction;
import java.util.function.Function;

import game.logics.entities.obstacles.ZapperBase;
import game.logics.entities.obstacles.ZapperRay;
import game.utility.other.Pair;

public interface Generator extends Runnable{
	
	void setZapperBaseCreator(Function<Pair<Double,Double>,ZapperBase> zapperb);
	
	void setZapperRayCreator(BiFunction<Pair<ZapperBase,ZapperBase>,Pair<Double,Double>,ZapperRay> zapperr);
	
	boolean isWorking();
	
	void initialize();
	
	void stop();
	
	void start();
	
	void run();
}
