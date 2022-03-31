package game.utility.textures;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import game.utility.other.Pair;

public class Texture {
	
	private static final String separator = System.getProperty("file.separator");
	public static final String defaultDir = System.getProperty("user.dir") + separator + "res" + separator + "game" + separator + "textures" + separator;
	
	private Optional<BufferedImage> image = Optional.empty();
	private final Color placeHolder;
	private String name = "unknown";
	
	public Texture(final String name, final Color placeHolder) {
		this.name = name;
		this.placeHolder = placeHolder;
	}
	
	public Texture(final String name, final Color placeHolder, final String path) {
		this(name, placeHolder);
		this.load(path);
	}
	
	public String getName() {
		return name;
	}
	
	public void load(final String path) {
		try {
			image = Optional.of(ImageIO.read(new File(defaultDir + path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(final Graphics2D g, final Pair<Double,Double> pos, final int size) {
		if(this.image.isPresent()) {
			g.drawImage(image.get(), (int)Math.round(pos.getX()), (int)Math.round(pos.getY()), size, size, null);
		} else {
			g.setColor(placeHolder);
			g.fillRect((int)Math.round(pos.getX()), (int)Math.round(pos.getY()), size, size);
		}
	}
}
