package jonahshader.game.menu.mainmenuactions

import jonahshader.game.MaddBomber
import jonahshader.game.menu.MenuAction
import jonahshader.game.screens.PlayScreen


class PlayGameAction(private val game: MaddBomber) : MenuAction {
    override fun executeAction() : Boolean {
        game.screen = PlayScreen(game)
        return true
    }
}
