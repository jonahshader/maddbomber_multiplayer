package jonahshader.game.players

import com.badlogic.gdx.graphics.Color
import jonahshader.game.ControlProfile
import jonahshader.game.GameWorld
import jonahshader.game.MaddBomber
import jonahshader.game.networking.UpdateMovementPacket
import jonahshader.game.networking.UpdateStatsPacket

class ServerNetworkPlayer(tileX: Int, tileY: Int,
                          controlProfile: ControlProfile, gameWorld: GameWorld, game: MaddBomber,
                          playerId: Int, playerColor: Color) : Player(tileX, tileY, controlProfile, gameWorld, game, playerId, playerColor) {
    // server networked player is a dummy that runs on the client and gets updates from the server

    fun applyPacket(packet: Any) {
        if (packet is UpdateMovementPacket) {
            upKeyDown = packet.up
            downKeyDown = packet.down
            leftKeyDown = packet.left
            rightKeyDown = packet.right
            x = packet.x.toDouble()
            y = packet.y.toDouble()
            xSpeed = packet.xSpeed.toDouble()
            ySpeed = packet.ySpeed.toDouble()
        } else if (packet is UpdateStatsPacket) {
            maxSpeedCurrent = packet.maxSpd.toDouble()
            acceleration = packet.acceleration.toDouble()
        }
    }
}