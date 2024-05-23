package Model


import Model.{Acceleration, Position, Velocity}

case class Vehicule(currentvitesse: Velocity,
                    maxVitesse : Double,
                    var position: Position,
                    acceleration:Acceleration,
                    next: Option[Vehicule] = None) {

  override def toString: String = {
    s"Current velocity = ${currentvitesse.vitesseTotal}\n"
  }

  def updateVelocity(acceleration: Acceleration, time: Double): Velocity = ??? // TODO

  def move(): Unit = {
    position = position.move(currentvitesse.dx, currentvitesse.dy)
  }

  def moveNext(): Unit = {
    move()
    next.foreach(_.moveNext())
  }



} // End of the Agent class