package Model

final case class Position(x : Double, y: Double)  {

  def move(dx: Double, dy : Double) : Position = {
    Position(x + dx, y + dy)
  }

  def distance(position1: Position, position2: Position): Double = {
    Math.sqrt(
      (position1.x - position2.x) * (position1.x - position2.x) +
        (position1.y - position2.y) * (position1.y - position2.y))
  }

}