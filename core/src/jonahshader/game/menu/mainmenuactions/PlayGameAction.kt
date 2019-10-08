package jonahshader.game.menu.mainmenuactions

import jonahshader.game.MaddBomber
import jonahshader.game.menu.MenuAction
import jonahshader.game.sccreens.PlayScreen


class PlayGameAction(private val game: MaddBomber) : MenuAction {
    override fun executeAction() {
        game.screen = PlayScreen(game)
    }
}