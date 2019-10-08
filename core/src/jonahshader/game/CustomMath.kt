package jonahshader.game

fun interpolate(fade: Double, a: Double, b: Double) : Double = (a * (1.0 - fade)) + (b * fade)