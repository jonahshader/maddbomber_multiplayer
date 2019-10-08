package jonahshader.game.players

import com.badlogic.gdx.graphics.Color
import jonahshader.game.ControlProfile
import jonahshader.game.GameWorld
import jonahshader.game.MaddBomber

class ClientNetworkPlayer(tileX: Int, tileY: Int,
                          controlProfile: ControlProfile, gameWorld: GameWorld, game: MaddBomber,
                          playerId: Int, playerColor: Color) : Player(tileX, tileY, controlProfile, gameWorld, game, playerId, playerColor) {

}