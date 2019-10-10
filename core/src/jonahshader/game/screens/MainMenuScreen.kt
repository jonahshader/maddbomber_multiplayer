package jonahshader.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.viewport.FitViewport
import jonahshader.game.MaddBomber
import jonahshader.game.menu.Menu
import jonahshader.game.menu.mainmenuactions.ExitGameAction
import jonahshader.game.menu.mainmenuactions.PlayGameAction
import jonahshader.game.menu.mainmenuactions.SettingsAction
import jonahshader.game.menu.multiplayermenuactions.PlayMultiplayerAction

class MainMenuScreen(val game: MaddBomber) : Screen {
    private val width = 1280
    private val height = 720

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(width.toFloat(), height.toFloat(), camera)

    private var menu: Menu

    init {
//        val param = FreeTypeFontGenerator.FreeTypeFontParameter()
//        param.size = 148
        val mainFont = game.assets.manager.get(game.assets.loadingFont, BitmapFont::class.java)

        menu = Menu(mainFont, (-width/2f) + 90f, 150f, 90f, game, viewport)
        menu.addMenuItem(PlayGameAction(game), "Play Singleplayer")
        menu.addMenuItem(PlayMultiplayerAction(menu), "Play Multiplayer")
        menu.addMenuItem(SettingsAction(), "Settings")
        menu.addMenuItem(ExitGameAction(), "Exit")
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        menu.run(delta)

        // clear background
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // render
        viewport.apply()
        game.batch.projectionMatrix = viewport.camera.combined
        game.batch.begin()
        menu.draw()
        game.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {

    }
}
