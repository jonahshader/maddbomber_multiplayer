package jonahshader.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class MainGame : ApplicationAdapter() {
    companion object {
        const val V_WIDTH = 640
        const val V_HEIGHT = 360
        const val TILE_SIZE = 32
    }

    private lateinit var batch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer

    override fun create() {
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
    }

    override fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
    }
}
