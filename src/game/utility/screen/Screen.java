package game.utility.screen;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * The <code>Screen</code> interface can be used for accessing <code>ScreenHandler</code> methods
 * 
 * The <code>ScreenHandler</code> stores all the basic screen information.
 * 
 * @author Daniel Pellanda
 */
public interface Screen {
    
    static final Dimension systemResolution = Toolkit.getDefaultToolkit().getScreenSize();
    
    ////     DEFAULT VALUES    ////
    static final int baseTileSize = 32;
    
    static final int horizontalRatio = 16;
    static final int verticalRatio = 9;
    
    static final double baseScaling = 2;
    
    static final double proportion = 1.5;
        
    /**
     * @return a <code>Dimension</code> containing the size of the screen
     */
    Dimension getScreenSize();
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
