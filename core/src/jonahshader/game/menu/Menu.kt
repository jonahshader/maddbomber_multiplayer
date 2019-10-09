package jonahshader.game.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import jonahshader.game.MaddBomber

import java.util.ArrayList

import jonahshader.game.interpolate


class Menu(private val font: BitmapFont, private val firstX: Float, private val firstY: Float, private val itemHeight: Float, internal val game: MaddBomber, internal val viewport: Viewport) : InputProcessor {
    private val menuItems: ArrayList<MenuItem>

    internal val batch: SpriteBatch = game.batch
    internal val shapeRenderer: ShapeRenderer = game.shapeRenderer

    private var index = -1

    private var arrowKeysLast: Boolean = false

    init {
        arrowKeysLast = false

        menuItems = ArrayList()
    }

    fun addMenuItem(menuAction: MenuAction, label: String) {
        val newItem = MenuItem(menuAction, label, menuItems.size)
        menuItems.add(newItem)
    }

    fun run(delta: Float) {
        for (menuItem in menuItems) {
            menuItem.run(delta)
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        //        if (index < 0)
        //            index = 0;
        if (keycode == Input.Keys.UP) {
            index--
            arrowKeysLast = true
            game.assets.manager.get(game.assets.menuHover, Sound::class.java).play(.35f)

        } else if (keycode == Input.Keys.DOWN) {
            index++
            arrowKeysLast = true
            game.assets.manager.get(game.assets.menuHover, Sound::class.java).play(.35f)
        } else if (keycode == Input.Keys.ENTER) {
            if (index > -1) {
                menuItems[index].menuAction.executeAction()
                game.assets.manager.get(game.assets.menuClick, Sound::class.java).play(.5f)
            }

        } else {
            return false // keycode unused
        }

        if (arrowKeysLast) {
            if (index < 0) {
                index = menuItems.size - 1
            } else if (index >= menuItems.size) {
                index = 0
            }
        }

        return true // keycode used
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        mouseMoved(screenX, screenY)
        return if (index > -1) {
            game.assets.manager.get(game.assets.menuClick, Sound::class.java).play(.5f)
            menuItems[index].menuAction.executeAction()
            true
        } else {
            false
        }
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        for (menuItem in menuItems) {
            if (menuItem.isMouseOver(viewport.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())))) {
                if (index != menuItem.id)
                    game.assets.manager.get(game.assets.menuHover, Sound::class.java).play(.35f)
                index = menuItem.id
                arrowKeysLast = false
                return true
            }
        }
        if (!arrowKeysLast)
            index = -1
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    fun draw() {
        for (item in menuItems) {
            item.draw()
        }
    }

    private val X_SELECTED_EXTENTION = 24.0
    inner class MenuItem(val menuAction: MenuAction, private val label: String, val id: Int) {
        private val selected = false
        private var x: Float = 0.toFloat()

        init {
            x = firstX
        }

        fun run(delta: Float) {
            //            x = (float) interpolate(delta, x, firstX + (selected ? X_SELECTED_EXTENTION : 0.0));
            x = interpolate(delta * 8.0, x.toDouble(), firstX + if (id == index) X_SELECTED_EXTENTION else 0.0).toFloat()
        }

        fun draw() {
            font.draw(batch, label, x, firstY - id * itemHeight)
        }

        fun isMouseOver(pos: Vector2): Boolean {
            return pos.x >= firstX && pos.y < firstY - (id - 0.25) * itemHeight && pos.y > firstY - (id + 0.75) * itemHeight
        }

    }
}
