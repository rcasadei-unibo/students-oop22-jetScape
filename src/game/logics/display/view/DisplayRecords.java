package game.logics.display.view;

import java.awt.Color;
import java.awt.Font;
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
	static final Set<String> monetineRecords = new HashSet<>();

	static final Font titleFont = new Font("magneto", Font.PLAIN, 112);
	static final Font textFont = new Font("calibri", Font.PLAIN, 36);
	static final Font selectedTextFont = new Font("calibri", Font.BOLD, 64);
	static final GameState currentGS = GameState.RECORDS;

	public DisplayRecords(Screen gScreen) {
		super(gScreen);
		super.textTile = 8;
		
		firstOption = "Back to Menu";
		this.options.put(firstOption, GameState.MENU);
		this.buildText(firstOption);
		
		DisplayRecords.lengthRecords.add("1");
		DisplayRecords.lengthRecords.add("50");

		DisplayRecords.lengthRecords.add("1500");

	}
	
	//TODO SPOSTARE IN CLASSE APPOSITA
	private final List<String> listify(final Set<String> set) {
		List<String> returnList = new ArrayList<>();
		returnList.addAll(List.copyOf(set));
		Collections.sort(returnList);
		return returnList;
	}
	
	//game.utility.sprites.Drawer per caricare una sprite
	public void drawScreen(Graphics2D g, String selected) {
		this.selectedOption = selected;
		//TITLE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(titleFont);
		final int titleXPosition = super.getCenteredX(gScreen, g, title);
		g.drawString(title, titleXPosition + titleShift, gScreen.getTileSize() * titleTile);
		
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, titleXPosition, gScreen.getTileSize() * titleTile);
		
		//RECORDS
		g.setColor(Color.white);
		g.setFont(textFont);
		final int centerX = super.getCenteredX(gScreen, g, "");

		int i;
		final int records1Position = super.getCenteredX(gScreen, g, records1);
		
		g.drawString("Length", records1Position - centerX/2, gScreen.getTileSize() * 4);
		final List<String> recordList = this.listify(DisplayRecords.lengthRecords);
		for( i = 0 ; i < recordList.size() ; i++) {
			g.drawString(recordList.get(i), gScreen.getTileSize()*4, gScreen.getTileSize() * (4 + i+1));
		}
		
		final int records2Position = super.getCenteredX(gScreen, g, records2);
		g.drawString("Monetine", records2Position + centerX/2, gScreen.getTileSize() * 4);
		final List<String> monetineList = this.listify(DisplayRecords.monetineRecords);
		for( i = 0 ; i < monetineList.size() ; i++) {
			g.drawString(monetineList.get(i), gScreen.getTileSize()*4, gScreen.getTileSize() * (4 + i+1));
		}
		
		//OPTIONS SHADOW
		g.setColor(Color.darkGray);
		super.drawText(g, textShift);
		
		//OPTIONS
		g.setColor(Color.white);
		super.drawText(g,0);
	}
	
}
