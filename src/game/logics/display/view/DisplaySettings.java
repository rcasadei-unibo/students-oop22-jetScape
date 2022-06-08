package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.function.Function;

import game.frame.GameWindow;
import game.utility.other.MenuOption;
import game.utility.screen.Screen;

public class DisplaySettings extends Display {

    private static final String TITLE = "Settings";
    private static final int OPTIONS_XTILE = 1;
    private static final int OPTIONS_YTILE = 4;
    private static final int RECTANGLE_XTILE = 8;
    private static final int RECTANGLE_WIDTH = 4;
    private  final Screen gScreen = super.getGameScreen();
    
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
        this.setSelectedOption(selected);

        // TITLE
        super.drawTitleText(g, TITLE, Function.identity());

        // OPTIONS
        this.drawOptions(g);
        g.setStroke(new BasicStroke(5));
        g.drawRect(gScreen.getTileSize() * RECTANGLE_XTILE,
                (int) (gScreen.getTileSize() * 3.5), gScreen.getTileSize() * RECTANGLE_WIDTH,
                gScreen.getTileSize()/2);
        g.drawRect(gScreen.getTileSize() * RECTANGLE_XTILE, (int) (gScreen.getTileSize() * 5.5),
                gScreen.getTileSize() * RECTANGLE_WIDTH, gScreen.getTileSize()/2);
        g.fillRect(gScreen.getTileSize() * RECTANGLE_XTILE,
                (int) (gScreen.getTileSize() * 3.5),
                gScreen.getTileSize() * GameWindow.GAME_MUSIC.getVolumeLevel(), gScreen.getTileSize()/2);
        g.fillRect(gScreen.getTileSize() * RECTANGLE_XTILE,
                (int) (gScreen.getTileSize() * 5.5),
                gScreen.getTileSize() * GameWindow.GAME_SOUND.getVolumeLevel(), gScreen.getTileSize()/2);
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
            i += 2;
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
