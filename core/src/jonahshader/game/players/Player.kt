package jonahshader.game.players

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import jonahshader.game.ControlProfile
import jonahshader.game.GameWorld
import jonahshader.game.MaddBomber
import jonahshader.game.gameitems.Bomb
import jonahshader.game.matchsystems.PlayerSpawner

import java.util.ArrayList
import kotlin.math.atan2


open class Player(tileX: Int, tileY: Int, private val controlProfile: ControlProfile, var gameWorld: GameWorld, protected var game: MaddBomber, private val playerId: Int, private val playerColor: Color) : InputProcessor {
    //Center of player sprite
    //in world pixels
    var x: Double = 0.toDouble()
    var y: Double = 0.toDouble()
    var xSpeed: Double = 0.toDouble()
    var ySpeed: Double = 0.toDouble()
    private var maxSpeedCurrent: Double = 0.toDouble()
    private var acceleration = ACCELERATION_REGULAR
    var bombsDeployed = 0
        private set
    private var maxDeployedBombs: Int = 0
    var explosionSize: Int = 0
    var score = 0
        private set

    private val sprite: Sprite
    private val itemAtlas: TextureAtlas = game.assets.manager.get(game.assets.itemAtlas, TextureAtlas::class.java)
    private val hitbox: Rectangle
    private val map: TiledMap = gameWorld.map
    private val prop: MapProperties
    val spawner: PlayerSpawner
    var mapTileWidth: Int = 0
    var mapTileHeight: Int = 0
    var isSpawned = true
        private set

    private val bombs: ArrayList<Bomb>
    private val bombsIgnored: ArrayList<Bomb> //bombs are put into here until the player moves off of them
    var upKeyDown: Boolean = false
    var downKeyDown: Boolean = false
    var leftKeyDown: Boolean = false
    var rightKeyDown: Boolean = false
    var placeKeyDown: Boolean = false
    var activateKeyDown: Boolean = false

    private val wallColliding: Boolean
        get() {
            val walls = map.layers.get("Walls") as TiledMapTileLayer
            val explodable = map.layers.get("Explodable") as TiledMapTileLayer
            val rectangleMapObject = Rectangle(0f, 0f, MaddBomber.TILE_SIZE.toFloat(), MaddBomber.TILE_SIZE.toFloat())

            for (tx in 0 until mapTileWidth) {
                for (ty in 0 until mapTileHeight) {
                    if (walls.getCell(tx, ty) != null || explodable.getCell(tx, ty) != null) {
                        rectangleMapObject.setX((tx * MaddBomber.TILE_SIZE).toFloat())
                        rectangleMapObject.setY((ty * MaddBomber.TILE_SIZE).toFloat())

                        if (Intersector.overlaps(hitbox, rectangleMapObject))
                            return true
                    }
                }
            }

            for (bomb in gameWorld.bombs) {
                if (!bombsIgnored.contains(bomb)) {
                    if (Intersector.overlaps(hitbox, bomb.hitbox))
                        return true
                }
            }

            return false
        }

    init {
        val tr = TextureRegion(itemAtlas.findRegion("player"))
        spawner = PlayerSpawner(gameWorld, this)
        prop = map.properties
        resetStats()
        bombs = ArrayList()
        bombsIgnored = ArrayList()

        mapTileWidth = prop.get("width", Int::class.java)
        mapTileHeight = prop.get("height", Int::class.java)

        x = (tileX * MaddBomber.TILE_SIZE + MaddBomber.TILE_SIZE / 2).toDouble()
        y = (tileY * MaddBomber.TILE_SIZE + MaddBomber.TILE_SIZE / 2).toDouble()

        hitbox = Rectangle((tileX * MaddBomber.TILE_SIZE).toFloat(), (tileY * MaddBomber.TILE_SIZE).toFloat(), width.toFloat(), height.toFloat())
        sprite = Sprite(tr)
        sprite.setCenterX(x.toFloat())
        sprite.setCenterY(y.toFloat())
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == controlProfile.upKey) {
            upKeyDown = true
            //            return true;
        } else if (keycode == controlProfile.downKey) {
            downKeyDown = true
            //            return true;
        } else if (keycode == controlProfile.leftKey) {
            leftKeyDown = true
            //            return true;
        } else if (keycode == controlProfile.rightKey) {
            rightKeyDown = true
            //            return true;
        } else if (keycode == controlProfile.placeKey) {
            createBomb()
            placeKeyDown = true
            //            return true;
        } else if (keycode == controlProfile.activateKey) {
            activateKeyDown = true
            //            return true;
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == controlProfile.upKey) {
            upKeyDown = false
            //            return true;
        } else if (keycode == controlProfile.downKey) {
            downKeyDown = false
            //            return true;
        } else if (keycode == controlProfile.leftKey) {
            leftKeyDown = false
            //            return true;
        } else if (keycode == controlProfile.rightKey) {
            rightKeyDown = false
            //            return true;
        } else if (keycode == controlProfile.placeKey) {
            placeKeyDown = false
            //            return true;
        } else if (keycode == controlProfile.activateKey) {
            activateKeyDown = false
            //            return true;
        }

        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    open fun kill(killer: Player, cause: String) {
        spawner.requestRespawn()
        if (isSpawned) {
            if (killer === this) {
                killer.givePoints(-1) // you killed yourself... -1 point
            } else {
                killer.givePoints(1)
            }
        }
        resetStats()
        isSpawned = false
        println(cause)
        game.assets.manager.get(game.assets.death, Sound::class.java).play(0.27f, 0.8f + Math.random().toFloat() * 0.4f, ((x / MaddBomber.TILE_SIZE / gameWorld.mapProperties.get("width", Int::class.java) - 0.5f) * 1f).toFloat())
    }

    fun respawn(spawnTileX: Int, spawnTileY: Int) {
        x = (spawnTileX * MaddBomber.TILE_SIZE + MaddBomber.TILE_SIZE / 2).toDouble()
        y = (spawnTileY * MaddBomber.TILE_SIZE + MaddBomber.TILE_SIZE / 2).toDouble()
        isSpawned = true
        run(0f)
    }

    private fun move(dt: Float) {
        val px = x
        val py = y
        var tempXSpd = 0.0
        var tempYSpd = 0.0

        tempXSpd += (if (leftKeyDown) -1 else 0).toDouble()
        tempXSpd += (if (rightKeyDown) 1 else 0).toDouble()
        tempYSpd += (if (upKeyDown) 1 else 0).toDouble()
        tempYSpd += (if (downKeyDown) -1 else 0).toDouble()

        var directionalKeysPressed = 0
        if (leftKeyDown) directionalKeysPressed++
        if (rightKeyDown) directionalKeysPressed++
        if (upKeyDown) directionalKeysPressed++
        if (downKeyDown) directionalKeysPressed++

        if (directionalKeysPressed == 1) {
            val movementKernel = Array(3) { BooleanArray(3) }
            val collidables = gameWorld.collidables

            val tempTileX = (x / MaddBomber.TILE_SIZE).toInt()
            val tempTileY = (y / MaddBomber.TILE_SIZE).toInt()

            for (kernelX in -1..1) {
                for (kernelY in -1..1) {
                    val sampleX = tempTileX + kernelX
                    val sampleY = tempTileY + kernelY
                    if (sampleX in 0 until mapTileWidth && sampleY >= 0 && sampleY < mapTileHeight) {
                        movementKernel[kernelX + 1][kernelY + 1] = collidables[sampleX][sampleY]
                    } else {
                        movementKernel[kernelX + 1][kernelY + 1] = true
                    }
                }
            }

            val xBias = x - (tempTileX * MaddBomber.TILE_SIZE + MaddBomber.TILE_SIZE / 2)
            val yBias = y - (tempTileY * MaddBomber.TILE_SIZE + MaddBomber.TILE_SIZE / 2)

            //            System.out.println("x bias: " + xBias);
            //            System.out.println("y bias: " + yBias);


            if (leftKeyDown) {
                if (movementKernel[0][1]) { //if the spot directly to the left of the player is solid...
                    if (yBias > MOVE_AUTO_CORRECT_THRESHOLD) { //if the player is on the upper half of the tile...
                        if (!movementKernel[0][2]) { //if the block directly to the top left of the player is not solid...
                            tempYSpd = 1.0 //move up (should already be moving left)
                            //                            System.out.println("correction upwards");
                        }
                    } else if (yBias < -MOVE_AUTO_CORRECT_THRESHOLD) { //if the player is on the lower half of the tile...
                        if (!movementKernel[0][0]) { //if the block directly to the bottom left of the player is not solid...
                            tempYSpd = -1.0 //move down (should already be moving left)
                            //                            System.out.println("correction downwards");
                        }
                    }
                }
                if (!movementKernel[0][1]) { //left is clear
                    if (movementKernel[0][2]) { //top left is solid
                        if (yBias > MOVE_AUTO_CORRECT_THRESHOLD) { //leaning towards top
                            tempYSpd = -1.0 //move down
                        }
                    }
                    if (movementKernel[0][0]) { //bottom left is clear
                        if (yBias < -MOVE_AUTO_CORRECT_THRESHOLD) { //leaning torwards bottom
                            tempYSpd = 1.0 //move up
                        }
                    }
                }

            } else if (rightKeyDown) {
                if (movementKernel[2][1]) { //if the spot directly to the right of the player is solid...
                    if (yBias > MOVE_AUTO_CORRECT_THRESHOLD) { //if the player is on the upper half of the tile...
                        if (!movementKernel[2][2]) { //if the block directly to the top right of the player is not solid...
                            tempYSpd = 1.0 //move up (should already be moving left)
                            //                            System.out.println("correction upwards");
                        }
                    } else if (yBias < -MOVE_AUTO_CORRECT_THRESHOLD) { //if the player is on the lower half of the tile...
                        if (!movementKernel[2][0]) { //if the block directly to the bottom right of the player is not solid...
                            tempYSpd = -1.0 //move down (should already be moving left)
                            //                            System.out.println("correction downwards");
                        }
                    }
                }
                if (!movementKernel[2][1]) { //right is clear
                    if (movementKernel[2][2]) { //top right is solid
                        if (yBias > MOVE_AUTO_CORRECT_THRESHOLD) { //leaning towards top
                            tempYSpd = -1.0 //move down
                        }
                    }
                    if (movementKernel[2][0]) { //bottom right is clear
                        if (yBias < -MOVE_AUTO_CORRECT_THRESHOLD) { //leaning torwards bottom
                            tempYSpd = 1.0 //move up
                        }
                    }
                }
            } else if (upKeyDown) {
                if (movementKernel[1][2]) { //if the spot directly to the top of the player is solid...
                    if (xBias > MOVE_AUTO_CORRECT_THRESHOLD) { //if the player is on the right half of the tile...
                        if (!movementKernel[2][2]) { //if the block directly to the top right of the player is not solid...
                            tempXSpd = 1.0 //move right (should already be moving up)
                            //                            System.out.println("correction right");
                        }
                    } else if (xBias < -MOVE_AUTO_CORRECT_THRESHOLD) { //if the player is on the left half of the tile...
                        if (!movementKernel[0][2]) { //if the block directly to the top left of the player is not solid...
                            tempXSpd = -1.0 //move left (should already be moving up)
                            //                            System.out.println("correction left");
                        }
                    }
                }
                if (!movementKernel[1][2]) { //up is clear
                    if (movementKernel[2][2]) { //top right is solid
                        if (xBias > MOVE_AUTO_CORRECT_THRESHOLD) { //leaning towards right
                            tempXSpd = -1.0 //move left
                        }
                    }
                    if (movementKernel[0][2]) { //top left is clear
                        if (xBias < -MOVE_AUTO_CORRECT_THRESHOLD) { //leaning towards right
                            tempXSpd = 1.0 //move right
                        }
                    }
                }
            } else if (downKeyDown) {
                if (movementKernel[1][0]) { //if the spot directly to the bottom of the player is solid...
                    if (xBias > MOVE_AUTO_CORRECT_THRESHOLD) { //if the player is on the right half of the tile...
                        if (!movementKernel[2][0]) { //if the block directly to the bottom right of the player is not solid...
                            tempXSpd = 1.0 //move right (should already be moving up)
                            //                            System.out.println("correction right");
                        }
                    } else if (xBias < -MOVE_AUTO_CORRECT_THRESHOLD) { //if the player is on the left half of the tile...
                        if (!movementKernel[0][0]) { //if the block directly to the bottom left of the player is not solid...
                            tempXSpd = -1.0 //move left (should already be moving up)
                            //                            System.out.println("correction left");
                        }
                    }
                }
                if (!movementKernel[1][0]) { //down is clear
                    if (movementKernel[2][0]) { //bottom right is solid
                        if (xBias > MOVE_AUTO_CORRECT_THRESHOLD) { //leaning towards right
                            tempXSpd = -1.0 //move left
                        }
                    }
                    if (movementKernel[0][0]) { //bottom left is clear
                        if (xBias < -MOVE_AUTO_CORRECT_THRESHOLD) { //leaning towards right
                            tempXSpd = 1.0 //move right
                        }
                    }
                }
            }
        }

        //add temp speed from keyboard input to the player's actual speed
        xSpeed += tempXSpd * acceleration * dt.toDouble() * 2.0
        ySpeed += tempYSpd * acceleration * dt.toDouble() * 2.0

        //apply friction to x
        if (xSpeed > 0) {
            if (xSpeed - dt * acceleration > 0) {
                xSpeed -= dt * acceleration
            } else {
                xSpeed = 0.0
            }
        } else if (xSpeed < 0) {
            if (xSpeed + dt * acceleration < 0) {
                xSpeed += dt * acceleration
            } else {
                xSpeed = 0.0
            }
        }

        //apply friction to y
        if (ySpeed > 0) {
            if (ySpeed - dt * acceleration > 0) {
                ySpeed -= dt * acceleration
            } else {
                ySpeed = 0.0
            }
        } else if (ySpeed < 0) {
            if (ySpeed + dt * acceleration < 0) {
                ySpeed += dt * acceleration
            } else {
                ySpeed = 0.0
            }
        }

        //clamp values to max speed
        xSpeed = limit(xSpeed, -maxSpeedCurrent, maxSpeedCurrent)
        ySpeed = limit(ySpeed, -maxSpeedCurrent, maxSpeedCurrent)

        x += xSpeed * MaddBomber.TILE_SIZE.toDouble() * dt.toDouble()
        y += ySpeed * MaddBomber.TILE_SIZE.toDouble() * dt.toDouble()

        if (xSpeed != 0.0 || ySpeed != 0.0) {
            sprite.rotation = (180.0 * atan2(ySpeed, xSpeed) / Math.PI).toFloat() - 90
        }

        hitbox.setX((x - width / 2).toFloat())
        hitbox.setY((y - height / 2).toFloat())

        if (wallColliding) {
            val newX = x
            val newY = y
            x = px
            hitbox.setX((x - width / 2).toFloat())
            if (wallColliding) {
                x = newX
                y = py
                hitbox.setX((x - width / 2).toFloat())
                hitbox.setY((y - height / 2).toFloat())
                if (wallColliding) {
                    x = px
                    y = py
                    hitbox.setX((x - width / 2).toFloat())
                    hitbox.setY((y - height / 2).toFloat())
                    xSpeed = 0.0
                    ySpeed = 0.0
                } else {
                    ySpeed = 0.0
                }
            } else {
                xSpeed = 0.0
            }
        }

        sprite.setCenterX(x.toFloat())
        sprite.setCenterY(y.toFloat())
    }

    open fun run(dt: Float) {
        if (isSpawned) {
            move(dt)
            checkBombCollision()
            checkExplosionCollision()
            checkPickupCollision()
        } else { //if not spawned...
            xSpeed = 0.0
            ySpeed = 0.0
            spawner.run(dt)
        }

        for (bomb in bombs) {
            if (bomb.isUsed)
                bombsDeployed--
        }

        bombs.removeIf{ it.isUsed }
        bombsIgnored.removeIf{ it.isUsed }
    }

    fun draw(batch: SpriteBatch) {
        if (isSpawned) {
            sprite.color = playerColor
            sprite.draw(batch)
        }
    }

    private fun checkBombCollision() {
        for (i in bombsIgnored.indices.reversed()) {
            if (!Intersector.overlaps(hitbox, bombsIgnored[i].hitbox)) {
                bombsIgnored.removeAt(i)
            }
        }
    }

    private fun checkExplosionCollision() {
        for (i in 0 until gameWorld.explosions.size) {
            if (hitbox.overlaps(gameWorld.explosions[i].hitbox)) {
                kill(gameWorld.explosions[i].owner, "Killed from explosion")
            }
        }
    }

    private fun checkPickupCollision() {
        for (i in 0 until gameWorld.pickups.size) {
            if (hitbox.overlaps(gameWorld.pickups[i].hitbox)) {
                gameWorld.pickups[i].use(this)
            }
        }
    }

    protected open fun resetStats() {
        maxSpeedCurrent = INITIAL_SPEED
        acceleration = ACCELERATION_REGULAR
        maxDeployedBombs = 1
        explosionSize = 1
    }

    fun addBombToIgnore(bomb: Bomb) {
        bombsIgnored.add(bomb)
    }

    protected fun createBomb() {
        if (bombsDeployed < maxDeployedBombs && isSpawned) {
            val tileX = (x / MaddBomber.TILE_SIZE).toInt()
            val tileY = (y / MaddBomber.TILE_SIZE).toInt()

            //Check if there is already a bomb on this tile
            var bombExists = false
            for (bomb in gameWorld.bombs) {
                if (bomb.tileX == tileX && bomb.tileY == tileY) {
                    bombExists = true
                    break
                }
            }
            //If not, place a bomb
            if (!bombExists) {
                val newBomb = Bomb(tileX, tileY, itemAtlas, explosionSize, false, this, gameWorld, game)
                bombs.add(newBomb)
                gameWorld.addBomb(newBomb)
                bombsDeployed++
            }
        }
    }

    fun increaseSpeedByFactor(v: Double) {
        maxSpeedCurrent += v
        if (maxSpeedCurrent > MAX_SPEED) {
            maxSpeedCurrent = MAX_SPEED
        }
        acceleration += v
    }

    fun increaseMaxBombs(increment: Int) {
        maxDeployedBombs += increment
    }

    fun increaseExplosionRadius(increment: Int) {
        explosionSize += increment
    }

    fun givePoints(pointsToAdd: Int) {
        score += pointsToAdd
    }

    companion object {

        internal val ACCELERATION_REGULAR = 10.0
        internal val INITIAL_SPEED = 1.3
        internal val MAX_SPEED = 10.0
        private val width = 24.0 //26
        private val height = 24.0
        private val MOVE_AUTO_CORRECT_THRESHOLD = (MaddBomber.TILE_SIZE - width) / 2

        fun limit(value: Double, lower: Double, higher: Double): Double {
            var value = value
            if (value > higher) {
                value = higher
            } else if (value < lower) {
                value = lower
            }
            return value
        }
    }
}
