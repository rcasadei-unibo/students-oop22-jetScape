package game.logics.interactions;

public class SpeedHandler {
	
	public static final double baseXSpeed = 0;
	public static final double baseXSpeedIncDiff = 10;
	public static final double baseXAcceleration = 0;
	
	private double xSpeed = 0;
	private double xSpeedIncDifficulty = 0;
	private double xAcceleration = 0;
	
	public SpeedHandler() {
		setDefaultValues();
	}
	
	public SpeedHandler(final double xSpeed, final double xSpeedIncDifficulty, final double xAcceleration) {
		this.xSpeed = xSpeed;
		this.xSpeedIncDifficulty = xSpeedIncDifficulty;
		this.xAcceleration = xAcceleration;
	}
	
	public void setDefaultValues() {
		this.xSpeed = baseXSpeed;
		this.xSpeedIncDifficulty = baseXSpeedIncDiff;
		this.xAcceleration = baseXAcceleration;
	}
	
	public void setXSpeed(final double xSpeed) {
		this.xSpeed = xSpeed;
	}
	
	public void setXSpeedIncDiff(final double xSpeedIncDifficulty) {
		this.xSpeedIncDifficulty = xSpeedIncDifficulty;
	}
	
	public void setXAcceleration(final double xAcceleration) {
		this.xAcceleration = xAcceleration;
	}
	
	public double getXSpeed() {
		return xSpeed;
	}
	
	public double getXSpeedIncDiff() {
		return xSpeedIncDifficulty;
	}
	
	public double getXAcceleration() {
		return xAcceleration;
	}
}
