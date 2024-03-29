package jonahshader.game.matchsystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import jonahshader.game.gameitems.Pickups.Pickup;
import jonahshader.game.GameWorld;
import jonahshader.game.MaddBomber;
import jonahshader.game.networking.GameClient;
import jonahshader.game.networking.RegisterNewPlayerPacket;
import jonahshader.game.players.Player;
import jonahshader.game.scenes.Hud;
import jonahshader.game.screens.PlayState;

import java.awt.*;
import java.util.ArrayList;

public class Match implements Disposable{
    private final static float TIME_SCALE = 1f;
    //Game systems stuff
    private MaddBomber game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private InputMultiplexer multiplexer;

    // Networking stuff
    private PlayState playState;
    private double timeToStart = 30; // in seconds

    //Map stuff
    public GameWorld gameWorld;
    private OrthogonalTiledMapRenderer mapRenderer;

    public Match(MaddBomber game, String mapFileName, PlayState playState, RegisterNewPlayerPacket playerInfo) {
        this.game = game;
        this.playState = playState;
        gameCam = new OrthographicCamera(MaddBomber.V_WIDTH, MaddBomber.V_HEIGHT);
        hud = new Hud(game);
        gameWorld = new GameWorld(mapFileName);
        gamePort = new FitViewport(gameWorld.getMapProperties().get("width", Integer.class) * MaddBomber.TILE_SIZE, gameWorld.getMapProperties().get("height", Integer.class) * MaddBomber.TILE_SIZE, gameCam);
//        gamePort = new StretchViewport(gameWorld.getMapProperties().get("width", Integer.class) * MaddBomber.TILE_SIZE, gameWorld.getMapProperties().get("height", Integer.class) * MaddBomber.TILE_SIZE, gameCam);
        mapRenderer = new OrthogonalTiledMapRenderer(gameWorld.getMap());
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        GameClient.INSTANCE.setGameWorldAndGame(gameWorld, game);

        if (playState == PlayState.SINGLEPLAYER) {
            timeToStart = 0; // Start immediately if in singleplayer mode
        } else if (playState == PlayState.CLIENT) {
            GameClient.INSTANCE.registerPlayer(playerInfo);
        }

        //Use InputMultiplexer for input handling
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
    }

    //first thing that runs in render method
    private void update(float dt) {
        dt *= TIME_SCALE;



        if (timeToStart > 0) {
            timeToStart -= dt;
        } else {
            gameWorld.update(dt);
            gameCam.update();
            mapRenderer.setView(gameCam);
            hud.updateLables();
            updateScores();
            runItemSpawner(dt, playState);
        }
    }

    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().setProjectionMatrix(gameCam.combined);
        mapRenderer.setView(gameCam);
        mapRenderer.render();


        game.getBatch().begin();
        game.getBatch().setProjectionMatrix(gameCam.combined);
        gameWorld.draw(game.getBatch());
        game.getBatch().end();

        hud.draw();
    }

    public void resize(int width, int height) {
        //viewport needs to be updated
        gamePort.update(width, height);
        gameCam.update();
    }

    public void addPlayer(Player player) {
        multiplexer.addProcessor(player);
        gameWorld.addPlayer(player);
    }

    public void removePlayer(Player player) {
        multiplexer.removeProcessor(player);
        gameWorld.getPlayers().remove(player);
    }

    public static Vector3 getMousePosInGameWorld(Camera cam) {
        return cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    private void updateScores() {
        //TODO: this is broken
        for (int i = 0; i < gameWorld.getPlayers().values().size(); i++) {
            switch (i){
                case 0:
                    hud.setPlayer1Score(((Player)gameWorld.getPlayers().values().toArray()[i]).getScore());
                    break;
                case 1:
                    hud.setPlayer2Score(((Player)gameWorld.getPlayers().values().toArray()[i]).getScore());
                    break;
                case 2:
                    hud.setPlayer3Score(((Player)gameWorld.getPlayers().values().toArray()[i]).getScore());
                    break;
                case 3:
//                    hud.setPlayer4Score(gameWorld.getPlayers().get(i).getScore());
                    hud.setPlayer4Score(((Player)gameWorld.getPlayers().values().toArray()[i]).getScore());
                    break;
                default:
                    break;
            }
        }
    }

    private void runItemSpawner(float dt) {
        if (Math.random() < 0.1 * dt) {
            ArrayList<Point> pickupSpawnCanidates = gameWorld.getWalkableSpace();
            Point selectedLocation = pickupSpawnCanidates.get((int) (Math.random() * pickupSpawnCanidates.size()));
            gameWorld.getPickups().add(new Pickup(selectedLocation.x, selectedLocation.y, game));
        }
    }

    @Override
    public void dispose() {
    }

    public static void invertBooleanArray(boolean[][] input) {
        //Invert the array so that true = safe and false = unsafe. (this makes this method mesh better with the path finder)
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                input[i][j] = !input[i][j];
            }
        }
    }
}
