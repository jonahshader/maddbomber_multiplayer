package jonahshader.game.menu.multiplayermenuactions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import jonahshader.game.MaddBomber
import jonahshader.game.menu.Menu
import jonahshader.game.menu.MenuAction
import jonahshader.game.menu.mainmenuactions.PlayGameAction
import jonahshader.game.menu.mainmenuactions.SettingsAction
import java.text.NumberFormat
import javax.swing.JFormattedTextField
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane

class CreateServerAction(private val game: MaddBomber) : MenuAction {
    override fun executeAction(): Boolean {
        val portString = JOptionPane.showInputDialog(JFrame(), "Enter Port:")

        // create server with port 25565

        return true
    }
}

class JoinServerAction(private val game: MaddBomber) : MenuAction {
    override fun executeAction(): Boolean {
        val ipString = JOptionPane.showInputDialog(JFrame(), "Enter IP (without port):")
        val portString = JOptionPane.showInputDialog(JFrame(), "Enter Port:")

        // join server with address

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