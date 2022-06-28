package game.logics.interactions;

import game.frame.GameWindow;
import game.logics.handler.AbstractLogics;

/**
 * The {@link SpeedHandler} class is used to regulate 
 * the horizontal movement behaviour of entities.
 */
public class SpeedHandler {

    private final double xStartSpeed;
    private double xSpeed;
    private final double xSpeedIncDifficulty;
    private final double xAcceleration;

    /**
     * Defines a new {@link SpeedHandler} with the specified speed values.
     * 
     * @param xSpeed the starting speed,
     * @param xSpeedIncDifficulty the amount of speed to increase for each difficulty level,
     * @param xAcceleration the amount of speed to increase each time the entities moves.
     */
    public SpeedHandler(final double xSpeed, final double xSpeedIncDifficulty, final double xAcceleration) {
        this.xStartSpeed = xSpeed;
        this.xSpeed = xStartSpeed + xSpeedIncDifficulty * AbstractLogics.getDifficultyLevel();
        this.xSpeedIncDifficulty = xSpeedIncDifficulty;
        this.xAcceleration = xAcceleration;
    }

    /**
     * @return the current speed of the entity
     */
    public double getXSpeed() {
        return xSpeed + xSpeedIncDifficulty * AbstractLogics.getDifficultyLevel();
    }
    /**
     * Applies the speed acceleration to the current speed.
     */
    public void applyAcceleration() {
        xSpeed += xAcceleration / GameWindow.FPS_LIMIT;
    }
    /**
     * Reset the current speed to the default starting speed.
     */
    public void resetSpeed() {
        xSpeed = xStartSpeed;
    }
    /**
     * @return a copy of this object.
     */
    public SpeedHandler copy() {
        return new SpeedHandler(xStartSpeed, xSpeedIncDifficulty, xAcceleration);
    }
}
