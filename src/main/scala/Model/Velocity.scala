package Model

case class Acceleration(ax: Float, ay: Float){
  var accelerationTotal = Math.sqrt(ax*ax + ay*ay)
}

case class Velocity(var dx: Float, var dy: Float) {
  require(dx >= 0)
  require(dy >= 0)

  lazy val vitesseTotal: Float = Math.sqrt(dx * dx + dy * dy).toFloat

  def moveFrom(from: Position): Position =
    from.move(dx, dy)

}

