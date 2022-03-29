package game.logics.interactions;

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

import game.logics.entities.basic.Entity;
import game.logics.entities.obstacles.ZapperBase;
import game.logics.entities.obstacles.ZapperRay;
import game.utility.other.Pair;

public class TileGenerator implements Generator{

	private Optional<BiFunction<Pair<ZapperBase,ZapperBase>,Pair<Double,Double>,ZapperRay>> createZRay = Optional.empty();
	private Optional<Function<Pair<Double,Double>,ZapperBase>> createZBase = Optional.empty();
	
	private final Map<String, Set<Entity>> entities;
	private final List<Set<Entity>> zapperTiles = new ArrayList<>();
	
	private final Thread generator = new Thread(this);
	private final int spawnInterval;
	private boolean working = false;
	
	public TileGenerator(final Map<String, Set<Entity>> entities, final int interval) {
		this.entities = entities;
		this.spawnInterval = interval;
	}
	
	private void loadTiles() {
		JSONParser jsonparser = new JSONParser();
		try {
			JSONObject allTiles = (JSONObject)jsonparser.parse(new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator") + "res" + System.getProperty("file.separator") + "game"+ System.getProperty("file.separator") + "utility" + System.getProperty("file.separator") + "generator" + System.getProperty("file.separator") + "tiles.json"));
			if(createZBase.isPresent() && createZRay.isPresent()) {
				JSONArray types = (JSONArray)allTiles.get("zappers");
				for(int i = 0; i < types.size(); i++){
					JSONArray zsets = (JSONArray)types.get(i);
					if(zsets.size() >= 2) {
						Set<Entity> s = new HashSet<>();
						
						JSONObject b1 = (JSONObject)zsets.get(0), b2 = (JSONObject)zsets.get(1);
						ZapperBase base1 = createZBase.get().apply(new Pair<>(
								Double.parseDouble((String)b1.get("x")),
								Double.parseDouble((String)b1.get("y"))));
						ZapperBase base2 = createZBase.get().apply(new Pair<>(
								Double.parseDouble((String)b2.get("x")),
								Double.parseDouble((String)b2.get("y"))));
						base1.setPaired(base2);
						s.add(base1);
						s.add(base2);
				
						for(int j = 2; j < zsets.size(); j++) {
							JSONObject z = (JSONObject)zsets.get(j);
							s.add(createZRay.get().apply(new Pair<>(base1, base2), new Pair<>(
								Double.parseDouble((String)z.get("x")),
								Double.parseDouble((String)z.get("y")))));			
						}
						zapperTiles.add(s);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ParseException pe) {
			pe.printStackTrace();
		}
	}
	
	private void spawnTile() {
		final Random r = new Random();
		int randomNumber;
		do {
			randomNumber = r.nextInt() % zapperTiles.size();
			randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;
		}while(entities.get("zappers").containsAll(zapperTiles.get(randomNumber)));
		entities.get("zappers").addAll(zapperTiles.get(randomNumber));
	}
	
	public void setZapperBaseCreator(final Function<Pair<Double,Double>,ZapperBase> zapperb) {
		this.createZBase = Optional.of(zapperb);
	}
	
	public void setZapperRayCreator(final BiFunction<Pair<ZapperBase,ZapperBase>,Pair<Double,Double>,ZapperRay> zapperr) {
		this.createZRay = Optional.of(zapperr);
	}
	
	public boolean isWorking() {
		return working;
	}
	
	public void initialize() {
		this.loadTiles();
		generator.start();
	}
	
	public void stop() {
		working = false;
	}
	
	public void start() {
		working = true;
		//generator.notify();
	}
	
	@Override
	public void run(){
		long interval = spawnInterval * 1000;
		
		while(generator.isAlive()) {/*
			if(!this.isWorking()) {
				try {
					generator.wait();
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}*/
			spawnTile();
			
			try {
				Thread.sleep(interval);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
