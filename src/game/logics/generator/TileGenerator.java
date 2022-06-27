package game.logics.generator;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;

import game.frame.GameWindow;
import game.logics.entities.generic.Entity;
import game.logics.entities.obstacles.missile.Missile;
import game.logics.entities.obstacles.zapper.Zapper;
import game.logics.entities.obstacles.zapper.ZapperBase;
import game.logics.entities.obstacles.zapper.ZapperInstance;
import game.logics.entities.obstacles.zapper.ZapperRay;
import game.logics.entities.pickups.shield.Shield;
import game.logics.entities.pickups.teleport.Teleport;
import game.logics.handler.AbstractLogics;
import game.utility.debug.Debugger;
import game.utility.other.EntityType;
import game.utility.other.FormatException;
import game.utility.other.Pair;

/**
 * The class {@link TileGenerator} handles the generation of tiles of
 * obstacles during the game.
 * 
 * {@link TileGenerator} works on a separated thread which can be manually
 * controlled by the {@link game.logics.handler.LogicsHandler LogicsHandler}.
 * 
 */
public class TileGenerator implements Generator {

    /**
     * The type of file separator the system uses.
     */
    private static final String SEPARATOR = System.getProperty("file.separator");
    /**
     * Path where all tiles information file is located.
     */
    private static final String TILES_PATH = System.getProperty("user.dir") + SEPARATOR + "res" + SEPARATOR + "game" + SEPARATOR + "generator" + SEPARATOR + "tiles.json";

    /**
     * A function used by the generator for creating <code>ZapperRay</code> object.
     */
    private Optional<BiFunction<Pair<ZapperBase, ZapperBase>, Pair<Double, Double>, ZapperRay>> createZRay = Optional.empty();
    /**
     * A function used by the generator for creating <code>ZapperBase</code> object.
     */
    private Optional<Function<Pair<Double, Double>, ZapperBase>> createZBase = Optional.empty();
    /**
     * A function used by the generator for creating <code>Missile</code> object.
     */
    private Optional<Function<Pair<Double, Double>, Missile>> createMissile = Optional.empty();
    /**
     * A function used by the generator for creating <code>Shield</code> object.
     */
    private Optional<Function<Pair<Double, Double>, Shield>> createShield = Optional.empty();
    /**
     * A function used by the generator for creating <code>Teleport</code> object.
     */
    private Optional<Function<Pair<Double, Double>, Teleport>> createTeleport = Optional.empty();

    /**
     * A map containing lists where all loaded set of tiles are stored.
     */
    private final Map<EntityType, List<Set<Entity>>> tileSets = new HashMap<>();

    /**
     * The entities map where the spawner adds the sets of obstacles.
     */
    private final Map<EntityType, Set<Entity>> entities;

    /**
     * Decides the odds for the generator to spawn a set of missiles.
     */
    private static final int MISSILE_ODDS = 30;
    /**
     * Decides the odds for the generator to spawn a power up.
     */
    private static final int POWERUP_ODDS = 5;

    /**
     * Decides how many seconds the generator pauses after each set spawned.
     */
    private final long interval;
    private static final long INTERVAL_DECREASE_DIFF = 40;
    private static final long MINIMAL_INTERVAL = 20;

    private final Thread generator = new Thread(this);
    private boolean running;
    private boolean waiting;
    private boolean loaded;

    private final int tileSize;

    private long systemTimeBeforeSleep;
    private long systemTimeAfterPaused;
    private long remainingTimeToSleep;
    private long sleepTimeLeft;
    private long sleepInterval;

    private static final Random RNG = new Random();

    /**
     * Constructor that sets up the entities structure where obstacles will be
     * added and allows to specify the interval for spawning.
     * 
     * @param entities the entities map where obstacles will be added
     * @param interval the interval between each generation
     */
    public TileGenerator(final Map<EntityType, Set<Entity>> entities, final double interval) {
        this.entities = entities;
        this.tileSize = GameWindow.GAME_SCREEN.getTileSize();
        this.interval = (long) (interval * GameWindow.MILLI_SECOND + INTERVAL_DECREASE_DIFF);
        this.sleepInterval = this.interval;

        EntityType.ALL_ENTITY_TYPE.stream()
                .filter(e -> e.isGenerableEntity())
                .collect(Collectors.toList())
                .forEach(e -> tileSets.put(e, new ArrayList<>()));
    }

    private Object checkParse(final Object parsed) throws FormatException {
        if (parsed == null) {
            throw new FormatException("Json Parse returned null when it was expecting an Object.");
        }
        return parsed;
    }

    private void loadZappers(final JsonArray types) throws FormatException {

        for (int i = 0; i < types.size(); i++) {
            final JsonArray zsets = (JsonArray) checkParse(types.get(i));
            final Set<Entity> tile = new HashSet<>();

            for (int j = 0; j < zsets.size(); j++) {
                final JsonArray set = (JsonArray) checkParse(zsets.get(j));

                if (set.size() >= 2) {
                    final Set<ZapperRay> tmp = new HashSet<>();

                    final JsonObject b1 = (JsonObject) checkParse(set.get(0));
                    final JsonObject b2 = (JsonObject) checkParse(set.get(1));

                    final ZapperBase base1 = createZBase.get().apply(new Pair<>(
                            Double.parseDouble((String) b1.get("x")) * tileSize,
                            Double.parseDouble((String) b1.get("y")) * tileSize));
                    final ZapperBase base2 = createZBase.get().apply(new Pair<>(
                            Double.parseDouble((String) b2.get("x")) * tileSize,
                            Double.parseDouble((String) b2.get("y")) * tileSize));

                    for (int h = 2; h < set.size(); h++) {
                        final JsonObject ray = (JsonObject) checkParse(set.get(h));
                        tmp.add(createZRay.get().apply(new Pair<>(base1, base2), new Pair<>(
                                Double.parseDouble((String) ray.get("x")) * tileSize,
                                Double.parseDouble((String) ray.get("y")) * tileSize)));
                    }
                    final Zapper master = new ZapperInstance(base1, base2, tmp);

                    base1.setMaster(master);
                    base2.setMaster(master);
                    tile.add(master);
                }
            }
            tileSets.get(EntityType.ZAPPER).add(tile);
        }
    }

    private void loadMissiles(final JsonArray types) throws FormatException {
        for (int i = 0; i < types.size(); i++) {
            final JsonArray sets = (JsonArray) checkParse(types.get(i));
            final Set<Entity> tmp = new HashSet<>();

            for (int j = 0; j < sets.size(); j++) {
                final JsonObject missile = (JsonObject) checkParse(sets.get(j));

                tmp.add(createMissile.get().apply(new Pair<>(
                    Double.parseDouble((String) missile.get("x")) * tileSize,
                    Double.parseDouble((String) missile.get("y")) * tileSize)));
            }
            tileSets.get(EntityType.MISSILE).add(tmp);
        }
    }

    private void loadShields(final JsonArray types) throws FormatException {
       for (int i = 0; i < types.size(); i++) {
            final JsonArray sets = (JsonArray) checkParse(types.get(i));
            final Set<Entity> tmp = new HashSet<>();

            for (int j = 0; j < sets.size(); j++) {
                final JsonObject shield = (JsonObject) checkParse(sets.get(j));

                tmp.add(createShield.get().apply(new Pair<>(
                    Double.parseDouble((String) shield.get("x")) * tileSize,
                    Double.parseDouble((String) shield.get("y")) * tileSize)));
            }
            tileSets.get(EntityType.SHIELD).add(tmp);
        }
    }

    private void loadTeleport(final JsonArray types) throws FormatException {
       for (int i = 0; i < types.size(); i++) {
            final JsonArray sets = (JsonArray) checkParse(types.get(i));
            final Set<Entity> tmp = new HashSet<>();

            for (int j = 0; j < sets.size(); j++) {
                final JsonObject teleport = (JsonObject) checkParse(sets.get(j));

                tmp.add(createTeleport.get().apply(new Pair<>(
                    Double.parseDouble((String) teleport.get("x")) * tileSize,
                    Double.parseDouble((String) teleport.get("y")) * tileSize)));
            }
            tileSets.get(EntityType.TELEPORT).add(tmp);
        }
    }

    /**
     * Loads each set of obstacles from a json file and store them in Lists.
     * 
     * @throws JsonException if parser fails to parse into objects the contents of json file
     * @throws FileNotFoundException if json file cannot be found
     * @throws FormatException if json file is not correctly formatted
     */
    private void loadTiles() throws FileNotFoundException, JsonException, FormatException {
        final Object parsed = Jsoner.deserialize(new FileReader(TILES_PATH));
        final JsonObject allTiles = (JsonObject) checkParse(parsed);

        ///        LOADING ZAPPERS        ///
        if (createZBase.isPresent() && createZRay.isPresent()) {
            final JsonArray types = (JsonArray) checkParse(allTiles.get(EntityType.ZAPPER.toString()));
            this.loadZappers(types);
        }
        ///        LOADING MISSILES    ///
        if (createMissile.isPresent()) {
            final JsonArray types = (JsonArray) checkParse(allTiles.get(EntityType.MISSILE.toString()));
            this.loadMissiles(types);
        }

        ///        LOADING SHIELDS     ///
        if (createShield.isPresent()) {
            final JsonArray types = (JsonArray) checkParse(allTiles.get(EntityType.SHIELD.toString()));
            this.loadShields(types);
        }

        ///        LOADING TELEPORTS      ///
        if (createTeleport.isPresent()) {
            final JsonArray types = (JsonArray) checkParse(allTiles.get(EntityType.TELEPORT.toString()));
            this.loadTeleport(types);
        }

        this.loaded = true;
    }

    private void spawnTile() {
        int randomNumber = RNG.nextInt() % 100;
        randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;

        if (randomNumber <= POWERUP_ODDS) {
            randomNumber = RNG.nextInt() % 2;
            randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;
            randomNumber += EntityType.SHIELD.ordinal();
            spawnSet(EntityType.values()[randomNumber]);
        } else if (randomNumber <= MISSILE_ODDS) {
            spawnSet(EntityType.MISSILE);
        } else {
            spawnSet(EntityType.ZAPPER);
        }
    }

    private void spawnSet(final EntityType type) {
        if (!this.loaded) {
            return;
        }

        boolean continueSearch;
        int randomNumber;
        do {
            continueSearch = false;
            randomNumber = RNG.nextInt() % tileSets.get(type).size();
            randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;

            for (final Entity e : tileSets.get(type).get(randomNumber)) {
                if (entities.get(type).contains(e)) {
                    continueSearch = true;
                    break;
                }
            }
        } while (continueSearch);

        if (!this.isWaiting()) {
            tileSets.get(type).get(randomNumber).forEach(e -> GameWindow.GAME_DEBUGGER.printLog(Debugger.Option.LOG_SPAWN, "spawned::" + e.toString()));
            entities.get(type).addAll(tileSets.get(type).get(randomNumber));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setZapperRayCreator(final BiFunction<Pair<ZapperBase, ZapperBase>, Pair<Double, Double>, ZapperRay> zapperr) {
        this.createZRay = Optional.of(zapperr);
    }
    /**
     * {@inheritDoc}
     */
    public void setZapperBaseCreator(final Function<Pair<Double, Double>, ZapperBase> zapperb) {
        this.createZBase = Optional.of(zapperb);
    }
    /**
     * {@inheritDoc}
     */
    public void setMissileCreator(final Function<Pair<Double, Double>, Missile> missile) {
        this.createMissile = Optional.of(missile);
    }
    /**
     * {@inheritDoc}
     */
    public void setShieldCreator(final Function<Pair<Double, Double>, Shield> shield) {
        this.createShield = Optional.of(shield);
    }
    /**
     * {@inheritDoc}
     */
    public void setTeleportCreator(final Function<Pair<Double, Double>, Teleport> teleport) {
        this.createTeleport = Optional.of(teleport);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isRunning() {
        return running;
    }
    /**
     * {@inheritDoc}
     */
    public boolean isWaiting() {
        return waiting;
    }

    private void invokeSleep(final long interval) {
        try {
            Thread.sleep(interval > 0 ? interval : 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() throws FileNotFoundException, JsonException, FormatException {
        this.loadTiles();
        this.start();
    }
    /**
     * {@inheritDoc}
     */
    public void start() {
        synchronized (generator) {
            if (!this.isRunning()) {
                running = true;
                waiting = true;
                generator.start();
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    public void terminate() {
        running = false;
        this.resume();
    }
    /**
     * {@inheritDoc}
     */
    public void stop() {
        waiting = true;

        synchronized (this) {
            remainingTimeToSleep = 0;
        }
    }
    /**
     * {@inheritDoc}
     */
    public void pause() {
        waiting = true;

        synchronized (this) {
            final long timePassed = System.nanoTime() / GameWindow.MICRO_SECOND - systemTimeBeforeSleep;
            remainingTimeToSleep = sleepInterval - timePassed;
        }
    }
    /**
     * {@inheritDoc}
     */
    public void resume() {
        synchronized (generator) {
            if (this.isWaiting()) {
                waiting = false;
                generator.notifyAll();

                synchronized (this) {
                    final long timePassed = System.nanoTime() / GameWindow.MICRO_SECOND - systemTimeBeforeSleep;
                    sleepTimeLeft = sleepInterval - timePassed > 0 ? sleepInterval - timePassed : 0;
                    remainingTimeToSleep = timePassed < sleepInterval ? remainingTimeToSleep - sleepTimeLeft : remainingTimeToSleep;
                    systemTimeAfterPaused = System.nanoTime() / GameWindow.MICRO_SECOND; 
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void drawNextSpawnTimer(final Graphics2D g) {
        final int spawnTimerXLocation = 3;
        final int spawnTimerYLocation = 17;

        if (GameWindow.GAME_DEBUGGER.isFeatureEnabled(Debugger.Option.NEXT_SPAWN_TIMER)) {
            synchronized (this) {
                final long expectedTimer = sleepInterval - (System.nanoTime() / GameWindow.MICRO_SECOND - systemTimeBeforeSleep);
                final long remainingTime = remainingTimeToSleep + sleepTimeLeft - (System.nanoTime() / GameWindow.MICRO_SECOND - systemTimeAfterPaused);
                final long timer = !this.isWaiting() ? remainingTime > 0 ? remainingTime : expectedTimer  : remainingTimeToSleep;

                g.setColor(Debugger.DEBUG_COLOR);
                g.setFont(Debugger.DEBUG_FONT);
                g.drawString("NEXT: " + timer + "ms", spawnTimerXLocation, spawnTimerYLocation);
            }
        }
    }

    /**
     * Begins the execution of the spawner thread.
     */
    @Override
    public void run() {
        final long minimum = (interval - INTERVAL_DECREASE_DIFF) * MINIMAL_INTERVAL / 100;

        while (generator.isAlive() && this.isRunning()) {

            synchronized (generator) {
                while (this.isWaiting()) {
                    try {
                        generator.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!this.isRunning()) {
                        continue;
                    }
                }
                this.invokeSleep(remainingTimeToSleep);
                synchronized (this) {
                    remainingTimeToSleep = 0;
                    sleepTimeLeft = 0;
                }
            }

            synchronized (entities) {
                spawnTile();
            }

            synchronized (this) {
                systemTimeBeforeSleep = System.nanoTime() / GameWindow.MICRO_SECOND;
                sleepInterval =  interval - INTERVAL_DECREASE_DIFF * AbstractLogics.getDifficultyLevel() > minimum
                    ? interval - INTERVAL_DECREASE_DIFF * AbstractLogics.getDifficultyLevel()
                    : minimum;
            }
            this.invokeSleep(sleepInterval);
        }
    }
}
