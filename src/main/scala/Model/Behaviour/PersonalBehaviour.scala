package Model.Behaviour

import Model.{Velocity, reactionTime, reactionDistance}

trait PersonalBehaviour {
  def stressed(speed: Velocity): Velocity

  def chill(speed: Velocity): Velocity

  def jamsHater(speed: Velocity, timeToReact: reactionTime): Velocity

  def jamsLover(speed: Velocity, timeToReact: reactionTime): Velocity


}
