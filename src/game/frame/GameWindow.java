package game.frame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import game.logics.handler.Logics;
import game.logics.handler.LogicsHandler;
import game.utility.debug.Debugger;
import game.utility.fonts.FontLoader;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.Sound;
import game.utility.screen.Screen;
import game.utility.screen.ScreenHandler;
import game.utility.sound.SoundManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>GameWindow</code> class manages both the panel representing 
 * the game window and the execution of the game.
 * 
 * <p>
 * The execution loop is handled by a thread implemented in this class.
 * As long as this thread is alive the game will continue to run.
 * </p>
 */
public class GameWindow extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    /**
     * Represents how many nanoseconds are in a second.
     */
    public static final long NANO_SECOND = 1_000_000_000;
    /**
     * Represents how many microseconds are in a second.
     */
    public static final long MICRO_SECOND = 1_000_000;
    /**
     * Represents how many milliseconds are in a second.
     */
    public static final long MILLI_SECOND = 1000;

    /**
     * Defines the cap for the "Frames Per Second". 
     * 
     * <p>
     * The game loop (coded in <code>{@link GameWindow}.run()</code>) is executed as many times as
     * specified in <code>{@link GameWindow}.FPS_LIMIT</code> each second.
     * </p>
     * 
     * <p>
     * E.G.: if <code>FPS_LIMIT</code> value is 60, it means that the game loop will be
     * executed 60 times per second.
     * </p>
     * 
     */
    public static final int FPS_LIMIT = 60;

    /**
     * Game FPS (Frame Per Second).
     */
    private int fps;

    private final List<Long> drawTimePerFrame = new ArrayList<>();
    private long averageDrawTime;

    /**
     * Stores the screen information (resolution, size of each tile, etc).
     */
    public static final Screen GAME_SCREEN = new ScreenHandler();
    /**
     * Listens the press of keys of the keyboard.
     */
    public static final KeyHandler GAME_KEYHANDLER = new KeyHandler();
    /**
     * Loads and stores the font used in the game.
     */
    public static final FontLoader GAME_FONTLOADER = new FontLoader();
    /**
     * Manages enabling and disabling of Debug Features.
     */
    public static final Debugger GAME_DEBUGGER = new Debugger(GameHandler.DEBUG_MODE);
    /**
     * Manages in game music.
     */
    public static final SoundManager GAME_MUSIC = new SoundManager(Sound.MAIN_THEME);
    /**
     * Manages in game sound.
     */
    public static final SoundManager GAME_SOUND = new SoundManager(Sound.MENU_SELECTION);
    /**
     * Handles the logic part of the game (entities, interface, game state, etc). 
     */
    private final transient Logics logH;

    private final transient Thread gameLoop = new Thread(this);

    /**
     * Indicates if the thread is set to be active or not.
     */
    private boolean gameRunning;

    /**
     * Basic constructor that sets {@link JPanel} attributes and sets up the {@link Logics} handler
     * and the {@link Debugger}.
     */
    public GameWindow() {
        super();

        /// Sets up the basic JPanel parameters /// 
        this.setPreferredSize(GAME_SCREEN.getScreenSize());
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        /// Links the keyboard listener to the JPanel ///
        this.addKeyListener(GAME_KEYHANDLER);

        this.logH = new LogicsHandler();
    }

    /**
     * Begins the execution of the game loop.
     */
    public void startGame() {
        gameRunning = true;
        GAME_MUSIC.playInLoop(Sound.MAIN_THEME);
        gameLoop.start();
    }

    /**
     * Ends the execution of the game loop and closes the game.
     */
    public void stopGame() {
        gameRunning = false;
        GAME_MUSIC.stop(Sound.MAIN_THEME);
    }

    /**
     * Updates the class parameters for each frame.
     */
    private void update() {
        // Updates Logics parameters
        logH.updateAll();
    }

    /**
     * Decides what to draw for each frame.
     */
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final int fpsMeterX = 3;
        final int fpsMeterY = 8;
        final int drawTimeX = 3;
        final int drawTimeY = GAME_SCREEN.getHeight() - 5;

        final long timeBeforeDraw = System.nanoTime();
        final Graphics2D board = (Graphics2D) g;

        // Draws logical parts of the game
        logH.drawAll(board);

        board.setColor(Debugger.DEBUG_COLOR);
        board.setFont(Debugger.DEBUG_FONT);

        // Draws FPS meter if enabled by debugger
        if (GAME_DEBUGGER.isFeatureEnabled(Debugger.Option.FPS_METER)) {
            board.drawString("FPS: " + fps, fpsMeterX, fpsMeterY);
        }
        // Draws average draw time if enabled by debugger
        if (GAME_DEBUGGER.isFeatureEnabled(Debugger.Option.DRAW_TIME)) {
            drawTimePerFrame.add(System.nanoTime() - timeBeforeDraw);
            board.drawString("AVG-DRAW: " + averageDrawTime + "ns", drawTimeX, drawTimeY);
        }

        board.dispose();
    }

    /**
     * Runs the loop, keeping the game alive.
     */
    @Override
    public void run() {
        // Defines how many nanoseconds have to pass until the next execution loop
        final double drawInterval = NANO_SECOND / FPS_LIMIT;
        // System time after interval has passed
        double nextDraw = System.nanoTime() + drawInterval;
        // Nanoseconds passed from the current loop
        long drawTime = 0;
        // FPS counted for the current second
        int fpsCount = 0;

        while (gameLoop.isAlive() && gameRunning) {

            // Gets current system time
            final long timer = System.nanoTime();

            // Updates parameters for each second passed
            if (drawTime > NANO_SECOND) {
                fps = fpsCount;
                drawTime = 0;
                fpsCount = 0;

                long drawTimeSum = 0;
                for (final long draw : drawTimePerFrame) {
                    drawTimeSum += draw;
                }
                averageDrawTime = drawTimeSum / (!drawTimePerFrame.isEmpty() ? drawTimePerFrame.size() : 1);
                drawTimePerFrame.clear();
            }

            /// RUNS EACH FRAME ///
            update();
            repaint();

            fpsCount++;
            try {
                // Thread sleeps until it's next loop time
                double sleepTime = nextDraw - System.nanoTime();
                sleepTime = sleepTime < 0 ? 0 : sleepTime / MICRO_SECOND;
                Thread.sleep((long) sleepTime);

                // Sets up the next loop time for the next frame
                nextDraw = System.nanoTime() + drawInterval;
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog((Component) this, "An error occured! \n Details: \n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

            // Adds the time passed since the last second 
            drawTime += System.nanoTime() - timer;
        }
    }
}
