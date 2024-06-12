package Model
 case class Velocity(var dx: Float, var dy: Float) {
    require(dx >= 0)
    require(dy >= 0)

    var vitesseTotal : Float = Math.sqrt(dx * dx + dy * dy).toFloat

    def moveFrom(from: Position): Position = from.move(dx, dy)
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
  var accelerationTotal = Math.sqrt(ax*ax + ay*ay)
}
