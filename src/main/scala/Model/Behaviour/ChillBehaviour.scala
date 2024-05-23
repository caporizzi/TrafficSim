package Model.Behaviour

import Model.{Velocity, reactionTime}

class ChillBehaviour extends VehicleBehaviour{
  override def stressed(speed: Velocity): Velocity = {
    // Example implementation, adjust speed for stressed behaviour
    speed.copy(dx = speed.dx * 1.2, dy = speed.dy * 1.2)
  }
  override def chill(speed: Velocity): Velocity = speed

  override def shortsighted(speed: Velocity): Velocity = speed

  override def polite(speed: Velocity): Velocity = speed

  override def jamsHater(speed: Velocity, timeToReact: reactionTime): Velocity = {
    val reductionFactor = 1.0 + (timeToReact.timeToReact * 0.5)
    speed.copy(dx = speed.dx / reductionFactor, dy = speed.dy / reductionFactor)
  }
  override def jamsLover(speed: Velocity, timeToReact: reactionTime): Velocity = {

    val increaseFactor = 1.0 - (timeToReact.timeToReact * 0.5)
    speed.copy(dx = speed.dx / increaseFactor, dy = speed.dy / increaseFactor)
  }


}
