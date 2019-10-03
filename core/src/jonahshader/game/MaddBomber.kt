package jonahshader.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import jonahshader.game.Screens.PlayScreen

/*
How to make a map in Tiled Editor:
	New Map Settings:
		Orthogonal, Base64 comp., Right Down
 */

class MaddBomber : Game() {
    companion object {
        const val V_WIDTH = 640
        const val V_HEIGHT = 360
        const val TILE_SIZE = 32
    }

    lateinit var batch: SpriteBatch
    lateinit var shapeRenderer: ShapeRenderer
    lateinit var controls: ControlManager
    lateinit var assets: Assets

    override fun create() {
        assets = Assets()
        //Load all assets
        assets.load()

        //Display progress bar
        var progress = 0.0
        while (!assets.manager.update()) {
            if (assets.manager.progress.toDouble() != progress) {
                progress = assets.manager.progress.toDouble()
                println((progress * 100).toString() + "%")
            }
        }

        batch = SpriteBatch()    //Main sprite batch
        shapeRenderer = ShapeRenderer()
        controls = ControlManager()
        setScreen(PlayScreen(this))
    }

    override fun dispose() {
        batch.dispose()
        assets.dispose()
    }

}
