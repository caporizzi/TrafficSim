package Model


 case class Velocity(var dx: Float, var dy: Float) {
   require(dx >= 0)
   require(dy >= 0)
   def update(newDx: Float): Velocity = this.copy(dx = newDx)
   var vitesseTotal : Float = Math.sqrt(dx * dx + dy * dy).toFloat

   def moveFrom(from: Position): Position = from.move(dx, dy)

   def withDx(newDx: Float): Velocity = copy(dx = newDx)

   def withDy(newDy: Float): Velocity = copy(dy = newDy)
  }


case class reactionDistance( var distanceToReact: Float )  {
  require(distanceToReact >= 0)
  distanceToReact
}

case class reactionTime( var timeToReact : Float)  {
  require(timeToReact < 1.0)
  timeToReact
}


case class Acceleration(var ax: Float, var ay: Float){
  def withAx(newAx: Float): Acceleration = copy(ax = newAx)

  def withAy(newAy: Float): Acceleration = copy(ay = newAy)
}
