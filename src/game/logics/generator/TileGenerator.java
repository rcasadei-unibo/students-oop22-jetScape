package game.logics.generator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.frame.GameWindow;
import game.logics.entities.generic.Entity;
import game.logics.entities.obstacles.generic.Obstacle;
import game.logics.entities.obstacles.missile.Missile;
import game.logics.entities.obstacles.zapper.ZapperBase;
import game.logics.entities.obstacles.zapper.ZapperRay;
import game.utility.other.Pair;

/**
 * The class <code>TileGenerator</code> handles the generation of tiles of
 * obstacles during the game.
 * 
 * <code>TileGenerator</code> works on a separated thread which can be manually
 * controlled by the <code>LogicsHandler</code>.
 * 
 * @author Daniel Pellanda
 */
public class TileGenerator implements Generator{
	
	/**
	 * The type of file separator the system uses.
	 */
	private static final String separator = System.getProperty("file.separator");
	/**
	 * Default directory where all tiles information file is located.
	 */
	public static final String defaultDir = System.getProperty("user.dir") + separator + "res" + separator + "game"+ separator + "generator" + separator + "tiles.json";
	
	/**
	 * A function used by the generator for creating <code>ZapperRay</code> object.
	 */
	private Optional<BiFunction<Pair<ZapperBase,ZapperBase>,Pair<Double,Double>,ZapperRay>> createZRay = Optional.empty();
	/**
	 * A function used by the generator for creating <code>ZapperBase</code> object.
	 */
	private Optional<Function<Pair<Double,Double>,ZapperBase>> createZBase = Optional.empty();
	/**
	 * A function used by the generator for creating <code>Missile</code> object.
	 */
	private Optional<Function<Pair<Double,Double>,Missile>> createMissile = Optional.empty();
	
	/**
	 * A list where all loaded set of zapper tiles are stored.
	 */
	private final List<Set<Entity>> zapperTiles = new ArrayList<>();
	/**
	 * A list where all loaded set of missile tiles are stored.
	 */
	private final List<Set<Entity>> missileTiles = new ArrayList<>();
	/**
	 * The entities map where the spawner adds the sets of obstacles.
	 */
	private final Map<String, Set<Entity>> entities;
	
	
	/**
	 * Decides the odds for the generator to spawn a set of zappers
	 */
	private final int zapperOdds = 8;
	/**
	 * Decides the odds for the generator to spawn a set of missiles
	 */
	private final int missileOdds = 4;
	/**
	 * Decides how many seconds the generator pauses after each set spawned.
	 */
	private final int spawnInterval;
	private final long interval;

	private final Thread generator = new Thread(this);
	private boolean running = false;
	private boolean waiting = false;
	
	private final int tileSize;
	
	private long systemTimeBeforeSleep;
	private long remainingTimeToSleep = 0;
	
	/**
	 * Constructor that sets up the entities structure where obstacles will be
	 * added and allows to specify the interval for spawning.
	 * 
	 * @param entities the entities map where obstacles will be added
	 * @param interval the interval between each generation
	 */
	public TileGenerator(final int tileSize, final Map<String, Set<Entity>> entities, final int interval) {
		this.entities = entities;
		this.spawnInterval = interval;
		this.tileSize = tileSize;
		
		this.interval = spawnInterval * GameWindow.milliSecond;
	}
	
	/**
	 * Loads each set of obstacles from a json file and store them in Lists.
	 * 
	 * @throws ParseException if parser fails to parse into objects the contents of json file
	 * @throws IOException if json file reading fails
	 * @throws FileNotFoundException if json file cannot be found
	 */
	private void loadTiles() throws FileNotFoundException, IOException, ParseException {
		JSONParser jsonparser = new JSONParser();
		JSONObject allTiles = (JSONObject)jsonparser.parse(new FileReader(defaultDir));
			
		///		LOADING ZAPPERS		///
		if(createZBase.isPresent() && createZRay.isPresent()) {
			JSONArray types = (JSONArray)allTiles.get("zappers");
			for(int i = 0; i < types.size(); i++){
					JSONArray zsets = (JSONArray)types.get(i);
				if(zsets.size() >= 2) {
					Set<Entity> s = new HashSet<>();
					
					JSONObject b1 = (JSONObject)zsets.get(0), b2 = (JSONObject)zsets.get(1);
					ZapperBase base1 = createZBase.get().apply(new Pair<>(
							Double.parseDouble((String)b1.get("x")) * tileSize,
							Double.parseDouble((String)b1.get("y")) * tileSize));
					ZapperBase base2 = createZBase.get().apply(new Pair<>(
							Double.parseDouble((String)b2.get("x")) * tileSize,
							Double.parseDouble((String)b2.get("y")) * tileSize));
					base1.setPaired(base2);
					s.add(base1);
					s.add(base2);
			
					for(int j = 2; j < zsets.size(); j++) {
						JSONObject z = (JSONObject)zsets.get(j);
					s.add(createZRay.get().apply(new Pair<>(base1, base2), new Pair<>(
							Double.parseDouble((String)z.get("x")) * tileSize,
							Double.parseDouble((String)z.get("y")) * tileSize)));			
					}
				zapperTiles.add(s);	
				}
			}
		}
			
		///		LOADING MISSILES	///
		if(createMissile.isPresent()) {
			JSONArray types = (JSONArray)allTiles.get("missiles");
			for(int i = 0; i < types.size(); i++){
				JSONArray sets = (JSONArray)types.get(i);
				Set<Entity> s = new HashSet<>();
				
				for(int j = 0; j < sets.size(); j++) {
					JSONObject z = (JSONObject)sets.get(j);
					s.add(createMissile.get().apply(new Pair<>(
						Double.parseDouble((String)z.get("x")) * tileSize,							
						Double.parseDouble((String)z.get("y")) * tileSize)));			
				}
				missileTiles.add(s);
			}
		}
}
	
	public void cleanTiles() {
		entities.get("zappers").removeIf(e -> {
			Obstacle o = (Obstacle)e;
			if(o.isOnClearArea()) {
				o.reset();
			}
			return o.isOnClearArea();
		});
		entities.get("missiles").removeIf(e -> {
			Obstacle o = (Obstacle)e;
			if(o.isOnClearArea()) {
				o.reset();
			}
			return o.isOnClearArea();
		});
	}
	
	private void spawnTile() {
		final Random r = new Random();
		int randomNumber;
		randomNumber = r.nextInt() % (missileOdds + zapperOdds);
		randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;
		
		if(randomNumber <= missileOdds) {
			spawnMissile();
		} else {
			spawnZapper();
		}
	}
	
	private void spawnMissile() {
		final Random r = new Random();
		int randomNumber;
		do {
			randomNumber = r.nextInt() % missileTiles.size();
			randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;
		}while(entities.get("missiles").containsAll(missileTiles.get(randomNumber)));
		
		if(!this.isWaiting()) {
			entities.get("missiles").addAll(missileTiles.get(randomNumber));
		}
	}
	
	private void spawnZapper() {
		final Random r = new Random();
		int randomNumber;
		do {
			randomNumber = r.nextInt() % zapperTiles.size();
			randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;
		}while(entities.get("zappers").containsAll(zapperTiles.get(randomNumber)));
		
		if(!this.isWaiting()) {
			entities.get("zappers").addAll(zapperTiles.get(randomNumber));
		}
	}
	
	public void setZapperRayCreator(final BiFunction<Pair<ZapperBase,ZapperBase>,Pair<Double,Double>,ZapperRay> zapperr) {
		this.createZRay = Optional.of(zapperr);
	}
	
	public void setZapperBaseCreator(final Function<Pair<Double,Double>,ZapperBase> zapperb) {
		this.createZBase = Optional.of(zapperb);
	}
	
	public void setMissileCreator(final Function<Pair<Double,Double>,Missile> missile) {
		this.createMissile = Optional.of(missile);
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean isWaiting() {
		return waiting;
	}
	
	private void invokeSleep(final long interval) {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void invokeWait() {
		try {
			generator.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void initialize() throws FileNotFoundException, IOException, ParseException {
		this.loadTiles();
		this.start();
	}
	
	public void start() {
		synchronized(generator) {
			if(!this.isRunning()) {
				running = true;
				waiting = true;
				generator.start();
			}
		}
	}
	
	public void terminate() {
		running = false;
		this.resume();
	}
	
	public void stop() {
		waiting = true;
		remainingTimeToSleep = 0;
	}
	
	public void pause() {
		waiting = true;
		remainingTimeToSleep = systemTimeBeforeSleep != 0 ? interval - (System.nanoTime() / GameWindow.microSecond - systemTimeBeforeSleep) : 0;
		System.out.println(remainingTimeToSleep);
	}
	
	public void resume() {
		synchronized(generator) {
			if(this.isWaiting()) {
				waiting = false;
				generator.notify();
			}
		}
	}
	
	@Override
	public void run(){
		
		while(generator.isAlive() && this.isRunning()) {
			systemTimeBeforeSleep = 0;
			
			synchronized(generator) {
				while(this.isWaiting()) {
					this.invokeWait();
					if(!this.isRunning()) {
						continue;
					}
				}
				this.invokeSleep(remainingTimeToSleep);
				remainingTimeToSleep = 0;
			}
			
			synchronized(entities) {
				spawnTile();
			}	
			
			systemTimeBeforeSleep = System.nanoTime() / GameWindow.microSecond;
			this.invokeSleep(interval);
		}
	}

}
