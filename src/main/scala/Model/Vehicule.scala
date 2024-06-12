package Model

import Model.Behaviour.VehicleBehavior
import ch.hevs.gdx2d.lib.GdxGraphics
import com.javaswingcomponents.framework.layout.GridBagHelper.Behaviour

import scala.collection.mutable.ArrayBuffer

case class Vehicule(
                     currentvitesse: Velocity,
                     maxVitesse: Float,
                     position: Position,
                     behaviour: VehicleBehavior,
                     safeDistance: Float
                   )

object Vehicule {

  def apply(currentvitesse: Velocity, position: Position, behaviour: VehicleBehavior, safeDistance: Float): Vehicule = {
    val maxVitesse = currentvitesse.dx * 10
    Vehicule(
      currentvitesse,
      maxVitesse,
      position,
      behaviour,
      safeDistance
    )
  }

  def detectCar(v: Vehicule, cars: ArrayBuffer[Vehicule], threshold: Float, g: GdxGraphics): Boolean = {
    cars.exists(otherCar =>
      otherCar.position.y == v.position.y &&
        otherCar.position.x > v.position.x &&
        (otherCar.position.x - v.position.x) <= threshold
    )
  }

  def updateVelocity(v: Vehicule, deltaTime: Float, cars: ArrayBuffer[Vehicule], threshold: Float, g: GdxGraphics, roadLength: Float): Vehicule = {
    val minDistance = 200f
    val updatingCondition = detectCar(v, cars, threshold, g)
    val updatedSpeed = if (!updatingCondition) {
      val newDx = math.min(v.currentvitesse.dx + 0.01f * deltaTime, v.maxVitesse)
      v.copy(currentvitesse = v.currentvitesse.update(newDx))
    } else {
      findCarInFront(v, cars, roadLength) match {
        case Some(car) =>
          val speedOfCarInFront = car.currentvitesse.dx
          if (distanceToCar(v, car) >= minDistance) {
            accelerate(v)
          } else {
            decelerate(v, speedOfCarInFront)
          }
        case None =>
          accelerate(v)
      }
    }
    updatedSpeed
  }

  def distanceToCar(v: Vehicule, otherCar: Vehicule): Float = {
    distance(v.position, otherCar.position)
  }

  def findCarInFront(v: Vehicule, cars: ArrayBuffer[Vehicule], roadLength: Float): Option[Vehicule] = {
    // Filter out the given vehicle from the list
    val otherCars = cars.filterNot(_ == v)

    // Split cars into two groups: those in front and those behind considering wrap-around
    val inFront = otherCars.filter(_.position.x > v.position.x)
    val behind = otherCars.filter(_.position.x <= v.position.x).map { car =>
      car.copy(position = Position(car.position.x + roadLength, car.position.y))
    }

    // Combine the two lists and find the closest car
    val allPossibleCars = (inFront ++ behind).sortBy(_.position.x)
    allPossibleCars.headOption
  }

  def accelerate(v: Vehicule): Vehicule = {
    if (v.currentvitesse.dx < v.maxVitesse) {
      v.copy(currentvitesse = v.currentvitesse.update(v.currentvitesse.dx + 0.01f))
    } else v
  }

  def decelerate(v: Vehicule, speedOfCarInFront: Float): Vehicule = {
    if (v.currentvitesse.dx > speedOfCarInFront) {
      v.copy(currentvitesse = v.currentvitesse.update(speedOfCarInFront))
    } else v
  }

  def distance(pos1: Position, pos2: Position): Float = {
    math.sqrt(math.pow(pos1.x - pos2.x, 2) + math.pow(pos1.y - pos2.y, 2)).toFloat
  }

  def move(v: Vehicule, deltaTime: Float): Vehicule = {
    v.copy(position = v.position.move(v.currentvitesse.dx * deltaTime, v.currentvitesse.dy * deltaTime))
  }

}
