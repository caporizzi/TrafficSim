package Model
 case class Velocity(var dx: Float, var dy: Float) {
    require(dx >= 0)
    require(dy >= 0)
    var vitesseTotal : Float = Math.sqrt(dx * dx + dy * dy).toFloat
 }

case class Acceleration(var ax: Float, var ay: Float){
  var accelerationTotal = Math.sqrt(ax*ax + ay*ay)
}
