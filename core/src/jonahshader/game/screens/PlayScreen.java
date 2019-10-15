package jonahshader.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import jonahshader.game.MaddBomber;
import jonahshader.game.matchsystems.Match;
import jonahshader.game.players.AIPlayer;
import jonahshader.game.players.Player;

public class PlayScreen implements Screen {
    private MaddBomber game;
    private Match match;

    public PlayScreen(MaddBomber game) {
        this.game = game;
        match = new Match(game, "Maps/Sandstone Larger.tmx", true);
        match.addPlayer(new Player(
                2,
                2,
                game.getControls().getControlProfile(0),
                match.gameWorld,
                game,
                0,
                new Color(1f, 0.75f, 0.75f, 1f)));

        match.addPlayer(new AIPlayer(
                21,
                13,
                game.getControls().getControlProfile(1),
                match.gameWorld,
                game,
                1,
                new Color(0.75f, 0.75f, 1f, 1f)));
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        match.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        match.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        match.dispose();
    }

}
