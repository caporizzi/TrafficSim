package Model

final case class Velocity(dx: Double, dy: Double) {
  lazy val distance: Double = Math.sqrt((dx * dx) + (dy * dy))

  def moveFrom(from: Position): Position =
    from.move(dx, dy)

}