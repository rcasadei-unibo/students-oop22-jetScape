package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.function.UnaryOperator;

import game.frame.GameWindow;
import game.utility.other.MenuOption;
import game.utility.screen.Screen;

/**
 * <p>This class contains what is shown when you want to change settings.</p>
 * 
 * <p>This class extends {@link Display}.</p>
 */
public class DisplaySettings extends Display {

    private static final String TITLE = "Settings";
    private static final int TILE_GAP = 2;
    private static final int OPTIONS_XTILE = 1;
    private static final int OPTIONS_YTILE = 4;
    private static final int STROKE_VAL = 5;
    private static final double RECTANGLE_XTILE = 8;
    private static final double RECTANGLE_YTILE = 3.5;
    private static final int RECTANGLE_WIDTH = 4;

    private final Screen gScreen = super.getGameScreen();

    /**
     * {@link DisplayPause} constructor: add options to be shown.
     *
     */
    public DisplaySettings() {
        super();
        this.getOptions().add(MenuOption.MUSIC);
        this.getOptions().add(MenuOption.SOUND);
        this.getOptions().add(MenuOption.MENU);
     }

    /**
     * {@inheritDoc}
     */
    public void drawScreen(final Graphics2D g, final MenuOption selected) {
        final int x = (int) (gScreen.getTileSize() * RECTANGLE_XTILE);
        int y = (int) (gScreen.getTileSize() * RECTANGLE_YTILE);
        final int width = gScreen.getTileSize() * RECTANGLE_WIDTH;
        final int height = gScreen.getTileSize() / 2;

        this.setSelectedOption(selected);

        // TITLE
        super.drawTitleText(g, TITLE, UnaryOperator.identity());

        // OPTIONS
        this.drawOptions(g);

        //MUSIC SLIDER
        g.setStroke(new BasicStroke(STROKE_VAL));
        g.drawRect(x, y, width, height);
        g.fillRect(x, y, gScreen.getTileSize() * GameWindow.GAME_MUSIC.getVolumeLevel(), height);

        //SOUND SLIDER
        y += gScreen.getTileSize() * TILE_GAP;
        g.drawRect(x, y, width, height);
        g.fillRect(x, y, gScreen.getTileSize() * GameWindow.GAME_SOUND.getVolumeLevel(), height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void drawOptions(final Graphics2D g) {
        int i = 0;
        for (final MenuOption option : super.getOptions()) {
            String current  = option.toString();
            if (option.equals(super.getSelectedOption())) {
                current = "> " + option;
            }
            this.drawText(g, super.getOptionsFont(), current,
                    gScreen.getTileSize() * OPTIONS_XTILE,
                    gScreen.getTileSize() * (i + OPTIONS_YTILE), super.getOptionShift());
            i += TILE_GAP;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Color getShiftColor() {
        return Color.GRAY;
    }
}
