package game.display;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;

import game.utility.screen.Screen;

public class DisplayPause implements Display {
	static final int msg1Tile = 2;
	static final int msg2Tile = 7;
	static final int msg1Shift = 5;
	static final int msg2Shift = 3;
	static final Font font = new Font("Stencil",Font.PLAIN,80);
	private final Screen gScreen;

	public DisplayPause(Screen gScreen) {
		super();
		this.gScreen = gScreen;
	}

	@Override
	public void drawScreen(Graphics2D g) {
		int x = 0;
		String msg1 = "PAUSED";
		String msg2 = "Press \"r\" to resume , \"e\" return to menu";
		
		//MESSAGE 1 SHADOW
		g.setColor(Color.black);
		g.setFont(font);
		x = this.getCenteredX(gScreen, g, msg1);
		g.drawString(msg1, x + msg1Shift, gScreen.getTileSize() * msg1Tile);
		//MESSAGE 1
		g.setColor(Color.lightGray);
		g.drawString(msg1, x , gScreen.getTileSize() * msg1Tile);
		//MESSAGE 2 SHADOW
		g.setColor(Color.black);
		g.setFont(g.getFont().deriveFont(Font.ITALIC, 32F));
		x = this.getCenteredX(gScreen, g, msg2);
		g.drawString(msg2, x + msg2Shift, gScreen.getTileSize() * msg2Tile);
		//MESSAGE 2
		g.setColor(Color.lightGray);
		g.drawString(msg2, x, gScreen.getTileSize() * msg2Tile);
	}

	@Override
	public void setCursorIndex(int index) {
		// TODO Auto-generated method stub
		
	}

}
