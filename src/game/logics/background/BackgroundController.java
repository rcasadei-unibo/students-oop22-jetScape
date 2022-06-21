package game.logics.background;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import game.frame.GameWindow;
import game.logics.interactions.SpeedHandler;
import game.utility.debug.Debugger;
import game.utility.other.Pair;

/**
 * This class is a {@link Background} handler.
 */
public class BackgroundController implements Background {

    /**
    * Specifies the path within the sprite folder [specified in {@link game.utility.sprites.Sprite Sprite} class]
     * where {@link ShieldInstance} sprites can be found.
     */
    private static final String SPRITE_PATH = "background" + System.getProperty("file.separator");

    private static final String KEY_SPRITE1 = "background1";
    private static final String KEY_SPRITE2 = "background2";

    /**
     * If sprites are missing, they will be replace by a rectangle of the color specified in
     * <code>{@link ShieldInstance}.PLACE_HOLDER</code>.
     */
    private static final Color PLACE_HOLDER = Color.black;

    /// FLAGS ///
    private boolean visible;
    private boolean onScreen;
    private boolean onClearArea;

    //private static final int leftBorderOffset = 5;
    private static final int SCREEN_WIDTH = GameWindow.GAME_SCREEN.getWidth();
    private static final Pair<Double, Double> LEFT_START_POS = new Pair<>(0.0, 0.0);
    //private static final Pair<Double, Double> RIGHT_START_POS = new Pair<>((double) SCREEN_WIDTH, 0.0);

    private final Pair<Double, Double> leftPosition = LEFT_START_POS.copy();
    //private final Pair<Double, Double> rightPosition = RIGHT_START_POS;

    private final SpeedHandler movement;

    private final BackgroundDrawer drawMgr = new BackgroundDrawManager();

    /**
     * @param speed the {@link SpeedHandler} to use for the pickup
     */
    public BackgroundController(final SpeedHandler speed) {

        this.movement = speed;
        this.drawMgr.setPlaceH(PLACE_HOLDER);
        this.drawMgr.addSprite(KEY_SPRITE1, SPRITE_PATH + "background_1.png");
        this.drawMgr.addSprite(KEY_SPRITE2, SPRITE_PATH + "background_2.png");

        this.setVisibility(true);
    }

    /**
     * Allows to set the background visibility.
     * 
     * @param v <code>true</code> if has to be shown, <code>false</code> if has to be hidden
     */
    private void setVisibility(final boolean v) {
        visible = v;
    }

    private boolean isVisible() {
        return visible;
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        this.leftPosition.set(LEFT_START_POS.getX(), LEFT_START_POS.getY());
        //this.rightPosition.set(RIGHT_START_POS.getX(), RIGHT_START_POS.getY());
        this.movement.resetSpeed();
    }

    /**
     * {@inheritDoc}
     */
    public void update() {
        this.updateFlags();

        if (this.leftPosition.getX() > -SCREEN_WIDTH * 2) {
            this.leftPosition.setX(this.leftPosition.getX() - this.movement.getXSpeed() / GameWindow.FPS_LIMIT);
            //this.rightPosition.setX(this.rightPosition.getX() - this.movement.getXSpeed() / GameWindow.FPS_LIMIT);
        }/* else if (this.isLeftOnClearArea()) {
            //final double tempRight = this.leftPosition.getX();
            //this.leftPosition.setX(this.rightPosition.getX());
            //this.rightPosition.setX(tempRight + SCREEN_WIDTH);
        }*/
    }

    /**
     * Draws the background if visible.
     * 
     * @param g the graphics drawer
     */
    public void draw(final Graphics2D g) {
        if (this.isVisible()) {
            if (this.onClearArea) {
                final Pair<Double, Double> temp = this.calculateRightPosition();
                this.leftPosition.set(temp.getX(), temp.getY());
                this.onClearArea = false;
            }
            this.drawMgr.drawSprite(g, KEY_SPRITE1, this.leftPosition,
                    GameWindow.GAME_SCREEN.getHeight(),
                    GameWindow.GAME_SCREEN.getWidth());
            if (this.onScreen) {
                this.drawMgr.drawSprite(g, KEY_SPRITE2, this.calculateRightPosition(),
                     GameWindow.GAME_SCREEN.getHeight(),
                     GameWindow.GAME_SCREEN.getWidth());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void drawCoordinates(final Graphics2D g) {
        final int xShift = (int) Math.round(leftPosition.getX()) + (int) Math.round(GameWindow.GAME_SCREEN.getTileSize() * 0.88);
        final int yShiftDrawnX = (int) Math.round(leftPosition.getY()) + GameWindow.GAME_SCREEN.getTileSize();
        final int yShiftDrawnY = yShiftDrawnX + 10;

        if (GameWindow.GAME_DEBUGGER.isFeatureEnabled(Debugger.Option.ENTITY_COORDINATES) && this.isVisible()) {
            g.setColor(Debugger.DEBUG_COLOR);
            g.setFont(Debugger.DEBUG_FONT);

            g.drawString("X:" + Math.round(leftPosition.getX()), xShift, yShiftDrawnX);
            g.drawString("Y:" + Math.round(leftPosition.getY()), xShift, yShiftDrawnY);
        }
    }

    private Pair<Double, Double> calculateRightPosition() {
        return new Pair<Double, Double>(this.leftPosition.getX() + SCREEN_WIDTH, this.leftPosition.getY());
    }

        /**
         * {@inheritDoc}
         */
        private boolean isOnScreenBounds() {
            return onScreen;
        }


    private boolean isOnClearArea() {
        return this.onClearArea;
    }

    /**
     * Updates the entity's flags.
     */
    private void updateFlags() {
        if (leftPosition.getX() <= Math.abs(GameWindow.GAME_SCREEN.getWidth())
                && leftPosition.getY() >= 0 && leftPosition.getY() <= GameWindow.GAME_SCREEN.getHeight()) {
            onScreen = true;
            onClearArea = false;
        } else {
            if (leftPosition.getX() < -GameWindow.GAME_SCREEN.getTileSize()) {
                onClearArea = true;
            } else if (leftPosition.getX() >= GameWindow.GAME_SCREEN.getWidth()) {
                onClearArea = false;
            } else {
                onClearArea = false;
            }
            onScreen = false;
        }
    }

    /**
     * @return a string representing the type of entity with his coordinates in the environment
     */
    @Override
    public String toString() {
        return "Background"
                 + "[L: X:" + Math.round(leftPosition.getX())
                 +  " - Y:" + Math.round(leftPosition.getY()) + "]\n" + "           "
                 + "[R: X:" + Math.round(this.calculateRightPosition().getX())
                 +  " - Y:" + Math.round(this.calculateRightPosition().getY()) + "]";
    }
}
