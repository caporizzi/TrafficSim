package Model.Behaviour

import Model.{Velocity, reactionTime}

class StressedBehaviour extends Behaviour {
  override def stressed(speed: Velocity): Velocity = {
    // Example implementation, adjust speed for stressed behaviour
    speed.copy(speed.vitesseTotal * 1.2)
  }

  override def chill(speed: Velocity): Velocity = speed

  override def shortsighted(speed: Velocity): Velocity = speed

  override def polite(speed: Velocity): Velocity = speed

  override def jamsHater(speed: Velocity, timeToReact: reactionTime): Velocity = speed //edit

  override def jamsLover(speed: Velocity, timeToReact: reactionTime): Velocity = speed // edit
}
