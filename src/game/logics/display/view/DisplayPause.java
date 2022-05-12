package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.utility.other.MenuOption;
import game.utility.screen.Screen;

/**
 * <p>This class contains what is shown when the game is paused.</p>
 * 
 * <p>This class extends {@link Display}.</p>
 */
public class DisplayPause extends Display implements MenuDisplay {

    private static final String TITLE = "Paused";

    /**
     * {@link DisplayPause} constructor: add options to be shown.
     * @param gScreen
     */
    public DisplayPause(final Screen gScreen) {
        super(gScreen);

        this.getOptions().add(MenuOption.RESUME);
        this.getOptions().add(MenuOption.MENU);
     }

    /**
     * {@inheritDoc}
     */
    public void drawScreen(final Graphics2D g, final MenuOption selected) {
        this.setSelectedOption(selected);

        // TITLE
        super.drawTitleText(g, TITLE, Function.identity());

        // OPTIONS
        super.drawOptions(g);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    protected Color getShiftColor() {
        return Color.BLACK;
    }
}
