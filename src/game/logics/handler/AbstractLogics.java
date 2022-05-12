package game.logics.handler;

import java.awt.Graphics2D;

public abstract class AbstractLogics implements Logics {

    /**
     * The frames passed since the last second.
     */
    private static int frameTime;

    private static int difficultyLevel = 1;

    public static int getDifficultyLevel() {
        return AbstractLogics.difficultyLevel;
    }

    static void setDifficultyLevel(final int newDifficultyLevel) {
        AbstractLogics.difficultyLevel = newDifficultyLevel;
    }

    public static int getFrameTime() {
        return AbstractLogics.frameTime;
    }

    static void updateTimer() {
        AbstractLogics.frameTime++;
    }

    /**
     * {@inheritDoc}
     */
    public abstract void updateAll();

    /**
     * {@inheritDoc}
     */
    public abstract void drawAll(Graphics2D g);

}
