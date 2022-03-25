package game.frame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import game.logics.handler.Logics;
import game.logics.handler.LogicsHandler;
import game.utility.input.keyboard.KeyHandler;
import game.utility.screen.Screen;
import game.utility.screen.ScreenHandler;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

public class GameWindow extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;
	
	public static final int fpsLimiter = 60;
	
	private final Thread gameLoop = new Thread(this);
	private final Screen gameScreen = new ScreenHandler();
	private final KeyHandler keyH = new KeyHandler();
	private final Logics logH;
	
	private Font fpsFont = new Font("Calibri", Font.PLAIN, 18);
	private int fps = 0;
	public boolean debug;
	
	public GameWindow(final boolean debug) {
		super();
		this.setPreferredSize(new Dimension(gameScreen.getWidth(), gameScreen.getHeight()));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.addKeyListener(keyH);
		this.logH = new LogicsHandler(gameScreen, keyH, debug);
		
		this.debug = debug;
	}
	
	public void startLoop() {
		gameLoop.start();
	}
	
	private void update() {
		logH.updateAll();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D board = (Graphics2D)g;
		board.setColor(Color.white);
		
		if(debug) {
			board.setFont(fpsFont);
			board.drawString("FPS: " + fps, 10, 15);
		}
		
		logH.drawAll(board);
		
		board.dispose();
	}
	
	@Override
	public void run() {
		double drawInterval = 1000000000 / fpsLimiter;
		double nextDraw = System.nanoTime() + drawInterval;
		long drawTime = 0;
		int fpsCount = 0;
		
		while(gameLoop.isAlive()) {
			
			//System.out.print(debug ? "Running...\n" : "");
			long timer = System.nanoTime();
			if(drawTime > 1000000000) {
				fps = fpsCount;
				drawTime = 0;
				fpsCount = 0;
				//System.out.print(debug ? "FPS: " + fps + "\n" : "");
			}
			
			update();
			repaint();
			
			fpsCount++;
			try {
				double sleepTime = nextDraw - System.nanoTime();
				sleepTime = sleepTime < 0 ? 0 : sleepTime / 1000000;
				Thread.sleep((long) sleepTime);
				
				nextDraw = System.nanoTime() + drawInterval;
			} catch (InterruptedException e) {
				JOptionPane.showMessageDialog((Component)this, "An error occurred!\nProcessing frame has been interrupted", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			
			drawTime += System.nanoTime() - timer;
		}
	}
}
