package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.UnaryOperator;

import game.utility.other.MenuOption;

/**
 * <p>This class contains what is shown when the game is paused.</p>
 * 
 * <p>This class extends {@link Display}.</p>
 */
public class DisplayPause extends Display implements MenuDisplay {

    private static final String TITLE = "Paused";

    /**
     * {@link DisplayPause} constructor: add options to be shown.
     *
     */
    public DisplayPause() {
        super();

        this.getOptions().add(MenuOption.RESUME);
        this.getOptions().add(MenuOption.MENU);
     }

    /**
     * {@inheritDoc}
     */
    public void drawScreen(final Graphics2D g, final MenuOption selected) {
        this.setSelectedOption(selected);

        // TITLE
        super.drawTitleText(g, TITLE, UnaryOperator.identity());

        // OPTIONS
        super.drawOptions(g);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Color getShiftColor() {
        return Color.BLACK;
    }
}
