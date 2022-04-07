package game.utility.screen;

/**
 * The <code>Screen</code> interface can be used for accessing <code>ScreenHandler</code> methods
 * 
 * The <code>ScreenHandler</code> stores all the basic screen information.
 * 
 * @author Daniel Pellanda
 */
public interface Screen {
	
	//// 	DEFAULT VALUES    ////
	static final int tileBaseSize = 32;
	
	static final int resolutionHorizontal = 16;
	static final int resolutionVertical = 9;
	
	static final double tileScaling = 2;
	static final double resolutionScaling = 1;
	
	/// CURRENT RESOLUTION: 1024 x 576 = (64 * 16) x (64 * 9) = ((32 * 2) * 16) x ((32 * 2) * 9)
	
	/**
	 * @return the size for each tile of the screen
	 */
	int getTileSize();
	/**
	 * @return the horizontal resolution of the screen
	 */
	int getWidth();
	/**
	 * @return the vertical resolution of the screen
	 */
	int getHeight();
}
