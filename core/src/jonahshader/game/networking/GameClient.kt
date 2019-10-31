package jonahshader.game.networking

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener

object GameClient {
    private val client = Client()

    fun startClient(ip: String, port: Int) {
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

    fun registerPlayer(regPacket: RegisterNewPlayerPacket) {
        client.sendTCP(regPacket)
    }
}