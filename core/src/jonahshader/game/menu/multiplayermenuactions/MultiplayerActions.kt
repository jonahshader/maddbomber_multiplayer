package jonahshader.game.menu.multiplayermenuactions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import jonahshader.game.MaddBomber
import jonahshader.game.menu.Menu
import jonahshader.game.menu.MenuAction
import jonahshader.game.menu.mainmenuactions.PlayGameAction
import jonahshader.game.menu.mainmenuactions.SettingsAction

class CreateServerAction(private val game: MaddBomber) : MenuAction {
    override fun executeAction(): Boolean {
        return true
    }
}

class JoinServerAction(private val game: MaddBomber) : MenuAction {
    override fun executeAction(): Boolean {
        return true
    }
}

class PlayMultiplayerAction(private var menu: Menu) : MenuAction {
    override fun executeAction(): Boolean {
        menu.clearMenuItems()
        menu.addMenuItem(JoinServerAction(menu.game), "Join Server")
        menu.addMenuItem(CreateServerAction(menu.game), "Create Server")
        menu.addMenuItem(object : MenuAction {
            override fun executeAction(): Boolean {
                menu.clearMenuItems()
                menu.addMenuItem(PlayGameAction(menu.game), "Play Singleplayer")
                menu.addMenuItem(PlayMultiplayerAction(menu), "Play Multiplayer")
                menu.addMenuItem(SettingsAction(), "Settings")
                return false
            }

        }, "Back")
        return false
    }
}