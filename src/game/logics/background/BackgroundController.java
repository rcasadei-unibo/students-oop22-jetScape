package game.logics.background;

import java.awt.Color;
import java.awt.Graphics2D;

import game.frame.GameWindow;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.debug.Debugger;
import game.utility.other.Pair;
import game.utility.sprites.DrawManager;
import game.utility.sprites.Drawer;

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


    private static final Pair<Double, Double> START_POS = new Pair<>(0.0, 0.0);
    private final Pair<Double, Double> position = START_POS;


    private final Drawer spritesMgr = new DrawManager();

    /**
     * @param speed the {@link SpeedHandler} to use for the pickup
     */
    public BackgroundController(final SpeedHandler speed) {

        this.spritesMgr.setPlaceH(PLACE_HOLDER);
        this.spritesMgr.addSprite(KEY_SPRITE1, SPRITE_PATH + "background_1.png");
        this.spritesMgr.addSprite(KEY_SPRITE2, SPRITE_PATH + "background_2.png");

        this.setVisibility(true);
    }

    /**
     * Allows to set the background visibility.
     * 
     * @param v <code>true</code> if has to be shown, <code>false</code> if has to be hidden
     */
    protected final void setVisibility(final boolean v) {
        visible = v;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isVisible() {
        return visible;
    }

    public void update(final Graphics2D g) {
        this.draw(g);
    }

    /**
     * {@inheritDoc}
     */
    public void draw(final Graphics2D g) {
        if (this.isVisible()) {
            spritesMgr.drawSprite(g, KEY_SPRITE1, position, GameWindow.GAME_SCREEN.getHeight());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void drawCoordinates(final Graphics2D g) {
        final int xShift = (int) Math.round(position.getX()) + (int) Math.round(GameWindow.GAME_SCREEN.getTileSize() * 0.88);
        final int yShiftDrawnX = (int) Math.round(position.getY()) + GameWindow.GAME_SCREEN.getTileSize();
        final int yShiftDrawnY = yShiftDrawnX + 10;

        if (GameWindow.GAME_DEBUGGER.isFeatureEnabled(Debugger.Option.ENTITY_COORDINATES) && this.isVisible()) {
            g.setColor(Debugger.DEBUG_COLOR);
            g.setFont(Debugger.DEBUG_FONT);

            g.drawString("X:" + Math.round(position.getX()), xShift, yShiftDrawnX);
            g.drawString("Y:" + Math.round(position.getY()), xShift, yShiftDrawnY);
        }
    }
    
    
    
    
    
    
    
    
    
    
/*
        private boolean onScreen;
        private boolean onClearArea;

        /**
         * {@inheritDoc}
         */
        /*public boolean isOnScreenBounds() {
            return onScreen;
        }*/

        /**
         * {@inheritDoc}
         */
        /*public boolean isOnClearArea() {
            return this.onClearArea;
        }*/

        /**
         * {@inheritDoc}
         */
        /*public Pair<Double, Double> getPosition() {
            return position;
        }
        */

        /**
         * Updates the entity's flags.
         */
        /*private void updateFlags() {
            if (position.getX() >= -GameWindow.GAME_SCREEN.getTileSize()
                    && position.getX() <= GameWindow.GAME_SCREEN.getWidth()
                    && position.getY() >= 0 && position.getY() <= GameWindow.GAME_SCREEN.getHeight()) {
                onScreen = true;
                onClearArea = false;
            } else {
                if (position.getX() < -GameWindow.GAME_SCREEN.getTileSize()) {
                    onClearArea = true;
                } else if (position.getX() >= GameWindow.GAME_SCREEN.getWidth()) {
                    onClearArea = false;
                } else {
                    onClearArea = false;
                }
                onScreen = false;
            }
        }*/

        /**
         * {@inheritDoc}
         */
        /*
        public void update() {
            updateFlags();
        }*/


        /**
         * @return a string representing the type of entity with his coordinates in the environment
         */
        /*
        public String toString() {
            return "Background [X:" + Math.round(position.getX()) + "-Y:" + Math.round(position.getY()) + "]";
        }
*/
}
