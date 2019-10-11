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
        var gettingInput = true
        var port = 0

        while (gettingInput) {
            Gdx.input.getTextInput(object : Input.TextInputListener{
                override fun input(text: String?) {
                    port = text?.toInt() ?: 25565
                    gettingInput = false
                }

                override fun canceled() {

                }

            }, "Enter Port", "25565", "")
        }

        return true
    }
}

class JoinServerAction(private val game: MaddBomber) : MenuAction {
    override fun executeAction(): Boolean {
        var gettingInput = true
        var ip = ""

        while (gettingInput) {
            Gdx.input.getTextInput(object : Input.TextInputListener{
                override fun input(text: String?) {
                    if (text != null) {
                        ip = text
                    }
                    gettingInput = false
                }

                override fun canceled() {

                }
            }, "Enter IP (without port)", "", "")
        }

        gettingInput = true
        var port = 0

        while (gettingInput) {
            Gdx.input.getTextInput(object : Input.TextInputListener{
                override fun input(text: String?) {
                    port = text?.toInt() ?: 25565
                    gettingInput = false
                }

                override fun canceled() {

                }
            }, "Enter Port", "25565", "")
        }

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