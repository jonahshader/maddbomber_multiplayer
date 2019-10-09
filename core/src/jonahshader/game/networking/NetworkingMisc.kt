package jonahshader.game.networking

import jonahshader.game.gameitems.Pickups.Pickup

// packets

// client sends this to the server when client creates character
class RegisterNewPlayerPacket(val name: String, val r: Float, val g: Float, val b: Float)
class ID(val userId: Int)
class UpdateMovementPacket(val userId: Int, val x: Float, val y: Float,
                           val xSpeed: Float, val ySpeed: Float,
                           val leftRight: Float, val upDown: Float)
class CreateBombPacket(val userId: Int, val tileX: Int, val tileY: Int, val explosionSize: Int)
class KillPlayerPacket(val killedId: Int, killerId: Int)
class UpdateStatsPacket(val maxSpd: Float, val acceleration: Float)
class SpawnPickupPacket(val x: Int, val y: Int, val pickup: Pickup.PickupType)
/* maybe add remove pickup packet?? in case of the networking resolution not
being high enough on the player's position to successfully identify the collision
with the pickup. i guess for now this can be left out but it will need
to be added in the future
 */
//TODO: add player disconnected packet