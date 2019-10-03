package jonahshader.game

import com.badlogic.gdx.Input

import java.io.FileOutputStream
import java.io.IOException
import java.util.Properties

class ControlProfile {
    var upKey: Int = 0
    var downKey: Int = 0
    var leftKey: Int = 0
    var rightKey: Int = 0
    var placeKey: Int = 0
    var activateKey: Int = 0

    constructor(upKey: Int, downKey: Int, leftKey: Int, rightKey: Int, placeKey: Int, activateKey: Int) {
        this.upKey = upKey
        this.downKey = downKey
        this.leftKey = leftKey
        this.rightKey = rightKey
        this.placeKey = placeKey
        this.activateKey = activateKey
    }

    constructor(config: Properties) {
        this.upKey = Input.Keys.valueOf(config.getProperty("up"))
        this.downKey = Input.Keys.valueOf(config.getProperty("down"))
        this.leftKey = Input.Keys.valueOf(config.getProperty("left"))
        this.rightKey = Input.Keys.valueOf(config.getProperty("right"))
        this.placeKey = Input.Keys.valueOf(config.getProperty("place"))
        this.activateKey = Input.Keys.valueOf(config.getProperty("activate"))
    }

    constructor() {
        upKey = Input.Keys.W
        downKey = Input.Keys.S
        leftKey = Input.Keys.A
        rightKey = Input.Keys.D
        placeKey = Input.Keys.SPACE
        activateKey = Input.Keys.SHIFT_LEFT
    }

    fun saveControls(config: Properties, filename: String) {
        config.setProperty("up", Input.Keys.toString(upKey))
        config.setProperty("down", Input.Keys.toString(downKey))
        config.setProperty("left", Input.Keys.toString(leftKey))
        config.setProperty("right", Input.Keys.toString(rightKey))
        config.setProperty("place", Input.Keys.toString(placeKey))
        config.setProperty("activate", Input.Keys.toString(activateKey))

        try {
            val out = FileOutputStream(filename)
            config.store(out, "")
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}
