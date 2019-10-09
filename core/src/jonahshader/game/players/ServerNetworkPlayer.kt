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
            this.upKeyDown = packet.up
            this.downKeyDown = packet.down
            this.leftKeyDown = packet.left
            this.rightKeyDown = packet.right
            this.x = packet.x.toDouble()
            this.y = packet.y.toDouble()
            this.xSpeed = packet.xSpeed.toDouble()
            this.ySpeed = packet.ySpeed.toDouble()
        } else if (packet is UpdateStatsPacket) {
            this.maxSpeedCurrent = packet.maxSpd.toDouble()
            this.acceleration = packet.acceleration.toDouble()
        }
    }
}