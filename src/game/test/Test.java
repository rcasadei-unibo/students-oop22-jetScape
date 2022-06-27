package game.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import game.frame.GameWindow;
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
import game.logics.entities.player.PlayerInstance;
import game.logics.generator.Generator;
import game.logics.generator.TileGenerator;
import game.logics.handler.Logics;
import game.logics.handler.LogicsHandler;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

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

        assertEquals(player.entityType(), EntityType.PLAYER);
        logics.getEntities().get(player.entityType()).add(player);

        final int updates = 20;
        for (int i = 0; i < updates; i++) {
            player.update();
        }

        assertEquals(player.getCurrentScore(), updates / 2 - 1);
        assertFalse(player.hasDied());
        assertTrue(player.isOnScreenBounds());
        assertFalse(player.isOnClearArea());
        assertFalse(player.isOnSpawnArea());

        player.clean();
        player.update();

        assertEquals(player.getCurrentScore(), 0);
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

        assertEquals(zapper.entityType(), EntityType.ZAPPER);
        logics.getEntities().get(zapper.entityType()).add(zapper);

        assertEquals(zapper.getBothBases(), b);
        assertEquals(zapper.getPaired(b.getX()), b.getY());
        assertEquals(zapper.getPaired(b.getY()), b.getX());
        assertEquals(zapper.getEntitiesSet(), Set.of(b.getX(), b.getY(), r));

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

        assertEquals(missile.entityType(), EntityType.MISSILE);
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

        assertEquals(shield.entityType(), EntityType.SHIELD);
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

        assertEquals(teleport.entityType(), EntityType.TELEPORT);
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
}
