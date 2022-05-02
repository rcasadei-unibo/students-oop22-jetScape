package game.logics.display.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.utility.screen.Screen;

public class DisplayHUD extends Display {
	static final int scoreTile = 1;
	static final int scoreShift = 3;
	static final Font font = new Font("magneto",Font.PLAIN,32);
	private int score = 0;
	
	public DisplayHUD(Screen gScreen) {
		super(gScreen);
	}

	public void drawScreen(Graphics2D g) {
		int x = 0;
		String score = "SCORE: ";
		//SCORE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(font);
		x = 5*(this.getCenteredX(super.gScreen, g, score)/3);
		g.drawString(score + this.score, x + scoreShift,
				super.gScreen.getTileSize() * scoreTile);
		//SCORE
		g.setColor(Color.white);
		g.drawString(score + this.score, x, super.gScreen.getTileSize() * scoreTile);
	}
	
	public void updateScore(int score) {
		this.score = score;
	}
}
