package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import game.logics.records.Records;
import game.utility.other.MenuOption;


/**
 * <p>This class is used to display statistics and records.</p>
 * 
 * <p>This class extends {@link Display}.</p>
 */
public class DisplayRecords extends Display implements MenuDisplay {

    private static final int OPTION_TILE = 8;
    private static final String TITLE = "Records";
    private static final String RECORDS1 = "Length";
    private static final String RECORDS2 = "Money";

    private final Records records;

    //TODO: SPOSTARE
    //private static final List<String> lengthRecords = new ArrayList<>();
    //private static final List<String> moneyRecords = new ArrayList<>();
    private static final Set<String> LENGTH_RECORDS = new HashSet<>();
    private static final Set<String> MONEY_RECORDS = new HashSet<>();

    /**
     * {@link DisplayRecords} constructor: add options to be shown.
     *
     */
    public DisplayRecords(final Records records) {

        super();
        this.records = records;

        // TODO: remove these
        DisplayRecords.LENGTH_RECORDS.add("1235");
        DisplayRecords.LENGTH_RECORDS.add("150");
        DisplayRecords.LENGTH_RECORDS.add("1500");

        DisplayRecords.MONEY_RECORDS.add("3500");
    }

    // TODO: SPOSTARE IN CLASSE APPOSITA
    private List<String> listify(final Set<String> set) {
        final List<String> returnList = new ArrayList<>();
        returnList.addAll(List.copyOf(set));
        Collections.sort(returnList);
        return returnList;
    }

    //game.utility.sprites.Drawer per caricare una sprite
    /**
     * {@inheritDoc}
     */
    public void drawScreen(final Graphics2D g, final MenuOption selected) {

        int i;
        this.setSelectedOption(selected);

        // TITLE
        super.drawTitleText(g, TITLE, Function.identity());

        // RECORDS
        super.drawCenteredText(g, super.getTextFont(), DisplayRecords.RECORDS1,
                x -> x - super.getGameScreen().getWidth() / 4, super.getGameScreen().getTileSize() * 3, 0);

        final List<String> recordList = this.listify(DisplayRecords.LENGTH_RECORDS);
        for (i = 0; i < recordList.size(); i++) {
            super.drawText(g, super.getTextFont(), recordList.get(i),
                    super.getGameScreen().getTileSize() * 3, super.getGameScreen().getTileSize() * (3 + i + 1), 0);
        }

        super.drawCenteredText(g, super.getTextFont(), DisplayRecords.RECORDS2,
                x -> x + super.getGameScreen().getWidth() / 4, super.getGameScreen().getTileSize() * 3, 0);

        final List<String> moneyList = this.listify(DisplayRecords.MONEY_RECORDS);
        for (i = 0; i < moneyList.size(); i++) {
            super.drawText(g, super.getTextFont(), moneyList.get(i),
                    super.getGameScreen().getTileSize() * 3 + super.getGameScreen().getWidth() / 2,
                    super.getGameScreen().getTileSize() * (3 + i + 1), 0);
        }
        
        //OPTIONS
        super.drawOptions(g, DisplayRecords.OPTION_TILE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Color getShiftColor() {
        return Color.DARK_GRAY;
    }
}
