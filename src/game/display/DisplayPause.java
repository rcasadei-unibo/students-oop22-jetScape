package game.display;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;

import game.utility.screen.Screen;

public class DisplayPause implements Display {
	private final Screen gScreen;

	public DisplayPause(Screen gScreen) {
		super();
		this.gScreen = gScreen;
	}

	@Override
	public void drawScreen(Graphics2D g) {
		String msg1 = "PAUSED";
		String msg2 = "Press \"r\" to resume , \"e\" return to menu";
		g.setColor(Color.white);
		g.setFont(g.getFont().deriveFont(Font.LAYOUT_RIGHT_TO_LEFT, 80F));
		g.drawString(msg1,this.getCenteredX(gScreen, g, msg1),gScreen.getTileSize()*2);
		g.setFont(g.getFont().deriveFont(Font.ITALIC, 35F));
		g.drawString(msg2,this.getCenteredX(gScreen, g, msg2),gScreen.getTileSize()*7);

	}

}
