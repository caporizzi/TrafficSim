package Model.Behaviour

import Model.{Velocity, reactionTime}

trait VehicleBehaviour {
  def stressed(speed: Velocity): Velocity

  def chill(speed: Velocity): Velocity

  def shortsighted(speed: Velocity): Velocity

  def polite(speed: Velocity): Velocity

  def jamsHater(speed: Velocity, timeToReact: reactionTime): Velocity

  def jamsLover(speed: Velocity, timeToReact: reactionTime): Velocity


}
