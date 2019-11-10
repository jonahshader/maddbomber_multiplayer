package jonahshader.game.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import jonahshader.game.MaddBomber
import jonahshader.game.matchsystems.Match
import jonahshader.game.players.AIPlayer
import jonahshader.game.players.Player

enum class PlayState {
    CLIENT_SERVER,
    CLIENT,
    SERVER,
    SINGLEPLAYER
}

class PlayScreen(private val game: MaddBomber, val playState: PlayState) : Screen {
    private var match: Match

    init {
        when (playState) {
            PlayState.CLIENT_SERVER -> {
                match = Match(game, "Maps/Sandstone Larger.tmx", true, true)
            }
            PlayState.CLIENT -> {
                match = Match(game, "Maps/Sandstone Larger.tmx", false, true)
            }
            PlayState.SERVER -> {
                match = Match(game, "Maps/Sandstone Larger.tmx", true, true)
            }
            PlayState.SINGLEPLAYER -> {
                match = Match(game, "Maps/Sandstone Larger.tmx", true, false)
                match.addPlayer(Player(
                        2,
                        2,
                        game.controls.getControlProfile(0),
                        match.gameWorld,
                        game,
                        0,
                        Color(1f, 0.75f, 0.75f, 1f)))

                match.addPlayer(AIPlayer(
                        21,
                        13,
                        game.controls.getControlProfile(1),
                        match.gameWorld,
                        game,
                        1,
                        Color(0.75f, 0.75f, 1f, 1f)))
            }
        }
    }

    override fun show() {}

    override fun render(delta: Float) {
        match.render(delta)
    }

    override fun resize(width: Int, height: Int) {
        match.resize(width, height)
    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        match.dispose()
    }
}
