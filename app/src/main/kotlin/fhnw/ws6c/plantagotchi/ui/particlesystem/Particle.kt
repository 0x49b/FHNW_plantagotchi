package fhnw.ws6c.plantagotchi.ui.particlesystem

data class Particle(
    var n: Long = 0,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
    var speed: Float,
    var angle: Int,
)