package game.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.utility.screen.Screen;

public class DisplayHUD implements Display {
	static final int scoreTile = 1;
	static final int scoreShift = 3;
	static final Font font = new Font("Stencil",Font.PLAIN,32);
	private int score = 0;
	private final Screen gScreen;

	public DisplayHUD(Screen gScreen) {
		super();
		this.gScreen = gScreen;
	}

	@Override
	public void drawScreen(Graphics2D g) {
		int x = 0;
		String score = "SCORE: ";
		//SCORE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(font);
		x = 5*(this.getCenteredX(gScreen, g, score)/3);
		g.drawString(score + this.score, x + scoreShift, gScreen.getTileSize() * scoreTile);
		//SCORE
		g.setColor(Color.white);
		g.drawString(score + this.score, x, gScreen.getTileSize() * scoreTile);
	}
	
	public void updateScore(int score) {
		this.score = score;
	}

}
