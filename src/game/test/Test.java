package game.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import game.frame.GameWindow;
import java.awt.event.KeyEvent;
import game.logics.display.handlers.MenuHandler;
import game.logics.display.view.DisplayPause;
import game.logics.entities.generic.Entity;
import game.logics.entities.obstacles.missile.Missile;
import game.logics.entities.obstacles.missile.MissileInstance;
import game.logics.entities.obstacles.zapper.Zapper;
import game.logics.entities.obstacles.zapper.ZapperBase;
import game.logics.entities.obstacles.zapper.ZapperBaseInstance;
import game.logics.entities.obstacles.zapper.ZapperInstance;
import game.logics.entities.obstacles.zapper.ZapperRay;
import game.logics.entities.obstacles.zapper.ZapperRayInstance;
import game.logics.entities.pickups.shield.Shield;
import game.logics.entities.pickups.shield.ShieldInstance;
import game.logics.entities.pickups.teleport.Teleport;
import game.logics.entities.pickups.teleport.TeleportInstance;
import game.logics.entities.player.Player;
import game.logics.entities.player.Player.PlayerDeath;
import game.logics.entities.player.PlayerInstance;
import game.logics.generator.Generator;
import game.logics.generator.TileGenerator;
import game.logics.handler.Logics;
import game.logics.handler.LogicsHandler;
import game.logics.interactions.SpeedHandler;
import game.logics.records.Records;
import game.utility.input.JSONHandler;
import game.utility.input.JSONReader;
import game.utility.input.JSONReaderImpl;
import game.utility.input.JSONWriter;
import game.utility.input.JSONWriterImpl;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.EntityType;
import game.utility.other.MenuOption;
import game.utility.other.Pair;
import game.utility.other.Sound;
import game.utility.sound.SoundManager;

/**
 * JUnit test class.
 */
public class Test {

    /**
     * Tile generator test class.
     */
    @org.junit.Test
    public void generatorTest() {
        final double interval = 3.3;
        final Logics logics = new LogicsHandler();
        final Generator gen = new TileGenerator(logics.getEntities(), interval);

        assertFalse(gen.isRunning());
        assertFalse(gen.isWaiting());

        gen.start();
        assertTrue(gen.isRunning());
        assertTrue(gen.isWaiting());

        gen.resume();
        assertTrue(gen.isRunning());
        assertFalse(gen.isWaiting());

        gen.pause();
        assertTrue(gen.isRunning());
        assertTrue(gen.isWaiting());

        gen.resume();
        assertTrue(gen.isRunning());
        assertFalse(gen.isWaiting());

        gen.stop();
        assertTrue(gen.isRunning());
        assertTrue(gen.isWaiting());

        gen.resume();
        assertTrue(gen.isRunning());
        assertFalse(gen.isWaiting());

        gen.pause();
        assertTrue(gen.isRunning());
        assertTrue(gen.isWaiting());

        gen.terminate();
        assertFalse(gen.isRunning());
        assertFalse(gen.isWaiting());
    }

    /**
     * Player entity test class.
     */
    @org.junit.Test
    public void playerTest() {
        final Logics logics = new LogicsHandler();
        final Player player = new PlayerInstance(logics);

        assertEquals(EntityType.PLAYER, player.entityType());
        logics.getEntities().get(player.entityType()).add(player);

        final int updates = 20;
        for (int i = 0; i < updates; i++) {
            player.update();
        }

        assertEquals(updates / 2 - 1, player.getCurrentScore());
        assertFalse(player.hasDied());
        assertTrue(player.isOnScreenBounds());
        assertFalse(player.isOnClearArea());
        assertFalse(player.isOnSpawnArea());

        player.clean();
        player.update();

        assertEquals(0, player.getCurrentScore());
        assertFalse(player.hasDied());
        assertTrue(player.isOnScreenBounds());
        assertFalse(player.isOnClearArea());
        assertFalse(player.isOnSpawnArea());

        assertTrue(logics.getEntities().get(player.entityType()).isEmpty());
    }

    /**
     * Zapper entity test class.
     */
    @org.junit.Test
    public void zapperTest() {
        final Logics logics = new LogicsHandler();
        final Pair<Double, Double> pos = new Pair<>(17.0 * GameWindow.GAME_SCREEN.getTileSize(), (double) GameWindow.GAME_SCREEN.getTileSize());
        final SpeedHandler movement = new SpeedHandler(250.0, 15.0, 0);
        final Pair<ZapperBase, ZapperBase> b = new Pair<>(
                new ZapperBaseInstance(logics, pos, movement),
                new ZapperBaseInstance(logics, pos, movement)
        );
        final ZapperRay r = new ZapperRayInstance(logics, pos, b.getX(), b.getY());

        final Zapper zapper = new ZapperInstance(b.getX(), b.getY(),
                Set.of(r));
        b.getX().setMaster(zapper);
        b.getY().setMaster(zapper);

        assertEquals(EntityType.ZAPPER, zapper.entityType());
        logics.getEntities().get(zapper.entityType()).add(zapper);

        assertEquals(b, zapper.getBothBases());
        assertEquals(b.getY(), zapper.getPaired(b.getX()));
        assertEquals(b.getX(), zapper.getPaired(b.getY()));
        assertEquals(Set.of(b.getX(), b.getY(), r), zapper.getEntitiesSet());

        assertFalse(zapper.isOnScreenBounds());
        assertFalse(zapper.isOnClearArea());
        assertTrue(zapper.isOnSpawnArea());

        while (zapper.getPosition().getX() >= GameWindow.GAME_SCREEN.getWidth()) {
            zapper.update();
        }
        zapper.update();

        assertTrue(zapper.isOnScreenBounds());
        assertFalse(zapper.isOnClearArea());
        assertFalse(zapper.isOnSpawnArea());

        while (zapper.getPosition().getX() >= -GameWindow.GAME_SCREEN.getTileSize()) {
            zapper.update();
        }
        zapper.update();

        assertFalse(zapper.isOnScreenBounds());
        assertTrue(zapper.isOnClearArea());
        assertFalse(zapper.isOnSpawnArea());

        zapper.clean();
        zapper.update();

        assertFalse(zapper.isOnScreenBounds());
        assertFalse(zapper.isOnClearArea());
        assertTrue(zapper.isOnSpawnArea());

        assertTrue(logics.getEntities().get(zapper.entityType()).isEmpty());
    }

    /**
     * Missile entity test class.
     */
    @org.junit.Test
    public void missileTest() {
        final Logics logics = new LogicsHandler();
        final Pair<Double, Double> pos = new Pair<>(35.0 * GameWindow.GAME_SCREEN.getTileSize(), (double) GameWindow.GAME_SCREEN.getTileSize());
        final SpeedHandler movement = new SpeedHandler(500.0, 10.0, 5000.0);
        final Player player = new PlayerInstance(logics);
        final Missile missile = new MissileInstance(logics, pos, player, movement);

        assertEquals(EntityType.MISSILE, missile.entityType());
        logics.getEntities().get(missile.entityType()).add(missile);

        assertFalse(missile.isOnScreenBounds());
        assertFalse(missile.isOnClearArea());
        assertTrue(missile.isOnSpawnArea());

        while (missile.getPosition().getX() >= GameWindow.GAME_SCREEN.getWidth()) {
            missile.update();
        }
        missile.update();

        assertTrue(missile.isOnScreenBounds());
        assertFalse(missile.isOnClearArea());
        assertFalse(missile.isOnSpawnArea());

        while (missile.getPosition().getX() >= -GameWindow.GAME_SCREEN.getTileSize()) {
            missile.update();
        }
        missile.update();

        assertFalse(missile.isOnScreenBounds());
        assertTrue(missile.isOnClearArea());
        assertFalse(missile.isOnSpawnArea());

        missile.clean();
        missile.update();

        assertFalse(missile.isOnScreenBounds());
        assertFalse(missile.isOnClearArea());
        assertTrue(missile.isOnSpawnArea());

        assertTrue(logics.getEntities().get(missile.entityType()).isEmpty());
    }

    /**
     * Shield entity test class.
     */
    @org.junit.Test
    public void shieldTest() {
        final Logics logics = new LogicsHandler();
        final Pair<Double, Double> pos = new Pair<>(17.0 * GameWindow.GAME_SCREEN.getTileSize(), (double) GameWindow.GAME_SCREEN.getTileSize());
        final SpeedHandler movement = new SpeedHandler(250.0, 15.0, 0);
        final Player player = new PlayerInstance(logics);
        final Shield shield = new ShieldInstance(logics, pos, player, movement);

        assertEquals(EntityType.SHIELD, shield.entityType());
        logics.getEntities().get(shield.entityType()).add(shield);

        assertFalse(shield.isOnScreenBounds());
        assertFalse(shield.isOnClearArea());
        assertTrue(shield.isOnSpawnArea());

        while (shield.getPosition().getX() >= GameWindow.GAME_SCREEN.getWidth()) {
            shield.update();
        }
        shield.update();

        assertTrue(shield.isOnScreenBounds());
        assertFalse(shield.isOnClearArea());
        assertFalse(shield.isOnSpawnArea());

        while (shield.getPosition().getX() >= -GameWindow.GAME_SCREEN.getTileSize()) {
            shield.update();
        }
        shield.update();

        assertFalse(shield.isOnScreenBounds());
        assertTrue(shield.isOnClearArea());
        assertFalse(shield.isOnSpawnArea());

        shield.clean();
        shield.update();

        assertFalse(shield.isOnScreenBounds());
        assertFalse(shield.isOnClearArea());
        assertTrue(shield.isOnSpawnArea());

        assertTrue(logics.getEntities().get(shield.entityType()).isEmpty());
    }

    /**
     * Teleport entity test class.
     */
    @org.junit.Test
    public void teleportTest() {
        final Logics logics = new LogicsHandler();
        final Pair<Double, Double> pos = new Pair<>(17.0 * GameWindow.GAME_SCREEN.getTileSize(), (double) GameWindow.GAME_SCREEN.getTileSize());
        final SpeedHandler movement = new SpeedHandler(250.0, 15.0, 0);
        final Player player = new PlayerInstance(logics);
        final Teleport teleport = new TeleportInstance(logics, pos, player, movement);

        assertEquals(EntityType.TELEPORT, teleport.entityType());
        logics.getEntities().get(teleport.entityType()).add(teleport);

        assertFalse(teleport.isOnScreenBounds());
        assertFalse(teleport.isOnClearArea());
        assertTrue(teleport.isOnSpawnArea());

        while (teleport.getPosition().getX() >= GameWindow.GAME_SCREEN.getWidth()) {
            teleport.update();
        }
        teleport.update();

        assertTrue(teleport.isOnScreenBounds());
        assertFalse(teleport.isOnClearArea());
        assertFalse(teleport.isOnSpawnArea());

        while (teleport.getPosition().getX() >= -GameWindow.GAME_SCREEN.getTileSize()) {
            teleport.update();
        }
        teleport.update();

        assertFalse(teleport.isOnScreenBounds());
        assertTrue(teleport.isOnClearArea());
        assertFalse(teleport.isOnSpawnArea());

        teleport.clean();
        teleport.update();

        assertFalse(teleport.isOnScreenBounds());
        assertFalse(teleport.isOnClearArea());
        assertTrue(teleport.isOnSpawnArea());

        assertTrue(logics.getEntities().get(teleport.entityType()).isEmpty());
    }

    //FIXME
    /**
     * Records file writing test.
     */
    @org.junit.Test
    public void recordTest() {

        final int burnedTimes = 3;
        final Logics logics = new LogicsHandler();
        final Player player = new PlayerInstance(logics);
        final Records records = new Records(() -> logics.getGame().getActualGame(), player);

        records.clear();
        for (int i = 0; i < burnedTimes; i++) {
            // Adds a new missile
            final Pair<Double, Double> missilePos = new Pair<>(player.getPosition().getX() + 25, player.getPosition().getY());
            final SpeedHandler missileMovement = new SpeedHandler(500.0, 10.0, 5000.0);
            final Missile missile = new MissileInstance(logics, missilePos, player, missileMovement);

            logics.getEntities().get(missile.entityType()).add(missile);

            // Wait the collision
            while (missile.getPosition().getX() > player.getPosition().getX()) {
                missile.update();
            }

            // Missile hits the player
            missile.update();
            player.update();

            assertEquals(PlayerDeath.BURNED, player.getCauseOfDeath());

            //records.refresh();
            //logics.getGame().getActualGame().setGameEnded(500);
            //records.fetch(logics.getGame().getActualGame());
            //records.update();
        }
        assertEquals(burnedTimes, records.getBurnedTimes());
    }

    /**
     * Records file writing test.
     */
    @org.junit.Test
    public void writingTest() {
        final Logics logics = new LogicsHandler();
        final Player player = new PlayerInstance(logics);
        final Records records = new Records(() -> logics.getGame().getActualGame(), player);
        final JSONWriter writer = new JSONWriterImpl(records);

        final int burnedTimes = 5;
        final int zappedTimes = 2;
        final int highestScoreRecord = 400;
        final int lowestScoreRecord = 250;

        records.clear();

        records.setBurnedTimes(burnedTimes);
        records.setZappedTimes(zappedTimes);

        records.addRecordScore(highestScoreRecord);
        records.addRecordScore(lowestScoreRecord);

        records.update(); // write

        try (
            BufferedReader reader = new BufferedReader(
                    new FileReader(JSONHandler.getFile()));
        ) {
            assertEquals(reader.readLine(), writer.toJson());
        } catch (IOException e) {
            fail("File \"" + JSONHandler.getFile().toString() + "\" missing.");
        }
    }

    /**
     * Records file reading test.
     */
    @org.junit.Test
    public void readingTest() {
        final Logics logics = new LogicsHandler();
        final Player player = new PlayerInstance(logics);
        final Records records = new Records(() -> logics.getGame().getActualGame(), player);
        final JSONWriter writer = new JSONWriterImpl(records);
        final JSONReader reader = new JSONReaderImpl(records);

        final int burnedTimes = 5;
        final int zappedTimes = 2;
        final int highestScoreRecord = 400;
        final int lowestScoreRecord = 250;

        records.clear();

        records.setBurnedTimes(burnedTimes);
        records.setZappedTimes(zappedTimes);

        records.addRecordScore(highestScoreRecord);
        records.addRecordScore(lowestScoreRecord);

        records.update(); // write

        assertEquals(writer.toJson(), reader.toString());

        records.refresh(); // read

        assertEquals(burnedTimes, records.getBurnedTimes());
        assertEquals(zappedTimes, records.getZappedTimes());

        assertEquals(highestScoreRecord, records.getHighestScore());
        assertEquals(highestScoreRecord, records.getRecordScores().get(0).intValue());
        assertEquals(lowestScoreRecord, records.getRecordScores().get(1).intValue());
    }

    /**
     * collisions detection and handling test.
     */
    @org.junit.Test
    public void collisionsTest() {
         final Logics logics = new LogicsHandler();
         Player player = new PlayerInstance(logics);
         this.throwAtPlayer(logics, player, EntityType.ZAPPER);
         assertEquals(player.getCauseOfDeath(), PlayerDeath.ZAPPED);

         player = new PlayerInstance(logics);
         this.throwAtPlayer(logics, player, EntityType.MISSILE);
         assertEquals(player.getCauseOfDeath(), PlayerDeath.BURNED);

         player = new PlayerInstance(logics);
         this.throwAtPlayer(logics, player, EntityType.SHIELD);
         this.throwAtPlayer(logics, player, EntityType.MISSILE);
         assertEquals(player.getCauseOfDeath(), null);

         player = new PlayerInstance(logics);
         this.throwAtPlayer(logics, player, EntityType.TELEPORT);
         assertEquals(player.getCurrentScore(), 250);
    }

    /**
     * collisions detection and handling test.
     */
    @org.junit.Test
    public void menuTest() {
        DisplayPause pause = new DisplayPause();
        KeyHandler keyH = new KeyHandler();
        MenuHandler pauseH = new MenuHandler(keyH, pause,
                x -> System.out.println(x.toString()));

        assertEquals(MenuOption.RESUME, pauseH.getSelectedOption());

        keyH.typeKey(KeyEvent.VK_UP);
        pauseH.update();
        assertEquals(MenuOption.MENU, pauseH.getSelectedOption());

        keyH.typeKey(KeyEvent.VK_UP);
        pauseH.update();
        assertEquals(MenuOption.RESUME, pauseH.getSelectedOption());

        keyH.typeKey(KeyEvent.VK_DOWN);
        pauseH.update();
        keyH.typeKey(KeyEvent.VK_DOWN);
        pauseH.update();
        assertEquals(MenuOption.RESUME, pauseH.getSelectedOption());

    }

    /**
     * sounds playing and volume test.
     */
    @org.junit.Test
    public void soundTest() {
         final Logics logics = new LogicsHandler();
         final SoundManager sound = new SoundManager(MenuOption.SOUND);
         final int start = sound.getVolumeLevel();
         Player player = new PlayerInstance(logics);

         sound.play(Sound.MENU_SELECTION);
         //Max Volume bound
         for(int i = 0; i < 5; i++) {
         sound.raiseVolumeLevel();
         }
         assertEquals(4, sound.getVolumeLevel());

         //Min Volume bound
         for(int i = 0; i < 5; i++) {
         sound.lowerVolumeLevel();
         }
         assertEquals(0, sound.getVolumeLevel());

         //Back to player's settings
         for(int i = 0; i < start; i++) {
         sound.raiseVolumeLevel();
         }
         assertEquals(start, sound.getVolumeLevel());

         //check sound after events
         this.throwAtPlayer(logics, player, EntityType.ZAPPER);
         assertEquals(true, GameWindow.GAME_SOUND.getClipsMap().containsKey(Sound.ZAPPED));

         player = new PlayerInstance(logics);
         this.throwAtPlayer(logics, player, EntityType.MISSILE);
         assertEquals(true, GameWindow.GAME_SOUND.getClipsMap().containsKey(Sound.MISSILE));

         player = new PlayerInstance(logics);
         this.throwAtPlayer(logics, player, EntityType.SHIELD);
         assertEquals(true, GameWindow.GAME_SOUND.getClipsMap().containsKey(Sound.SHIELD_UP));
         this.throwAtPlayer(logics, player, EntityType.MISSILE);
         assertEquals(true, GameWindow.GAME_SOUND.getClipsMap().containsKey(Sound.SHIELD_DOWN));

         player = new PlayerInstance(logics);
         this.throwAtPlayer(logics, player, EntityType.TELEPORT);
         assertEquals(true, GameWindow.GAME_SOUND.getClipsMap().containsKey(Sound.TELEPORT));
    }

    private void throwAtPlayer(Logics logics, Player player, EntityType entityT) {
        Entity entity;
        final Pair<Double, Double> entityPos = new Pair<>(player.getPosition().getX(),
                player.getPosition().getY());
        final SpeedHandler entityMovement = new SpeedHandler(500.0, 10.0, 5000.0);

        switch(entityT) {
        case MISSILE:
            entity = new MissileInstance(logics, entityPos, player, entityMovement); break;
        case TELEPORT:
            entity = new TeleportInstance(logics, entityPos, player, entityMovement); break;
        case SHIELD:
            entity = new ShieldInstance(logics, entityPos,player, entityMovement); break;
        default:
            ZapperBaseInstance base1 = new ZapperBaseInstance(logics, entityPos, entityMovement);
            ZapperBaseInstance base2 = new ZapperBaseInstance(logics, entityPos, entityMovement);
            entity = new ZapperInstance(base1 , base2,
                    Set.of(new ZapperRayInstance(logics, entityPos,base1,base2))); break;
        }

        logics.getEntities().get(entityT).add(entity);
        player.update();
        logics.getEntitiesCleaner().accept(t -> true, e -> true);
    }
    
}
