package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import game.utility.other.MenuOption;
import game.utility.screen.Screen;

public class DisplayRecords extends Display {
    private static final int optionTile = 8;
    private static final String title = "Records";
    private static final String records1 = "Length";
    private static final String records2 = "Money";

    //TODO SPOSTARE
    //private static final List<String> lengthRecords = new ArrayList<>();
    //private static final List<String> moneyRecords = new ArrayList<>();
    private static final Set<String> lengthRecords = new HashSet<>();
    private static final Set<String> moneyRecords = new HashSet<>();

    public DisplayRecords(final Screen gScreen) {
        super(gScreen);

        this.options.add(MenuOption.MENU);

        DisplayRecords.lengthRecords.add("1235");
        DisplayRecords.lengthRecords.add("150");
        DisplayRecords.lengthRecords.add("1500");

        DisplayRecords.moneyRecords.add("3500");
    }

    //TODO SPOSTARE IN CLASSE APPOSITA
    private final List<String> listify(final Set<String> set) {
        final List<String> returnList = new ArrayList<>();
        returnList.addAll(List.copyOf(set));
        Collections.sort(returnList);
        return returnList;
    }

    //game.utility.sprites.Drawer per caricare una sprite
    public void drawScreen(final Graphics2D g, final MenuOption selected) {
        int i;

        this.selectedOption = selected;

        // TITLE
        super.drawTitleText(g, title, Function.identity());

        // RECORDS
        super.drawCenteredText(g, super.getTextFont(), DisplayRecords.records1,
                x -> x - gScreen.getWidth() / 4, gScreen.getTileSize() * 3, 0);

        final List<String> recordList = this.listify(DisplayRecords.lengthRecords);
        for (i = 0; i < recordList.size(); i++) {
            super.drawText(g, super.getTextFont(), recordList.get(i),
                    gScreen.getTileSize() * 3, gScreen.getTileSize() * (3 + i + 1), 0);
        }

        super.drawCenteredText(g, super.getTextFont(), DisplayRecords.records2,
                x -> x + gScreen.getWidth() / 4, gScreen.getTileSize() * 3, 0);

        final List<String> moneyList = this.listify(DisplayRecords.moneyRecords);
        for (i = 0; i < moneyList.size(); i++) {
            super.drawText(g, super.getTextFont(), moneyList.get(i),
                    gScreen.getTileSize() * 3 + gScreen.getWidth() / 2,
                    gScreen.getTileSize() * (3 + i + 1), 0);
        }

        //OPTIONS
        super.drawOptions(g, DisplayRecords.optionTile);
    }

    @Override
    protected Color getShiftColor() {
        return Color.DARK_GRAY;
    }
}
