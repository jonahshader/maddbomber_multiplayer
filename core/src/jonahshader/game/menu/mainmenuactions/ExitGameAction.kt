package jonahshader.game.menu.mainmenuactions

import com.badlogic.gdx.Gdx
import jonahshader.game.menu.MenuAction

class ExitGameAction : MenuAction {
    override fun executeAction() {
        Gdx.app.exit()
    }
}
