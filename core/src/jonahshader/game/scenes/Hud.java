package jonahshader.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import jonahshader.game.MaddBomber;

public class Hud {
    private MaddBomber game;
    private Camera cam;
    private Stage stage;
    private Viewport viewport;
    private BitmapFont font;

    private Integer player1Score;
    private Integer player2Score;
    private Integer player3Score;
    private Integer player4Score;

    private Label player1label;
    private Label player2label;
    private Label player3label;
    private Label player4label;

    public Hud(final MaddBomber game) {
        this.game = game;
        font = game.getAssets().getManager().get(game.getAssets().getLoadingFont(), BitmapFont.class);
        font.getData().scaleX = 0.4f;
        font.getData().scaleY = 0.4f;

        player1Score = 0;
        player2Score = 0;
        player3Score = 0;
        player4Score = 0;

        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
        stage = new Stage(viewport);

        Table table = new Table();
        table.top(); //this sets vertical allignment to top
        table.setFillParent(true); //table becomes the size of the parent (stage)

        player1label = new Label("Player 1 Score", new Label.LabelStyle(font, Color.WHITE));
        player2label = new Label("Player 2 Score", new Label.LabelStyle(font, Color.WHITE));
        player3label = new Label("Player 3 Score", new Label.LabelStyle(font, Color.WHITE));
        player4label = new Label("Player 4 Score", new Label.LabelStyle(font, Color.WHITE));

        table.add(player1label).expandX().padTop(2);
        table.add(player2label).expandX().padTop(2);
        table.add(player3label).expandX().padTop(2);
        table.add(player4label).expandX().padTop(2);
        table.row();

        stage.addActor(table);
    }

    public void updateLables() {
        player1label.setText("Player 1 Score: " + player1Score + " ");
        player2label.setText("Player 2 Score: " + player2Score + " ");
        player3label.setText("Player 3 Score: " + player3Score + " ");
        player4label.setText("Player 4 Score: " + player4Score + " ");
    }

    public void draw() {
        stage.getCamera().position.x -= 2;
        stage.getCamera().position.y -= 1;

        player1label.setColor(0, 0, 0, 1);
        player2label.setColor(0, 0, 0, 1);
        player3label.setColor(0, 0, 0, 1);
        player4label.setColor(0, 0, 0, 1);
        stage.draw();

        stage.getCamera().position.x += 2;
        stage.getCamera().position.y += 1;

        player1label.setColor(1, 1, 1, 1);
        player2label.setColor(1, 1, 1, 1);
        player3label.setColor(1, 1, 1, 1);
        player4label.setColor(1, 1, 1, 1);
        stage.draw();
    }

    public void setPlayer1Score(Integer player1Score) {
        this.player1Score = player1Score;
    }

    public void setPlayer2Score(Integer player2Score) {
        this.player2Score = player2Score;
    }

    public void setPlayer3Score(Integer player3Score) {
        this.player3Score = player3Score;
    }

    public void setPlayer4Score(Integer player4Score) {
        this.player4Score = player4Score;
    }

}
