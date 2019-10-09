package jonahshader.game.players

import com.badlogic.gdx.graphics.Color
import jonahshader.game.ControlProfile
import jonahshader.game.GameWorld
import jonahshader.game.MaddBomber
import jonahshader.game.networking.GameClient

class ClientNetworkPlayer(tileX: Int, tileY: Int,
                          controlProfile: ControlProfile, gameWorld: GameWorld, game: MaddBomber,
                          playerId: Int, playerColor: Color) : Player(tileX, tileY, controlProfile, gameWorld, game, playerId, playerColor) {
    // client network player runs on the client and is controlled by a player
    // actions and updates are sent from this to the server to be distributed
    // to all other clients

    fun updateNetworking(client: GameClient) {
    }
}