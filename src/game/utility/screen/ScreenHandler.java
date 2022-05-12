package game.utility.screen;

import java.awt.Dimension;

/**
 * The <code>ScreenHandler</code> stores all the basic screen information.
 * 
 * @author Daniel Pellanda
 */
public class ScreenHandler implements Screen{
    
    private final Dimension currentSize;
    private int tileSize;
    
    /**
     * Initialize a ScreenHandler with default screen values. 
     */
    public ScreenHandler() {
        tileSize = (int)((systemResolution.getWidth() / proportion) / horizontalRatio);
        currentSize= new Dimension(tileSize * horizontalRatio, tileSize * verticalRatio);
    }
    
    public Dimension getScreenSize() {
        return currentSize;
    }
    
    public int getTileSize() {
        return (int)tileSize;
    }
    
    public int getWidth() {
        return (int)currentSize.getWidth();
    }
    
    public int getHeight() {
        return (int)currentSize.getHeight();
    }
    
}
