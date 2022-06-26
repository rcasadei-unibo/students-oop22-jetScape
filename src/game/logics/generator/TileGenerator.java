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
import game.logics.entities.pickups.coin.Coin;
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
     * A function used by the generator for creating <code>Coin</code> object.
     */
    private Optional<Function<Pair<Double, Double>, Coin>> createCoins = Optional.empty();

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
    private static final int MISSILE_ODDS = 35;
    /**
     * Decides the odds for the generator to spawn a group of coins.
     */
    private static final int MONEY_ODDS = 15;
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
                .forEach(e -> this.tileSets.put(e, new ArrayList<>()));
    }

    private Object checkParse(final Object parsed) throws FormatException {
        if (parsed == null) {
            throw new FormatException("Json Parse returned null when it was expecting an Object.");
        }
        return parsed;
    }

    private void loadZappers(final JsonArray types) throws FormatException {

        for (int i = 0; i < types.size(); i++) {
            final JsonArray zsets = (JsonArray) this.checkParse(types.get(i));
            final Set<Entity> tile = new HashSet<>();

            for (int j = 0; j < zsets.size(); j++) {
                final JsonArray set = (JsonArray) this.checkParse(zsets.get(j));

                if (set.size() >= 2) {
                    final Set<ZapperRay> tmp = new HashSet<>();

                    final JsonObject b1 = (JsonObject) this.checkParse(set.get(0));
                    final JsonObject b2 = (JsonObject) this.checkParse(set.get(1));

                    final ZapperBase base1 = this.createZBase.get().apply(new Pair<>(
                            Double.parseDouble((String) b1.get("x")) * this.tileSize,
                            Double.parseDouble((String) b1.get("y")) * this.tileSize));
                    final ZapperBase base2 = this.createZBase.get().apply(new Pair<>(
                            Double.parseDouble((String) b2.get("x")) * this.tileSize,
                            Double.parseDouble((String) b2.get("y")) * this.tileSize));

                    for (int h = 2; h < set.size(); h++) {
                        final JsonObject ray = (JsonObject) this.checkParse(set.get(h));
                        tmp.add(this.createZRay.get().apply(new Pair<>(base1, base2), new Pair<>(
                                Double.parseDouble((String) ray.get("x")) * this.tileSize,
                                Double.parseDouble((String) ray.get("y")) * this.tileSize)));
                    }
                    final Zapper master = new ZapperInstance(base1, base2, tmp);

                    base1.setMaster(master);
                    base2.setMaster(master);
                    tile.add(master);
                }
            }
            this.tileSets.get(EntityType.ZAPPER).add(tile);
        }
    }

    private void loadMissiles(final JsonArray types) throws FormatException {
        for (int i = 0; i < types.size(); i++) {
            final JsonArray sets = (JsonArray) this.checkParse(types.get(i));
            final Set<Entity> tmp = new HashSet<>();

            for (int j = 0; j < sets.size(); j++) {
                final JsonObject missile = (JsonObject) this.checkParse(sets.get(j));

                tmp.add(this.createMissile.get().apply(new Pair<>(
                    Double.parseDouble((String) missile.get("x")) * this.tileSize,
                    Double.parseDouble((String) missile.get("y")) * this.tileSize)));
            }
            this.tileSets.get(EntityType.MISSILE).add(tmp);
        }
    }

    private void loadShields(final JsonArray types) throws FormatException {
       for (int i = 0; i < types.size(); i++) {
            final JsonArray sets = (JsonArray) this.checkParse(types.get(i));
            final Set<Entity> tmp = new HashSet<>();

            for (int j = 0; j < sets.size(); j++) {
                final JsonObject shield = (JsonObject) this.checkParse(sets.get(j));

                tmp.add(this.createShield.get().apply(new Pair<>(
                    Double.parseDouble((String) shield.get("x")) * this.tileSize,
                    Double.parseDouble((String) shield.get("y")) * this.tileSize)));
            }
            this.tileSets.get(EntityType.SHIELD).add(tmp);
        }
    }

    private void loadTeleport(final JsonArray types) throws FormatException {
       for (int i = 0; i < types.size(); i++) {
            final JsonArray sets = (JsonArray) this.checkParse(types.get(i));
            final Set<Entity> tmp = new HashSet<>();

            for (int j = 0; j < sets.size(); j++) {
                final JsonObject teleport = (JsonObject) this.checkParse(sets.get(j));

                tmp.add(this.createTeleport.get().apply(new Pair<>(
                    Double.parseDouble((String) teleport.get("x")) * this.tileSize,
                    Double.parseDouble((String) teleport.get("y")) * this.tileSize)));
            }
            this.tileSets.get(EntityType.TELEPORT).add(tmp);
        }
    }

    private void loadCoin(final JsonArray types) throws FormatException {
        for (int i = 0; i < types.size(); i++) {
             final JsonArray sets = (JsonArray) this.checkParse(types.get(i));
             final Set<Entity> tile = new HashSet<>();

             for (int j = 0; j < sets.size(); j++) {
                 final JsonArray set = (JsonArray) this.checkParse(sets.get(j));
                 final Set<Coin> tmp = new HashSet<>();

                 for (int h = 0; h < set.size(); h++) {
                     final JsonObject coin = (JsonObject) this.checkParse(set.get(h));
                     tmp.add(createCoins.get().apply(new Pair<>(
                             Double.parseDouble((String) coin.get("x")) * this.tileSize,
                             Double.parseDouble((String) coin.get("y")) * this.tileSize)));
                 }
                 tile.addAll(tmp);
             }
             this.tileSets.get(EntityType.COIN).add(tile);
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
        final JsonObject allTiles = (JsonObject) this.checkParse(parsed);

        ///        LOADING ZAPPERS        ///
        if (this.createZBase.isPresent() && this.createZRay.isPresent()) {
            final JsonArray types = (JsonArray) this.checkParse(allTiles.get(EntityType.ZAPPER.toString()));
            this.loadZappers(types);
        }
        ///        LOADING MISSILES    ///
        if (this.createMissile.isPresent()) {
            final JsonArray types = (JsonArray) this.checkParse(allTiles.get(EntityType.MISSILE.toString()));
            this.loadMissiles(types);
        }

        ///        LOADING SHIELDS     ///
        if (this.createShield.isPresent()) {
            final JsonArray types = (JsonArray) this.checkParse(allTiles.get(EntityType.SHIELD.toString()));
            this.loadShields(types);
        }

        ///        LOADING TELEPORTS      ///
        if (this.createTeleport.isPresent()) {
            final JsonArray types = (JsonArray) this.checkParse(allTiles.get(EntityType.TELEPORT.toString()));
            this.loadTeleport(types);
        }

        ///        LOADING COINS      ///
        if (this.createCoins.isPresent()) {
            final JsonArray types = (JsonArray) this.checkParse(allTiles.get(EntityType.COIN.toString()));
            this.loadCoin(types);
        }
    }

    private void spawnTile() {
        int randomNumber = RNG.nextInt() % 100;
        randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;

        if (randomNumber <= POWERUP_ODDS) {
            randomNumber = RNG.nextInt() % 2;
            randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;
            randomNumber += EntityType.SHIELD.ordinal();
            spawnSet(EntityType.values()[randomNumber]);
        } else if (randomNumber <= MONEY_ODDS) {
            spawnSet(EntityType.COIN);
        } else if (randomNumber <= MISSILE_ODDS) {
            spawnSet(EntityType.MISSILE);
        } else {
            spawnSet(EntityType.ZAPPER);
        }
    }

    private void spawnSet(final EntityType type) {
        boolean continueSearch;
        int randomNumber;
        do {
            continueSearch = false;
            randomNumber = RNG.nextInt() % tileSets.get(type).size();
            randomNumber = randomNumber < 0 ? randomNumber * -1 : randomNumber;

            for (final Entity e : tileSets.get(type).get(randomNumber)) {
                if (this.entities.get(type).contains(e)) {
                    continueSearch = true;
                    break;
                }
            }
        } while (continueSearch);

        if (!this.isWaiting()) {
            this.tileSets.get(type).get(randomNumber)
                    .forEach(e -> GameWindow.GAME_DEBUGGER
                            .printLog(Debugger.Option.LOG_SPAWN, "spawned:" + e.toString()));
            this.entities.get(type).addAll(tileSets.get(type).get(randomNumber));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setZapperRayCreator(final BiFunction<Pair<ZapperBase, ZapperBase>,
            Pair<Double, Double>, ZapperRay> zapperray) {
        this.createZRay = Optional.of(zapperray);
    }
    /**
     * {@inheritDoc}
     */
    public void setZapperBaseCreator(final Function<Pair<Double, Double>, ZapperBase> zapperbase) {
        this.createZBase = Optional.of(zapperbase);
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
    public void setCoinCreator(final Function<Pair<Double, Double>, Coin> coins) {
        this.createCoins = Optional.of(coins);
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
                this.running = true;
                this.waiting = true;
                this.generator.start();
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    public void terminate() {
        this.running = false;
        this.resume();
    }
    /**
     * {@inheritDoc}
     */
    public void stop() {
        this.waiting = true;

        synchronized (this) {
            this.remainingTimeToSleep = 0;
        }
    }
    /**
     * {@inheritDoc}
     */
    public void pause() {
        this.waiting = true;

        synchronized (this) {
            final long timePassed = System.nanoTime() / GameWindow.MICRO_SECOND - this.systemTimeBeforeSleep;
            this.remainingTimeToSleep = this.sleepInterval - timePassed;
        }
    }
    /**
     * {@inheritDoc}
     */
    public void resume() {
        synchronized (generator) {
            if (this.isWaiting()) {
                this.waiting = false;
                this.generator.notifyAll();

                synchronized (this) {
                    final long timePassed = System.nanoTime()
                            / GameWindow.MICRO_SECOND - this.systemTimeBeforeSleep;
                    this.sleepTimeLeft = this.sleepInterval - timePassed > 0
                            ? this.sleepInterval - timePassed : 0;
                    this.remainingTimeToSleep = timePassed < this.sleepInterval
                            ? this.remainingTimeToSleep - this.sleepTimeLeft
                            : this.remainingTimeToSleep;
                    this.systemTimeAfterPaused = System.nanoTime() / GameWindow.MICRO_SECOND; 
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
                final long expectedTimer = this.sleepInterval
                        - (System.nanoTime() / GameWindow.MICRO_SECOND - this.systemTimeBeforeSleep);
                final long remainingTime = this.remainingTimeToSleep
                        + this.sleepTimeLeft
                        - (System.nanoTime() / GameWindow.MICRO_SECOND - this.systemTimeAfterPaused);
                final long timer = !this.isWaiting()
                        ? remainingTime > 0 ? remainingTime : expectedTimer
                        : this.remainingTimeToSleep;

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
        final long minimum = (this.interval - TileGenerator.INTERVAL_DECREASE_DIFF)
                * TileGenerator.MINIMAL_INTERVAL / 100;

        while (this.generator.isAlive() && this.isRunning()) {

            synchronized (this.generator) {
                while (this.isWaiting()) {
                    try {
                        this.generator.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!this.isRunning()) {
                        continue;
                    }
                }
                this.invokeSleep(this.remainingTimeToSleep);
                synchronized (this) {
                    this.remainingTimeToSleep = 0;
                    this.sleepTimeLeft = 0;
                }
            }

            synchronized (this.entities) {
                spawnTile();
            }

            synchronized (this) {
                this.systemTimeBeforeSleep = System.nanoTime() / GameWindow.MICRO_SECOND;
                this.sleepInterval = interval - TileGenerator.INTERVAL_DECREASE_DIFF * AbstractLogics.getDifficultyLevel() > minimum
                    ? this.interval - TileGenerator.INTERVAL_DECREASE_DIFF * AbstractLogics.getDifficultyLevel()
                    : minimum;
            }
            this.invokeSleep(this.sleepInterval);
        }
    }
}
