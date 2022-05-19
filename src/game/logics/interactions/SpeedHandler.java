package game.logics.interactions;

import game.frame.GameWindow;
import game.logics.handler.AbstractLogics;

// TODO: Rebuild this class and add complete javadoc
/**
 * PLACEHOLDER JAVADOC.
 */
public class SpeedHandler implements Cloneable {

//    public static final double baseXSpeed = 250;
//    public static final double baseXSpeedIncDiff = 10;
//    public static final double baseXAcceleration = 0;

    private final double xStartSpeed;
    private double xSpeed;
    private final double xSpeedIncDifficulty;
    private final double xAcceleration;

//    public SpeedHandler() {
//        setDefaultValues();
//    }

    /**
     * @param xSpeed
     * @param xSpeedIncDifficulty
     * @param xAcceleration
     */
    public SpeedHandler(final double xSpeed, final double xSpeedIncDifficulty, final double xAcceleration) {
        this.xStartSpeed = xSpeed;
        this.xSpeed = xStartSpeed + xSpeedIncDifficulty * AbstractLogics.getDifficultyLevel();
        this.xSpeedIncDifficulty = xSpeedIncDifficulty;
        this.xAcceleration = xAcceleration;
    }

//    public void setDefaultValues() {
//        this.xSpeed = baseXSpeed;
//        this.xSpeedIncDifficulty = baseXSpeedIncDiff;
//        this.xAcceleration = baseXAcceleration;
//    }

    /**
     * @return the current speed of the entity
     */
    public double getXSpeed() {
        return xSpeed + xSpeedIncDifficulty * AbstractLogics.getDifficultyLevel();
    }
    /**
     * 
     */
    public void applyAcceleration() {
        xSpeed += xAcceleration / GameWindow.FPS_LIMIT;
    }
    /**
     * 
     */
    public void resetSpeed() {
        xSpeed = xStartSpeed;
    }
    /**
     * @return a copy of this class
     */
    public SpeedHandler copy() {
        return new SpeedHandler(xStartSpeed, xSpeedIncDifficulty, xAcceleration);
    }
}
