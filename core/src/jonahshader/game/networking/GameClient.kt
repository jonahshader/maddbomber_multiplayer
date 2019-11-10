package jonahshader.game.networking

import com.badlogic.gdx.graphics.Color
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import jonahshader.game.GameWorld
import jonahshader.game.MaddBomber
import jonahshader.game.players.Player

object GameClient {
    private val client = Client()
    private var clientPlayer: Player? = null
    private var gameWorld: GameWorld? = null
    private var game: MaddBomber? = null

    fun setGameWorldAndGame(gameWorld: GameWorld, game: MaddBomber) {
        this.gameWorld = gameWorld
        this.game = game
    }

    fun startClient(ip: String, port: Int) {
        client.start()
        client.connect(5000, ip, port, port)

        client.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
                if (`object` is PlayerRegisterResponse) {
                    // register successful, create player
                    clientPlayer = gameWorld?.let { game?.let { it1 -> Player(`object`.x, `object`.y, null, it, it1, `object`.userId, Color(`object`.r, `object`.g, `object`.b, 1f)) } }
                }
            }
        })
    }

    fun registerPlayer(regPacket: RegisterNewPlayerPacket) {
        client.sendTCP(regPacket)
    }
}