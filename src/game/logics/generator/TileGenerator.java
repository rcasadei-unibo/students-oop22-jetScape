package game.logics.generator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.awt.Graphics2D;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.frame.GameWindow;
import game.logics.entities.generic.Entity;
import game.logics.entities.obstacles.missile.Missile;
import game.logics.entities.obstacles.zapper.Zapper;
import game.logics.entities.obstacles.zapper.ZapperBase;
import game.logics.entities.obstacles.zapper.ZapperInstance;
import game.logics.entities.obstacles.zapper.ZapperRay;
import game.logics.entities.pickups.shield.Shield;
import game.logics.handler.Logics;
import game.utility.debug.Debugger;
import game.utility.other.EntityType;
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
	 * A function used by the generator for creating <code>Shield</code> object.
	 */
	private Optional<Function<Pair<Double,Double>,Shield>> createShield = Optional.empty();
	
	/**
	 * A map containing lists where all loaded set of tiles are stored.
	 */
	private final Map<EntityType, List<Set<Entity>>> tileSets = new HashMap<>();

	/**
	 * The entities map where the spawner adds the sets of obstacles.
	 */
	private final Map<EntityType, Set<Entity>> entities;
	
	
	/**
	 * Decides the odds for the generator to spawn a set of zappers
	 */
	private final int zapperOdds = 100;
	/**
	 * Decides the odds for the generator to spawn a set of missiles
	 */
	private final int missileOdds = 40;
	/**
	 * Decides the odds for the generator to spawn a power up
	 */
	private final int powerUpOdds = 5;
	
	/**
	 * Decides how many seconds the generator pauses after each set spawned.
	 */
	private final int spawnInterval;
	private final long interval;
	private final long intervalDecreaseDiff = 100;
	private final long minimalInterval = 15;

	private final Thread generator = new Thread(this);
	private boolean running = false;
	private boolean waiting = false;
	
	private final int tileSize;
	
	private long systemTimeBeforeSleep = 0;
	private long systemTimeAfterPaused = 0;
	private long remainingTimeToSleep = 0;
	private long sleepTimeLeft = 0;
	private long sleepInterval;

	
	/**
	 * Constructor that sets up the entities structure where obstacles will be
	 * added and allows to specify the interval for spawning.
	 * 
	 * @param entities the entities map where obstacles will be added
	 * @param interval the interval between each generation
	 */
	public TileGenerator(final int tileSize, final Map<EntityType, Set<Entity>> entities, final int interval) {
		this.entities = entities;
		this.spawnInterval = interval;
		this.tileSize = tileSize;
		this.interval = spawnInterval * GameWindow.milliSecond + intervalDecreaseDiff;
		this.sleepInterval = interval;
		
		EntityType.concreteGenericTypes.stream().filter(e -> e.isGenerableEntity()).collect(Collectors.toList())
		.forEach(e -> tileSets.put(e, new ArrayList<>()));
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
			JSONArray types = (JSONArray)allTiles.get(EntityType.ZAPPER.toString());
			for(int i = 0; i < types.size(); i++){
				JSONArray zsets = (JSONArray)types.get(i);
				Set<Entity> tile = new HashSet<>();
				for(int j = 0; j < zsets.size(); j++) {
					JSONArray set = (JSONArray)zsets.get(j);
					if(set.size() >= 2) {
						Set<ZapperRay> s = new HashSet<>();
					
						JSONObject b1 = (JSONObject)set.get(0), b2 = (JSONObject)set.get(1);
						ZapperBase base1 = createZBase.get().apply(new Pair<>(
								Double.parseDouble((String)b1.get("x")) * tileSize,
								Double.parseDouble((String)b1.get("y")) * tileSize));
						ZapperBase base2 = createZBase.get().apply(new Pair<>(
								Double.parseDouble((String)b2.get("x")) * tileSize,
								Double.parseDouble((String)b2.get("y")) * tileSize));
			
						for(int h = 2; h < set.size(); h++) {
							JSONObject z = (JSONObject)set.get(h);
							s.add(createZRay.get().apply(new Pair<>(base1, base2), new Pair<>(
									Double.parseDouble((String)z.get("x")) * tileSize,
									Double.parseDouble((String)z.get("y")) * tileSize)));			
						}
						Zapper master = new ZapperInstance(base1,base2,s);
						base1.setMaster(master);
						base2.setMaster(master);
						tile.add(master);	
					}
				}
				tileSets.get(EntityType.ZAPPER).add(tile);
			}
		}
			
		///		LOADING MISSILES	///
		if(createMissile.isPresent()) {
			JSONArray types = (JSONArray)allTiles.get(EntityType.MISSILE.toString());
			for(int i = 0; i < types.size(); i++){
				JSONArray sets = (JSONArray)types.get(i);
				Set<Entity> s = new HashSet<>();
				
				for(int j = 0; j < sets.size(); j++) {
					JSONObject z = (JSONObject)sets.get(j);
					s.add(createMissile.get().apply(new Pair<>(
						Double.parseDouble((String)z.get("x")) * tileSize,							
						Double.parseDouble((String)z.get("y")) * tileSize)));			
				}
				tileSets.get(EntityType.MISSILE).add(s);
			}
		}
		
		///		LOADING SHIELDS 	///
		if(createShield.isPresent()) {
			JSONArray types = (JSONArray)allTiles.get(EntityType.SHIELD.toString());
			for(int i = 0; i < types.size(); i++){
				JSONArray sets = (JSONArray)types.get(i);
				Set<Entity> s = new HashSet<>();
						
				for(int j = 0; j < sets.size(); j++) {
					JSONObject z = (JSONObject)sets.get(j);
					s.add(createShield.get().apply(new Pair<>(
						Double.parseDouble((String)z.get("x")) * tileSize,							
						Double.parseDouble((String)z.get("y")) * tileSize)));			
				}
				tileSets.get(EntityType.SHIELD).add(s);
			}
		}
}
	
	public void cleanTiles() {
		entities.entrySet().stream().filter(e -> e.getKey().isGenerableEntity()).map(e -> e.getValue()).collect(Collectors.toList()).forEach(
		s -> s.removeIf(e -> {
			if(e.isOnClearArea()) {
				e.reset();
			}
			return e.isOnClearArea();
		}));
	}
	
	private void spawnTile() {
		final Random r = new Random();
		int randomNumber;
		randomNumber = r.nextInt() % (missileOdds + zapperOdds);
		randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;
		
		if(randomNumber <= powerUpOdds) {
			spawnSet(EntityType.SHIELD);
		} else if(randomNumber <= missileOdds) {
			spawnSet(EntityType.MISSILE);
		} else {
			spawnSet(EntityType.ZAPPER);
		}
	}
	
	private void spawnSet(final EntityType type) {
		final Random r = new Random();
		boolean continueSearch;
		int randomNumber;
		do {
			continueSearch = false;
			randomNumber = r.nextInt() % tileSets.get(type).size();
			randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;
			
			for (Entity e : tileSets.get(type).get(randomNumber)) {
				if(entities.get(type).contains(e)) {
					continueSearch = true;
					break;
				}
			}
		}while(continueSearch);
		
		if(!this.isWaiting()) {
			entities.get(type).addAll(tileSets.get(type).get(randomNumber));
		}
	}
	
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean isWaiting() {
		return waiting;
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
	
	public void setShieldCreator(final Function<Pair<Double,Double>,Shield> shield) {
		this.createShield = Optional.of(shield);
	}
	
	public void drawNextSpawnTimer(final Graphics2D g) {
		if(GameWindow.debugger.isFeatureEnabled(Debugger.Option.NEXT_SPAWN_TIMER)) {
			synchronized(this) {
				long expectedTimer = sleepInterval - (System.nanoTime() / GameWindow.microSecond - systemTimeBeforeSleep);
				long remainingTime = (remainingTimeToSleep + sleepTimeLeft) - (System.nanoTime() / GameWindow.microSecond - systemTimeAfterPaused);
				long timer = !this.isWaiting() ? remainingTime > 0 ? remainingTime : expectedTimer  : remainingTimeToSleep;
			
			
				g.setColor(Debugger.debugColor);
				g.setFont(Debugger.debugFont);
				g.drawString("NEXT: " + timer + "ms", 3, 17);
			}
		}
	}
	
	private void invokeSleep(final long interval) {
		try {
			Thread.sleep(interval > 0 ? interval : 0);
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
		
		synchronized(this) {
			remainingTimeToSleep = 0;
		}
	}
	
	public void pause() {
		waiting = true;
		
		synchronized(this) {
			long timePassed = System.nanoTime() / GameWindow.microSecond - systemTimeBeforeSleep;
			remainingTimeToSleep = sleepInterval - timePassed;
		}
	}
	
	public void resume() {
		synchronized(generator) {
			if(this.isWaiting()) {
				waiting = false;
				generator.notify();
				
				synchronized(this) {
					long timePassed = System.nanoTime() / GameWindow.microSecond - systemTimeBeforeSleep;
					sleepTimeLeft = sleepInterval - timePassed;
					remainingTimeToSleep = timePassed < sleepInterval ? remainingTimeToSleep - sleepTimeLeft : remainingTimeToSleep;
					systemTimeAfterPaused = System.nanoTime() / GameWindow.microSecond; 
				}
			}
		}
	}

	@Override
	public void run(){
		final long minimum = (interval - intervalDecreaseDiff) * minimalInterval / 100;
		
		while(generator.isAlive() && this.isRunning()) {
			
			synchronized(generator) {
				while(this.isWaiting()) {
					this.invokeWait();
					if(!this.isRunning()) {
						continue;
					}
				}
				this.invokeSleep(remainingTimeToSleep);
				synchronized(this) {
					remainingTimeToSleep = 0;
					sleepTimeLeft = 0;
				}
			}
			
			
			synchronized(entities) {
				spawnTile();
			}	
			
			synchronized(this) {
				systemTimeBeforeSleep = System.nanoTime() / GameWindow.microSecond;
				sleepInterval =  interval - intervalDecreaseDiff * Logics.getDifficultyLevel() > minimum ? interval - intervalDecreaseDiff * Logics.getDifficultyLevel() : minimum;
			}
			this.invokeSleep(sleepInterval);
		}
	}

}
