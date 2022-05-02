package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayRecords extends Display {
	static final int titleTile = 2;
	static final int titleShift = 5;
	static final int textShift = 6;
	static final String title = "Records";
	static final String records1 = "Length";
	static final String records2 = "Monetine";
	
	//TODO SPOSTARE
	static final Set<String> lengthRecords = new HashSet<>();
	static final Set<String> moneyRecords = new HashSet<>();

	static final GameState currentGS = GameState.RECORDS;

	public DisplayRecords(final Screen gScreen) {
		super(gScreen);
		super.setTextTile(8);
		
		firstOption = "Back to Menu";
		this.options.put(firstOption, GameState.MENU);
		this.buildText(firstOption);
		
		DisplayRecords.lengthRecords.add("1235");
		DisplayRecords.lengthRecords.add("150");
		DisplayRecords.lengthRecords.add("1500");
		
		DisplayRecords.moneyRecords.add("3500");
	}
	
	//TODO SPOSTARE IN CLASSE APPOSITA
	private final List<String> listify(final Set<String> set) {
		List<String> returnList = new ArrayList<>();
		returnList.addAll(List.copyOf(set));
		Collections.sort(returnList);
		return returnList;
	}
	
	//game.utility.sprites.Drawer per caricare una sprite
	public void drawScreen(final Graphics2D g, final String selected) {
		int i;

		this.selectedOption = selected;
		//TITLE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(Display.titleFont);
		final int titleXPosition = super.getCenteredX(gScreen, g, title);
		g.drawString(title, titleXPosition + titleShift, gScreen.getTileSize() * titleTile);
		
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, titleXPosition, gScreen.getTileSize() * titleTile);
		
		//RECORDS
		g.setColor(Color.white);
		g.setFont(Display.textFont);
		final int centerX = super.getCenteredX(gScreen, g, "");
		final int recordsLeftPosition = super.getCenteredX(gScreen, g, records1);
		
		g.drawString("Length", recordsLeftPosition - centerX/2, gScreen.getTileSize() * 3);
		final List<String> recordList = this.listify(DisplayRecords.lengthRecords);
		for( i = 0 ; i < recordList.size() ; i++) {
			g.drawString(recordList.get(i), gScreen.getTileSize()*3, gScreen.getTileSize() * (3 + i+1));
		}
		
		final int recordsRightPosition = super.getCenteredX(gScreen, g, records2);
		g.drawString("Monetine", recordsRightPosition + centerX/2, gScreen.getTileSize() * 3);
		final List<String> moneyList = this.listify(DisplayRecords.moneyRecords);
		for( i = 0 ; i < moneyList.size() ; i++) {
			g.drawString(moneyList.get(i), gScreen.getTileSize()*3 + gScreen.getWidth()/2, gScreen.getTileSize() * (3 + i+1));
		}
		
		//OPTIONS SHADOW
		g.setColor(Color.darkGray);
		super.drawText(g, textShift);
		
		//OPTIONS
		g.setColor(Color.white);
		super.drawText(g,0);
	}
	
}
