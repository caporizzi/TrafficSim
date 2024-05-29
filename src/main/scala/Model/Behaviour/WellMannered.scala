package Model.Behaviour

import Model.{Velocity, reactionDistance}

class WellMannered extends Charac {
  override def shortsighted(distanceToReact: Float): Float = distanceToReact*2f

  override def polite(distanceToReact: Float): Float = distanceToReact*0.5f
}
