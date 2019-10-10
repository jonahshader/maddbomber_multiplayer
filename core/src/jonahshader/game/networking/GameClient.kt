package jonahshader.game.networking

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import jonahshader.game.players.Player

class GameClient(ip: String, port: Int) {
    private val client = Client()
    var registeringPlayer: Player? = null

    init {
        client.start()
        client.connect(5000, ip, port, port)

        client.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
                if (`object` is ID) {
                    // register successful
                }
            }
        })
    }

    fun registerPlayer(regPacket: RegisterNewPlayerPacket) : Boolean {
        if (registeringPlayer == null) {
            //TODO figure this out. should gameclient handle multiple local players? no, nevermind, thats too complicated for this program
            // would require more than 1 ID per player. one ID for the individual player, and one ID for the client it is a member of
            client.sendTCP(regPacket)
        }
    }
}