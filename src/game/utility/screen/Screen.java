package game.utility.screen;

/**
 * Provides all the screen information
 */
public interface Screen {
	
	//// 	DEFAULT VALUES    ////
	static final int tileBaseSize = 16;
	
	static final int resolutionHorizontal = 16;
	static final int resolutionVertical = 9;
	
	static final double tileScaling = 4;
	static final double resolutionScaling = 1;
	
	/**
	 * @return
	 * 		The size for each tile of the screen
	 */
	int getTileSize();
	/**
	 * @return
	 * 		The horizontal resolution of the screen
	 */
	int getWidth();
	/**
	 * @return
	 * 		The vertical resolution of the screen
	 */
	int getHeight();
}
