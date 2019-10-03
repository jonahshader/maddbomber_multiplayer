package jonahshader.game;

import com.badlogic.gdx.graphics.Color;
import jonahshader.game.GameItems.Bomb;
import jonahshader.game.GameItems.Explosion;
import jonahshader.game.GameItems.Pickups.Pickup;
import jonahshader.game.PathFinder.PathFinder;
import jonahshader.game.PathFinder.PointInt;

import java.util.ArrayList;

import static jonahshader.game.MaddBomber.TILE_SIZE;
import static jonahshader.game.MatchSystems.Match.invertBooleanArray;

public class AIPlayer extends Player {

    public final int TILE_MOVE_THRESHOLD = 2;
    private double bombSpawnTimeout;

    ArrayList<PointInt> currentPath;

    AIState state;

    public enum AIState {
        PURSUING_PLAYER,
        PURSUING_PICKUP,
        AVOIDING_DEATH,
        BLOWING_UP_STUFF
    }

    public AIPlayer(int tileX, int tileY, ControlProfile controlProfile, GameWorld gameWorld, MaddBomber game, int playerId, Color playerColor) {
        super(tileX, tileY, controlProfile, gameWorld, game, playerId, playerColor);
        bombSpawnTimeout = 0;
        currentPath = null;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void kill(Player killer, String cause) {
        System.out.println(state);
        System.out.println(currentPath); //TODO: print out in for loop
        if (currentPath != null)
            for (PointInt pointInt : currentPath) {
                System.out.println(pointInt);
            }
        super.kill(killer, cause);
    }

    @Override
    public void run(float dt) {
        Player target = null;
        double lowestDistance = Double.MAX_VALUE;
        for (Player player : getGameWorld().getPlayers()) {
            if (player != this && player.isSpawned()) {
                double distance = Math.sqrt(Math.pow(player.getX() - getX(), 2) + Math.pow(player.getY() - getY(), 2));
                if (distance < lowestDistance) {
                    lowestDistance = distance;
                    target = player;
                }
            }
        }

        boolean pickupClosest = false;
        state = AIState.PURSUING_PLAYER;
        Pickup closestPickup = null;
        for (Pickup pickup : getGameWorld().getPickups()) {
            double pickupX = pickup.getTileX() * TILE_SIZE + (TILE_SIZE / 2);
            double pickupY = pickup.getTileY() * TILE_SIZE + (TILE_SIZE / 2);
            double distance = Math.sqrt(Math.pow(pickupX - getX(), 2) + Math.pow(pickupY - getY(), 2));
            if (pickup.getType() == Pickup.PickupType.BOMB_COUNT_INCREASE) {
                distance += 1000 * TILE_SIZE; //Discourage getting extra bombs, as they are not as useful to the AI.
                                            //Here we make it appear further away to discourage getting the bomb powerup
            }
            if (distance < lowestDistance) {
                pickupClosest = true;
                state = AIState.PURSUING_PICKUP;
                closestPickup = pickup;
                lowestDistance = distance;
            }
        }

        if (lowestDistance < (getExplosionSize()) * TILE_SIZE && !pickupClosest) {
            if (bombSpawnTimeout <= 0) {
                createBomb();
                bombSpawnTimeout = Explosion.EXPLOSION_TIME + Bomb.FUSE_TIME_MAX;
            }
        }

        if (getBombsDeployed() > 0) {
            state = AIState.AVOIDING_DEATH;
        }


        ArrayList<PointInt> path = null;
        boolean[][] collidables = getGameWorld().getCollidables();
//        invertBooleanArray(collidables);
        for (Explosion explosion : getGameWorld().getExplosions()) {
//            System.out.println("added explosion to array at: " + explosion.getTileX() + ", " + explosion.getTileY());
            collidables[explosion.getTileX()][explosion.getTileY()] = true;
        }

        boolean[][] safeZones = getGameWorld().findSafeZones();

        for (int x = 0; x < getMapTileWidth(); x++) {
            for (int y = 0; y < getMapTileHeight(); y++) {
                safeZones[x][y] = collidables[x][y] || !safeZones[x][y];
            }
        }
        switch (state) {
            case PURSUING_PLAYER:
                if (target != null)
                    path = PathFinder.findPath(new PointInt((int) (getX() / TILE_SIZE), (int) (getY() / TILE_SIZE)), new PointInt((int) (target.getX() / TILE_SIZE), (int) (target.getY() / TILE_SIZE)), safeZones, getMapTileWidth(), getMapTileHeight());
                break;
            case PURSUING_PICKUP:
                if (closestPickup != null)
                    path = PathFinder.findPath(new PointInt((int) (getX() / TILE_SIZE), (int) (getY() / TILE_SIZE)), new PointInt(closestPickup.getTileX(), closestPickup.getTileY()), safeZones, getMapTileWidth(), getMapTileHeight());
                break;
            case AVOIDING_DEATH:
                path = PathFinder.findPath(new PointInt((int) (getX() / TILE_SIZE), (int) (getY() / TILE_SIZE)), getGameWorld().findSafeZones(), collidables, getMapTileWidth(), getMapTileHeight());
                break;
            default:
                break;
        }

        if (path == null && state != AIState.AVOIDING_DEATH) {
            state = AIState.BLOWING_UP_STUFF;
            boolean[][] explodables = getGameWorld().getExplodables();
            boolean[][] spotsToBomb = new boolean[getMapTileWidth()][getMapTileHeight()];
            for (int x = 1; x < getMapTileWidth() - 1; x++) {
                for (int y = 1; y < getMapTileHeight() - 1; y++) {
                    spotsToBomb[x][y] = explodables[x][y] || explodables[x + 1][y] || explodables[x - 1][y] || explodables[x][y + 1] || explodables[x][y - 1];
                }
            }

            boolean[][] safeSpots = getGameWorld().findSafeZones();
            invertBooleanArray(safeSpots);
            for (Explosion explosion : getGameWorld().getExplosions()) {
                safeSpots[explosion.getTileX()][explosion.getTileY()] = false;
            }
            path = PathFinder.findPath(new PointInt((int) (getX() / TILE_SIZE), (int) (getY() / TILE_SIZE)), spotsToBomb, collidables, getMapTileWidth(), getMapTileHeight());
        }

        currentPath = path;

        double targetX = 0;
        double targetY = 0;
        boolean dontMove = false;

        double closestPathPointDistance = 999999;
        int closestPathPointIndex = 0;
        if (path != null) {
            for (int i = 0; i < path.size(); i++) {
                PointInt pointInt = path.get(i);
                double cellX = pointInt.x * TILE_SIZE + TILE_SIZE / 2;
                double cellY = pointInt.y * TILE_SIZE + TILE_SIZE / 2;
                double tempDist = Math.pow(cellX - getX(), 2);
                tempDist += Math.pow(cellY - getY(), 2);
                tempDist = Math.sqrt(tempDist);
                if (tempDist < closestPathPointDistance) {
                    closestPathPointDistance = tempDist;
                    closestPathPointIndex = i;
                }
            }
            if (closestPathPointIndex > 0) {
                targetX = (path.get(closestPathPointIndex - 1).x * TILE_SIZE) + (TILE_SIZE / 2);
                targetY = (path.get(closestPathPointIndex - 1).y * TILE_SIZE) + (TILE_SIZE / 2);
                double tempTargetDist = Math.sqrt(Math.pow(targetX - getX(), 2) + Math.pow(targetY - getY(), 2));
                if (tempTargetDist > (TILE_SIZE + TILE_MOVE_THRESHOLD)) {
                    targetX = (path.get(closestPathPointIndex).x * TILE_SIZE) + (TILE_SIZE / 2);
                    targetY = (path.get(closestPathPointIndex).y * TILE_SIZE) + (TILE_SIZE / 2);
                }
            } else {
                targetX = (path.get(closestPathPointIndex).x * TILE_SIZE) + (TILE_SIZE / 2);
                targetY = (path.get(closestPathPointIndex).y * TILE_SIZE) + (TILE_SIZE / 2);

                double tempTargetDist = Math.sqrt(Math.pow(targetX - getX(), 2) + Math.pow(targetY - getY(), 2));
                if (tempTargetDist < TILE_MOVE_THRESHOLD) {
                    dontMove = true;
                }

                if (state == AIState.BLOWING_UP_STUFF) {
                    if (bombSpawnTimeout <= 0) {
                        createBomb();
                        bombSpawnTimeout = Explosion.EXPLOSION_TIME + Bomb.FUSE_TIME_MAX;
                    }
                }
            }

        } else {
//            if (state == AIState.BLOWING_UP_STUFF) {
//                System.out.println("bad things");
//            }
            dontMove = true;
        }


        //reset all keys beforehand
        setUpKeyDown(false);
        setDownKeyDown(false);
        setLeftKeyDown(false);
        setRightKeyDown(false);

        double xDist = targetX - getX();
        double yDist = targetY - getY();


        if (!dontMove) {
            if (Math.abs(yDist) > TILE_MOVE_THRESHOLD || Math.abs(xDist) < TILE_MOVE_THRESHOLD) {
                if (targetY > getY()) {
                    setUpKeyDown(true);
                    setDownKeyDown(false);
                } else {
                    setUpKeyDown(false);
                    setDownKeyDown(true);
                }
            }
            if (Math.abs(xDist) > TILE_MOVE_THRESHOLD || Math.abs(yDist) < TILE_MOVE_THRESHOLD) {
                if (targetX > getX()) {
                    setRightKeyDown(true);
                    setLeftKeyDown(false);
                } else {
                    setRightKeyDown(false);
                    setLeftKeyDown(true);
                }
            }
        } else {
            setUpKeyDown(false);
            setDownKeyDown(false);
            setLeftKeyDown(false);
            setRightKeyDown(false);
        }

//        System.out.println(state);

        if (bombSpawnTimeout > 0) {
            bombSpawnTimeout -= dt;
        }

        super.run(dt);
    }

    @Override
    protected void resetStats() {
        super.resetStats();
    }
}
