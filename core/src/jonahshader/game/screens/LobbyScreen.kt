package jonahshader.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.viewport.StretchViewport
import jonahshader.game.MaddBomber
import jonahshader.game.networking.GameServer

class LobbyScreen(val game: MaddBomber, val playState: PlayState, val ip: String, val port: Int) : Screen, InputProcessor {
    val cam = OrthographicCamera()
    val viewport = StretchViewport(640f, 360f, cam)
    var font = game.assets.manager.get(game.assets.loadingFont, BitmapFont::class.java)

    init {
        game.multiplexer.addProcessor(this)
    }


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
        font.draw(game.batch, "press enter to start game", 320f, 200f)
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

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        when (playState) {
            PlayState.CLIENT_SERVER -> {
                if (keycode == Input.Keys.ENTER) {
                    startGame()
                    return true
                }
            }
            PlayState.SERVER -> {
                if (keycode == Input.Keys.ENTER) {
                    startGame()
                    return true
                }
            }
            else -> return false
        }
        return false
    }

    private fun startGame() {
        game.multiplexer.removeProcessor(this)
        GameServer.startGame()
        game.screen = PlayScreen(game, playState)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

}