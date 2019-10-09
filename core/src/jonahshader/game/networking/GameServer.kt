package jonahshader.game.networking

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server

class GameServer(port: Int) {
    private val server = Server()

    init {
        server.start()
        server.bind(port, port)

        server.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
//                if (`object` is String) {
//                    println(`object`)
//                    for (con in server.connections)
//                        con?.sendTCP(`object`)
//                }

                if (`object` is RegisterNewPlayerPacket) {
                    // ask match if there is room for another player,
                    // if there is, send match the RegisterNewPlayer and have it generate a reply ID
                    // send that ID to the new client
                    // send info about this player's starting position to all clients somehow
                }
            }
        })
    }
}