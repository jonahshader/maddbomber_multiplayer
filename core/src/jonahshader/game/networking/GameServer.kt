package jonahshader.game.networking

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server

object GameServer {
    private val server = Server()
    private var gameStarted = false

    fun startServer(port: Int) {
        registerClasses(server.kryo)
        server.start()
        server.bind(port, port)

        server.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
                if (gameStarted) {
                    if (`object` is UpdateMovementPacket ||
                            `object` is KillPlayerPacket ||
                            `object` is UpdateStatsPacket ||
                            `object` is SpawnPickupPacket) {
                        for (c in server.connections) {
                            if (c != connection) {
                                c.sendTCP(`object`)
                            }
                        }
                    } else if (`object` is StartGamePacket) {
                        startGame()
                    } else if (`object` is CreateBombPacket) {
                        // Notify everyone of the bomb placement, even the one that sent it
                        // After the one that sent it receives this, it will spawn the bomb locally
                        for (c in server.connections) {
                            c.sendTCP(`object`)
                        }
                    }
                } else if (`object` is RegisterNewPlayerPacket) {
                    // ask match if there is room for another player,
                    // if there is, send match the RegisterNewPlayer and have it generate a reply ID
                    // send that ID to the new client
                    // send info about this player's starting position to all clients somehow
                }
            }
        })
    }

    // moves server from lobby to playing game
    fun startGame() {
        if (!gameStarted) {
            gameStarted = true
            val msg = StartGamePacket()
            for (connection in server.connections) {
                connection.sendTCP(gameStarted)
                // loop through all players and call spawnPlayer on them
            }
        }
    }

    fun spawnPlayer(packet: SpawnPlayerPacket) {
        for (c in server.connections) {
            c.sendTCP(packet)
        }
    }
}