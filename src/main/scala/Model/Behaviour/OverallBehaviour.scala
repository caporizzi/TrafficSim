package Model.Behaviour
import Model.{Velocity, reactionTime, reactionDistance}
trait OverallBehaviour {

  def stableBehaviour( ) = ???

  def enteringJams( ) = ???

  def exitingJams( timeToReact: reactionTime ) : Float = ???

  def intoJams() = ???
}
