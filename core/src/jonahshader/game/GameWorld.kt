package jonahshader.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.Disposable
import jonahshader.game.GameItems.Bomb
import jonahshader.game.GameItems.Explosion
import jonahshader.game.GameItems.Pickups.Pickup
import jonahshader.game.NonObjects.ExplosionPropagator

import java.awt.*
import java.util.ArrayList

class GameWorld(mapFileName: String) : Disposable {
    //Game map stuff
    val map: TiledMap
    val mapProperties: MapProperties

    //Game objects/items
    val bombs: ArrayList<Bomb> = ArrayList()
    val explosions: ArrayList<Explosion> = ArrayList()
    val pickups: ArrayList<Pickup> = ArrayList()
    val players: ArrayList<Player> = ArrayList()

    /**
     *
     * @return true = collidable, false = free space
     */
    val collidables: Array<BooleanArray>
        get() {
            val walls = map.layers.get("Walls") as TiledMapTileLayer
            val explodable = map.layers.get("Explodable") as TiledMapTileLayer
            val width = map.properties.get("width", Int::class.java)
            val height = map.properties.get("height", Int::class.java)
            val collidables = Array(width) { BooleanArray(height) }

            for (x in 0 until width) {
                for (y in 0 until height) {
                    collidables[x][y] = walls.getCell(x, y) != null || explodable.getCell(x, y) != null
                }
            }

            for (bomb in bombs) {
                collidables[bomb.tileX][bomb.tileY] = true
            }

            return collidables
        }

    val explodables: Array<BooleanArray>
        get() {
            val explodable = map.layers.get("Explodable") as TiledMapTileLayer
            val width = map.properties.get("width", Int::class.java)
            val height = map.properties.get("height", Int::class.java)
            val collidables = Array(width) { BooleanArray(height) }

            for (x in 0 until width) {
                for (y in 0 until height) {
                    collidables[x][y] = explodable.getCell(x, y) != null
                }
            }

            return collidables
        }

    val walkableSpace: ArrayList<Point>
        get() {
            val spawnableArea = collidables
            val spawnableLocations = ArrayList<Point>()

            for (x in spawnableArea.indices) {
                for (y in spawnableArea[0].indices) {
                    if (!spawnableArea[x][y]) {
                        spawnableLocations.add(Point(x, y))
                    }
                }
            }
            return spawnableLocations
        }

    init {

        val mapLoader = TmxMapLoader()
        map = mapLoader.load(mapFileName)
        mapProperties = map.properties
    }

    fun update(deltaTime: Float) {
        for (player in players) {
            player.run(deltaTime)
        }
        for (bomb in bombs) {
            bomb.run(deltaTime)
        }
        for (explosion in explosions) {
            explosion.run(deltaTime)
        }
        for (pickup in pickups) {
            pickup.run(deltaTime)
        }

        explosions.removeIf{ it.isUsed }
        bombs.removeIf{ it.isUsed }
        pickups.removeIf{ it.isUsed }
    }

    fun draw(batch: SpriteBatch) {
        for (bomb in bombs) {
            bomb.draw(batch)
        }

        for (player in players) {
            player.draw(batch)
        }

        for (explosion in explosions) {
            explosion.draw(batch)
        }

        for (pickup in pickups) {
            pickup.draw(batch)
        }
    }

    fun addBomb(bomb: Bomb) {
        bombs.add(bomb)
        for (player in players) {
            player.addBombToIgnore(bomb)
        }
    }

    /**
     *
     * @return true = safe, false = unsafe
     */
    fun findSafeZones(): Array<BooleanArray> {
        val safeZones = collidables //Start off with all of the collidables in the game, with true being a collidable.
        val futureExplosions = ArrayList<Point>() //arraylist to store all future explosion coordinates
        for (bomb in bombs) { //loop through all bombs in the game
            futureExplosions.addAll(ExplosionPropagator.getExplosionPattern(bomb.tileX, bomb.tileY, bomb.explosionSize, this)) //add each bomb's explosion propagation to the arraylist
            safeZones[bomb.tileX][bomb.tileY] = true //also add the bomb coordinate as an unsafe location
        }
        for (point in futureExplosions) { //convert the arraylist of coordinates and add it to the 2d boolean array
            safeZones[point.getX().toInt()][point.getY().toInt()] = true
        }

        for (explosion in explosions) {
            safeZones[explosion.tileX][explosion.tileY] = true
        }

        //Invert the array so that true = safe and false = unsafe. (this makes this method mesh better with the path finder)
        for (i in safeZones.indices) {
            for (j in safeZones[i].indices) {
                safeZones[i][j] = !safeZones[i][j]
            }
        }

        return safeZones
    }

    override fun dispose() {
        map.dispose()
        //TODO: dispose items n stuff
    }
}
