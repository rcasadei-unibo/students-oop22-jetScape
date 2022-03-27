package game.logics.entities.obstacles;

//import java.util.Random;

//import javax.imageio.ImageIO;

//import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;

//import java.io.IOException;

import game.logics.handler.Logics;
import game.utility.other.Pair;

public class ZapperRayInstance extends ObstacleInstance implements ZapperRay{
	
	private final ZapperBase electrode1;
	private final ZapperBase electrode2;
	
	//private final Random r = new Random();
	//private BufferedImage texture;
	private final Color texture = Color.yellow;
	private String rotation = "diagonal-left";
	
	
	public ZapperRayInstance(final Logics l, final Pair<Double,Double> p, final ZapperBase e1, final ZapperBase e2) {
		super(l, p);
		entityTag = "zapper-ray";
		
		electrode1 = e1;
		electrode2 = e2;	
		
		this.movement = e1.getSpeedHandler();
		
//		position.setX((double)screen.getWidth());
//		position.setY(yRoof + (yGround - yRoof) * r.nextDouble());
		
//		try {
//			texture = ImageIO.read(getClass().getResourceAsStream("/images/Immagine.png"));
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
	}
	
	private void updateRotation() {
		if(electrode1.getX() == electrode2.getX()) {
			rotation = "vertical";
		} else if(electrode1.getY() == electrode2.getY()) {
			rotation = "horizontal";
		} else if((electrode1.getX() > electrode2.getX() && electrode1.getY() < electrode2.getY()) || (electrode1.getX() < electrode2.getX() && electrode1.getY() > electrode2.getY())) {
			rotation = "diagonal-right";
		} else {
			rotation = "diagonal-left";
		}
	}
	
	@Override
	public void update() {
		super.update();
		updateRotation();
		
		if(position.getX() > -screen.getTileSize()) {
			position.setX(position.getX() - movement.getXSpeed() / fps);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(texture);
		super.draw(g);
		//g.drawImage(texture, round(position.getX()), round(position.getY()), screen.getTileSize(), screen.getTileSize(), null);
	}
	
}
