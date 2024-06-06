package Model.Behaviour

import Model.Velocity
import Model.reactionDistance
import Model.reactionTime
trait Charac {
  def shortsighted(distanceToSee: Float): Float
  def polite(timeToReact: Float): Float
}
