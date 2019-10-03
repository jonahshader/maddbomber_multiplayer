package jonahshader.game

import java.io.FileInputStream
import java.io.IOException
import java.io.Serializable
import java.util.ArrayList
import java.util.Properties

class ControlManager : Serializable {
    private val controlProfiles: ArrayList<ControlProfile> = ArrayList()
    private val properties: ArrayList<Properties> = ArrayList(4)

    //Load control profiles
    init {
        for (i in 0..3) {
            properties.add(Properties())
        }
        try {
            val `in` = FileInputStream("Player1Config")
            properties[0].load(`in`)
            `in`.close()
        } catch (e: IOException) {
            controlProfiles[0].saveControls(properties[0], "Player1Config")
            e.printStackTrace()
        }

        try {
            val `in` = FileInputStream("Player2Config")
            properties[1].load(`in`)
            `in`.close()
        } catch (e: IOException) {
            controlProfiles[1].saveControls(properties[1], "Player2Config")
            e.printStackTrace()
        }

        try {
            val `in` = FileInputStream("Player3Config")
            properties[2].load(`in`)
            `in`.close()
        } catch (e: IOException) {
            controlProfiles[2].saveControls(properties[2], "Player3Config")
            e.printStackTrace()
        }

        try {
            val `in` = FileInputStream("Player4Config")
            properties[3].load(`in`)
            `in`.close()
        } catch (e: IOException) {
            controlProfiles[3].saveControls(properties[3], "Player4Config")
            e.printStackTrace()
        }

        for (i in 0..3) {
            controlProfiles.add(ControlProfile(properties[i]))
        }
    }

    /**
     *
     * @param i index from 0 to 3
     * @return
     */
    fun getControlProfile(i: Int): ControlProfile {
        return controlProfiles[i]
    }
}
