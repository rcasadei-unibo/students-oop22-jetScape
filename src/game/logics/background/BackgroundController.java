package game.logics.background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import game.frame.GameWindow;
import game.logics.interactions.SpeedHandler;
import game.utility.debug.Debugger;
import game.utility.other.Pair;

/**
 * This class is a {@link Background} handler.
 *
 * The correponding reference class for entity management is
 * {@link game.logics.entities.generic.EntityIstance EntityIstance}
 * implementation of {@link game.logics.entities.generic.Entity Entity}, from
 * which some methods are the same.
 *
 * These classes in fact share a similar behavior.
 */
public class BackgroundController implements Background {

    /**
     * Specifies the path within the sprite folder [specified in
     * {@link game.utility.sprites.AbstractSprite AbstractSprite} class]
     * where {@link BackgroundSprite} sprites can be found.
     */
    private static final String SPRITE_PATH = "background" + System.getProperty("file.separator");

    /**
     * If sprites are missing, they will be replaced by a rectangle of the color specified here.
     *
     *   HSV: 240° 33% 14.1%
     *   RGB: 24 24 36
     */
    private static final Color PLACE_HOLDER = Color.getHSBColor((float) 0.666, (float) 0.333, (float) 0.141);

    /**
     * Identifier for the first background, without ladders.
     */
    private static final String KEY_SPRITE1 = "background1";
    /**
     * Identifier for the second background, with ladders.
     */
    private static final String KEY_SPRITE2 = "background2";

    /**
     * Probability that {@link BackgroundController#KEY_SPRITE2} is used instead
     * of {@link BackgroundController#KEY_SPRITE1}.
     */
    private static final double LADDER_GENERATION = 0.15;

    private static final int SCREEN_WIDTH = GameWindow.GAME_SCREEN.getWidth();
    private static final Pair<Double, Double> START_POS = new Pair<>(0.0, 0.0);

    private final Pair<Double, Double> position = START_POS.copy();

    private final BackgroundDrawer drawMgr = new BackgroundDrawManager();
    private final Random rand = new Random();
    private final SpeedHandler movement;

    /// FLAGS ///
    private boolean visible;
    private boolean toBeGenerated;
    private boolean toBeShifted;
    private final Map<BoxPos, Boolean> boxVisible;
    private final Map<BoxPos, Optional<String>> boxSprite;

    /**
     * @param speed the {@link SpeedHandler} to use for the pickup
     */
    public BackgroundController(final SpeedHandler speed) {

        this.movement = speed;
        this.drawMgr.setPlaceH(PLACE_HOLDER);
        this.drawMgr.addSprite(KEY_SPRITE1, SPRITE_PATH + "background_1.png");
        this.drawMgr.addSprite(KEY_SPRITE2, SPRITE_PATH + "background_2.png");

        this.boxVisible = new HashMap<>(Map.of(
                BoxPos.LEFT, false,
                BoxPos.CENTRAL, true,
                BoxPos.RIGHT, false));
        this.boxSprite = new HashMap<>(3);

        Set.of(BoxPos.values()).stream()
                .forEach(key -> this.boxSprite.put(key, Optional.empty()));
        this.boxSprite.put(BoxPos.CENTRAL, Optional.of(KEY_SPRITE1));
        this.boxSprite.put(BoxPos.RIGHT, Optional.of(KEY_SPRITE1));

        this.setVisibility(true);
    }

    /**
     * {@inheritDoc}
     */
    public final void setVisibility(final boolean v) {
        visible = v;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isVisible() {
        return visible;
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        this.position.set(START_POS.getX(), START_POS.getY());
        this.movement.resetSpeed();
    }

    /**
     * {@inheritDoc}
     */
    public void update() {
        this.updateFlags();
        if (this.isVisible()) {
            if (this.toBeGenerated) {
                this.boxSprite.put(BoxPos.RIGHT,
                        rand.nextDouble() > BackgroundController.LADDER_GENERATION
                        ? Optional.of(BackgroundController.KEY_SPRITE1)
                        : Optional.of(BackgroundController.KEY_SPRITE2));
            }
            if (this.toBeShifted) {
                this.shiftBox();
                this.toBeShifted = false;
            }
            if (this.position.getX() > -SCREEN_WIDTH * 2) {
                this.position.setX(this.position.getX() - this.movement.getXSpeed() / GameWindow.FPS_LIMIT);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(final Graphics2D g) {
        if (this.isVisible()) {
            this.boxSprite.entrySet().stream()
                    .filter(box -> this.boxVisible.get(box.getKey()))
                    .forEach(box -> this.drawMgr.drawSprite(g,
                            box.getValue().orElse(BackgroundDrawer.PLACEHOLDER_KEY),
                            this.calculate(box.getKey()),
                            GameWindow.GAME_SCREEN.getHeight(),
                            GameWindow.GAME_SCREEN.getWidth()));
        }
    }

    /**
     * This method shifts the assignments to the boxes one position to the left.
     * 
     * <p>It has to be called when the sprite assigned to the left box is going
     * out of the screen and therefore it will be no longer visible.</p>
     */
    private void shiftBox() {

        this.boxVisible.putAll(new HashMap<>(Map.of(
                BoxPos.LEFT, true,
                BoxPos.CENTRAL, true,
                BoxPos.RIGHT, false)));

        this.boxSprite.put(BoxPos.LEFT, this.boxSprite.get(BoxPos.CENTRAL));
        this.boxSprite.put(BoxPos.CENTRAL, this.boxSprite.get(BoxPos.RIGHT));

        final Pair<Double, Double> temp = this.calculate(BoxPos.RIGHT);
        this.position.set(temp.getX(), temp.getY());
    }

    private Pair<Double, Double> calculate(final BoxPos box) {
        final Pair<Double, Double> newPos;
        switch (box) {
            case LEFT:
                newPos = new Pair<>(this.position.getX() - SCREEN_WIDTH, this.position.getY());
                break;
            case RIGHT:
                newPos = new Pair<>(this.position.getX() + SCREEN_WIDTH, this.position.getY());
                break;
            default:
                //newPos = new Pair<>(this.position);
                newPos = this.position.copy();
                break;
        }
        return newPos;
    }

    /**
     * {@inheritDoc}
     */
    public void drawCoordinates(final Graphics2D g) {
        final int xShift = (int) Math.round(position.getX())
                + (int) Math.round(GameWindow.GAME_SCREEN.getTileSize() * 0.88);
        final int yShiftDrawnX = (int) Math.round(position.getY())
                + GameWindow.GAME_SCREEN.getTileSize();
        final int yShiftDrawnY = yShiftDrawnX + 10;

        if (GameWindow.GAME_DEBUGGER.isFeatureEnabled(Debugger.Option.BACKGROUND_COORDINATES) && this.isVisible()) {
            g.setColor(Debugger.DEBUG_COLOR);
            g.setFont(Debugger.DEBUG_FONT);

            g.drawString("X:" + Math.round(position.getX()), xShift, yShiftDrawnX);
            g.drawString("Y:" + Math.round(position.getY()), xShift, yShiftDrawnY);
        }
    }

    /**
     * Updates the entity's flags.
     */
    private void updateFlags() {
        if (position.getX() <= 0) {
            toBeGenerated = true;
            toBeShifted = true;
        } else {
            toBeGenerated = false;
        }
    }

    /**
     * @return a string representing the type of entity with his coordinates in the environment
     */
    @Override
    public String toString() {
        return "Background"
                + "[L: X:" + Math.round(this.calculate(BoxPos.LEFT).getX())
                +  " - Y:" + Math.round(this.calculate(BoxPos.LEFT).getY()) + "]\n" + "           "
                + "[C: X:" + Math.round(position.getX())
                +  " - Y:" + Math.round(position.getY()) + "]\n" + "           "
                + "[R: X:" + Math.round(this.calculate(BoxPos.RIGHT).getX())
                +  " - Y:" + Math.round(this.calculate(BoxPos.RIGHT).getY()) + "]";
    }

    /**
     * Enumeration used to attach at every background sprite its relative
     * position on the screen:
     * 
     * <p>For example {@link BoxPos#LEFT} is on the left of the player window.</p>
     */
    private enum BoxPos {
        LEFT, CENTRAL, RIGHT;
    }
}
