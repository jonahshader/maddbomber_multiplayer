package jonahshader.game.Screens;

import com.badlogic.gdx.Screen;
import jonahshader.game.MaddBomber;
import jonahshader.game.MatchSystems.Match;

public class PlayScreen implements Screen {
    private MaddBomber game;
    private Match match;

    public PlayScreen(MaddBomber game) {
        this.game = game;
        match = new Match(game, 1, "Maps/Sandstone Larger.tmx");
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