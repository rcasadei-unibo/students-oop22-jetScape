package game.logics.entities.obstacles;

import java.awt.Graphics2D;
import java.awt.Color;

import game.logics.handler.Logics;
import game.utility.other.Pair;
import game.utility.textures.DrawManager;
import game.utility.textures.Drawer;

public class ZapperRayInstance extends ObstacleInstance implements ZapperRay{

	private static final String texturePath = "zapperray" + System.getProperty("file.separator");
	private static final Color placeH = Color.yellow;
	
	private final ZapperBase electrode1;
	private final ZapperBase electrode2;

//	private BufferedImage textureVert;
//	private BufferedImage textureHorr;
	private final Drawer textureMgr;
	private String rotation = "diagonal-left";
	
	
	public ZapperRayInstance(final Logics l, final Pair<Double,Double> p, final ZapperBase e1, final ZapperBase e2) {
		super(l, p);
		entityTag = "zapper-ray";
		
		electrode1 = e1;
		electrode2 = e2;	
		
		this.movement = e1.getSpeedHandler();
		
		updateRotation();
//		try {
//			textureVert = ImageIO.read(getClass().getClassLoader().getResourceAsStream(System.getProperty("file.separator") + "game" + System.getProperty("file.separator") + "textures" + System.getProperty("file.separator") + "zapperray" + System.getProperty("file.separator") + "zapperray_vert.png"));
//			textureHorr = ImageIO.read(getClass().getClassLoader().getResourceAsStream(System.getProperty("file.separator") + "game" + System.getProperty("file.separator") + "textures" + System.getProperty("file.separator") + "zapperray" + System.getProperty("file.separator") + "zapperray_horr.png"));
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
		textureMgr = new DrawManager(placeH);
		textureMgr.addTexture("vertical", texturePath + "zapperray_vert.png");
		textureMgr.addTexture("horizontal", texturePath + "zapperray_horr.png");
		textureMgr.setAnimator(() -> rotation);
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
//		updateRotation();
		
		if(position.getX() > -screen.getTileSize()) {
			position.setX(position.getX() - movement.getXSpeed() / fps);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		
//		if(this.rotation == "vertical") {
//			g.drawImage(textureVert, round(position.getX()), round(position.getY()), screen.getTileSize(), screen.getTileSize(), null);
//		} else {
//			g.drawImage(textureHorr, round(position.getX()), round(position.getY()), screen.getTileSize(), screen.getTileSize(), null);
//		}
		textureMgr.drawTexture(g, position, screen.getTileSize());
		super.draw(g);
	}
	
}
