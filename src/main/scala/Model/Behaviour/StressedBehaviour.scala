package Model.Behaviour

import Model.{Velocity, reactionTime}

class StressedBehaviour extends PersonalBehaviour {
  override def stressed(speed: Velocity): Velocity = {
    // Example implementation, adjust speed for stressed behaviour

    speed.copy(dx = (speed.dx * 1.01).toFloat, dy = (speed.dy * 1.01).toFloat)
  }
  override def chill(speed: Velocity): Velocity = speed

  override def jamsHater(speed: Velocity, timeToReact: reactionTime): Velocity = speed //edit

  override def jamsLover(speed: Velocity, timeToReact: reactionTime): Velocity = speed // edit
}
