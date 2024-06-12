package Model

case class Position(var x: Float, var y: Float) {
  def move(dx: Float, dy: Float): Position = Position(x + dx, y + dy)

  def distance(position1: Position, position2: Position): Double = {
    Math.sqrt(
      Math.pow(position1.x - position2.x, 2) +
        Math.pow(position1.y - position2.y, 2)
    )
  }
}
