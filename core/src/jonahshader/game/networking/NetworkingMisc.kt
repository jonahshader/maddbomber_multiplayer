package jonahshader.game.networking

// packets

// client sends this to the server when client creates character
class RegisterNewPlayer(val name: String, val r: Float, val g: Float, val b: Float)
class ID(val id: Int)