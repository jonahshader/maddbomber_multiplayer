package jonahshader.game.menu.mainmenuactions

import jonahshader.game.MaddBomber
import jonahshader.game.menu.MenuAction
import jonahshader.game.screens.PlayScreen
import jonahshader.game.screens.PlayState


class PlayGameAction(private val game: MaddBomber) : MenuAction {
    override fun executeAction() : Boolean {
        game.screen = PlayScreen(game, PlayState.SINGLEPLAYER)
        return true
    }
}
