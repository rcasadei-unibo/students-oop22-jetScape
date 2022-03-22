package game.utility.screen;

/**
 * A basic class for storing the screen information
 */
public class ScreenHandler implements Screen{

	private final int tileSize;
	private final int screenWidth;
	private final int screenHeight;
	
	/**
	 * Initialize a ScreenHandler with default screen values. 
	 */
	public ScreenHandler() {
		tileSize = (int)(tileBaseSize * tileScaling);
		screenWidth = (int)(resolutionHorizontal * tileSize * resolutionScaling);
		screenHeight = (int)(resolutionVertical * tileSize * resolutionScaling);
	}
	
//	/**
//	 * Initialize a ScreenHandler with values taken from the settings file specified
//	 * @param filename 
//	 * 			Settings file where screen values are taken 
//	 */
	//public ScreenHandler(final String filename) {}
	
	public int getTileSize() {
		return tileSize;
	}
	
	public int getWidth() {
		return screenWidth;
	}
	
	public int getHeight() {
		return screenHeight;
	}
	
}
