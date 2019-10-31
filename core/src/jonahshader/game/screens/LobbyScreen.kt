package jonahshader.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.viewport.StretchViewport
import jonahshader.game.MaddBomber

class LobbyScreen(val game: MaddBomber) : Screen {
    val cam = OrthographicCamera()
    val viewport = StretchViewport(640f, 360f, cam)
    var font = game.assets.manager.get(game.assets.loadingFont, BitmapFont::class.java)


    override fun hide() {

    }

    override fun show() {

    }

    override fun render(delta: Float) {
        // clear background
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.apply()
        game.batch.projectionMatrix = viewport.camera.combined
        game.batch.begin()
        font.draw(game.batch, "hi", 320f, 180f)
        game.batch.end()
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {

    }

}