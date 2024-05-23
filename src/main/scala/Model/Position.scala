package Model

final case class Position(var x : Float, var y: Float)  {

  def move(dx: Float, dy : Float) : Position = {
    Position(x + dx, y + dy)
  }

  def distance(position1: Position, position2: Position): Double = {
    Math.sqrt(
      (position1.x - position2.x) * (position1.x - position2.x) +
        (position1.y - position2.y) * (position1.y - position2.y))
  }

}
