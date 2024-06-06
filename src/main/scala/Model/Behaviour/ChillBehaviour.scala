package Model.Behaviour

import Model.{Velocity, reactionTime}

class ChillBehaviour extends PersonalBehaviour{
  override def stressed(speed: Velocity): Velocity = speed
    // Example implementation, adjust speed for stressed behaviour


  override def chill(speed: Velocity): Velocity = {

    speed.copy(dx = (speed.dx * 0.99).toFloat, dy = (speed.dy * 0.99).toFloat)
  }

  override def jamsHater(speed: Velocity, timeToReact: reactionTime): Velocity = speed
  /*{
    val reductionFactor = 1.0 + (timeToReact.timeToReact * 0.5)
    speed.copy(dx = (speed.dx / reductionFactor).toFloat, dy = (speed.dy / reductionFactor).toFloat)
  }*/
  override def jamsLover(speed: Velocity, timeToReact: reactionTime): Velocity = speed
  /*{

    val increaseFactor = 1.0 - (timeToReact.timeToReact * 0.5)
    speed.copy(dx = (speed.dx / increaseFactor).toFloat, dy = ((speed.dy / increaseFactor).toFloat))
  }*/


}
