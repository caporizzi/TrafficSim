package Model

case class Acceleration(ax: Double, ay: Double){
  var accelerationTotal = Math.sqrt(ax*ax + ay*ay)
}

case class Velocity(var dx: Double, var dy: Double) {
  require(dx >= 0)
  require(dy >= 0)

  lazy val vitesseTotal: Double = Math.sqrt(dx * dx + dy * dy)

  def moveFrom(from: Position): Position =
    from.move(dx, dy)

}

