package Model.Behaviour

import Model.{Velocity, reactionTime}

class StressedBehaviour extends PersonalBehaviour {
  override def stressed(speed: Velocity): Velocity = {
    // Example implementation, adjust speed for stressed behaviour
    speed.copy((speed.vitesseTotal * 1.2).toFloat)
  }
  override def chill(speed: Velocity): Velocity = speed

  override def jamsHater(speed: Velocity, timeToReact: reactionTime): Velocity = speed //edit

  override def jamsLover(speed: Velocity, timeToReact: reactionTime): Velocity = speed // edit
}
