package Model.Behaviour
import Model.{Velocity, reactionTime, reactionDistance}
trait OverallBehaviour {

  def stableBehaviour( )

  def enteringJams( )

  def exitingJams( timeToReact: Float) : Float

  def intoJams()
}
