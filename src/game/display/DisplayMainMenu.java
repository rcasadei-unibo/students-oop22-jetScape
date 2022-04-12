package game.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.utility.other.Pair;
import game.utility.screen.Screen;

public class DisplayMainMenu implements Display {
	static final int titleTile = 2;
	static final int textTile = 6;
	static final int titleShift = 5;
	static final int textShift = 2;
	static final Font font = new Font("magneto", Font.PLAIN, 112);
	static final Font fontText = new Font("calibri", Font.PLAIN, 48);
	static final Font selectedTextFont = new Font("calibri", Font.BOLD, 64);
	private final List<Pair<Integer,String>> text = new ArrayList<>();
	private int cursorIndex = 0;
	
	private final Screen gScreen;

	public DisplayMainMenu(Screen gScreen) {
		super();
		this.gScreen = gScreen;
	}

	@Override
	public void drawScreen(Graphics2D g) {
		int x = 0;
		String title = "JetScape";
		//TITLE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(font);
		x = this.getCenteredX(gScreen, g, title);
		g.drawString(title, x + titleShift, gScreen.getTileSize() * titleTile);
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, x, gScreen.getTileSize() * titleTile);
		//CREATE TEXT LIST
		g.setFont(DisplayMainMenu.fontText);
		this.buildText(g);
		//MESSAGE SHADOW
		g.setColor(Color.darkGray);
		this.drawShadow(g);
		//MESSAGE
		g.setColor(Color.white);
		this.drawText(g);
		//SELECTED TEXT SHADOW
		g.setColor(Color.darkGray);
		g.setFont(DisplayMainMenu.selectedTextFont);
		String selected = "> "+ this.text.get(cursorIndex).getY() +" <";
		x = this.getCenteredX(gScreen, g, selected);
		g.drawString(selected, x + textShift, gScreen.getTileSize() *(textTile + this.cursorIndex));
		// SELECTED TEXT 
		g.setColor(Color.white);
		g.drawString(selected, x, gScreen.getTileSize() *(textTile + this.cursorIndex));
	}
	
	private void buildText(Graphics2D g) {
		if(text.isEmpty()) {
			text.add(new Pair<>(this.getCenteredX(gScreen, g, "Start"),"Start"));
			text.add(new Pair<>(this.getCenteredX(gScreen, g, "Shop"),"Shop"));
			text.add(new Pair<>(this.getCenteredX(gScreen, g, "Quit"),"Quit"));
		}
	}
	
	private void drawText(Graphics2D g) {
		int i = 0;
		for(Pair<Integer,String> e : this.text) {
			if(i != this.cursorIndex) {
				g.drawString(e.getY(), e.getX(), gScreen.getTileSize() * (DisplayMainMenu.textTile + i));
			}
			i++;
		}
	}
	
	private void drawShadow(Graphics2D g) {
		int i = 0;
		for(Pair<Integer,String> e : this.text) {
			if(i != this.cursorIndex) {
				g.drawString(e.getY(), e.getX() + textShift, gScreen.getTileSize() * (textTile + i));
			}
			i++;
		}
	}
	
	public void setCursorIndex(int index) {
		this.cursorIndex = index;
	}
	
}
