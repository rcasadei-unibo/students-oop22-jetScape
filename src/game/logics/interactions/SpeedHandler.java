package game.logics.interactions;

import game.frame.GameWindow;
import game.logics.handler.Logics;

public class SpeedHandler {
    
    public static final double baseXSpeed = 250;
    public static final double baseXSpeedIncDiff = 10;
    public static final double baseXAcceleration = 0;
    
    private double xStartSpeed = 0;
    private double xSpeed = 0;
    private double xSpeedIncDifficulty = 0;
    private double xAcceleration = 0;
    
    public SpeedHandler() {
        setDefaultValues();
    }
    
    public SpeedHandler(final double xSpeed, final double xSpeedIncDifficulty, final double xAcceleration) {
        this.xStartSpeed = xSpeed;
        this.xSpeed = xStartSpeed + xSpeedIncDifficulty * Logics.getDifficultyLevel();
        this.xSpeedIncDifficulty = xSpeedIncDifficulty;
        this.xAcceleration = xAcceleration;
    }
    
    public void setDefaultValues() {
        this.xSpeed = baseXSpeed;
        this.xSpeedIncDifficulty = baseXSpeedIncDiff;
        this.xAcceleration = baseXAcceleration;
    }
    
    public double getXSpeed() {
        return xSpeed + xSpeedIncDifficulty * Logics.getDifficultyLevel();
    }
    
    public void applyAcceleration() {
        xSpeed += xAcceleration / GameWindow.fpsLimit;
    }
    
    public void resetSpeed() {
        xSpeed = xStartSpeed;
    }
    
    public SpeedHandler clone() {
        return new SpeedHandler(xStartSpeed, xSpeedIncDifficulty, xAcceleration);
    }
}
