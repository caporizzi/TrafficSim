package Model.Behaviour

import Model.{Velocity,reactionTime}

abstract class ChillBehaviour extends Behaviour{
  override def stressed(speed: Velocity): Velocity = {
    // Example implementation, adjust speed for stressed behaviour
    speed.copy(dx = (speed.dx * 1.2).toFloat,((speed.dy * 1.2).toFloat))
  }
  override def chill(speed: Velocity): Velocity = speed

  override def shortsighted(speed: Velocity): Velocity = speed

  override def polite(speed: Velocity): Velocity = speed


  //override def jamsHater(speed: Velocity, timeToReact: Double): Velocity = ???
    //val reductionFactor = 1.0 + (timeToReact * 0.5)
    //speed.copy(dx = (speed.dx / reductionFactor.toFloat), dy = (speed.dy / reductionFactor.toFloat))



  /*
  override def jamsLover(speed: Velocity, timeToReact: reactionTime): Velocity = {

    val increaseFactor = 1.0 - (timeToReact * 0.5)
    speed.copy(dx = (speed.dx / increaseFactor).toFloat, dy = (speed.dy / increaseFactor).toFloat)
  }

   */


}
