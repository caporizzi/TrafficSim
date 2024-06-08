package Model

import ch.hevs.gdx2d.lib.GdxGraphics

import scala.util.Random

case class Vehicule(
                     currentvitesse: Velocity,
                     maxVitesse: Float,
                     position: Position
                   )

object Vehicule {

  def apply(currentvitesse: Velocity, position: Position): Vehicule = {
    val maxVitesse = currentvitesse.dx * 10
    Vehicule(
      currentvitesse,
      maxVitesse,
      position
    )
  }

  def detectCar(v: Vehicule, cars: Array[Vehicule], threshold: Float, g: GdxGraphics): Boolean = {
    cars.exists(otherCar =>
      otherCar.position.y == v.position.y &&
        otherCar.position.x > v.position.x &&
        (otherCar.position.x - v.position.x) <= threshold
    )
  }

  def detectCarRadius(v: Vehicule, cars: Array[Vehicule], radius: Float, g: GdxGraphics): Boolean = {
    cars.exists(otherCar =>
      otherCar.position.distanceTo(v.position) <= radius && otherCar.position != v.position
    )
  }

  def updateVelocity(v: Vehicule, deltaTime: Float, cars: Array[Vehicule], threshold: Float, minDistance: Float, g: GdxGraphics): Vehicule = {
    val updatingCondition = detectCar(v, cars, threshold, g)
    val updatedSpeed = if (!updatingCondition) {
      val newDx = math.min(v.currentvitesse.dx + 0.01f * deltaTime, v.maxVitesse)
      v.copy(currentvitesse = v.currentvitesse.update(newDx))
    } else {
      findCarInFront(v, cars) match {
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

  def updateVelocityRadius(v: Vehicule, deltaTime: Float, cars: Array[Vehicule], radius: Float, minDistance: Float, g: GdxGraphics): Vehicule = {
    val updatingCondition = detectCarRadius(v, cars, radius, g)
    val updatedSpeed = if (!updatingCondition) {
      val newDx = math.min(v.currentvitesse.dx + 0.01f * deltaTime, v.maxVitesse)
      v.copy(currentvitesse = v.currentvitesse.update(newDx))
    } else {
      findCarInFrontRadius(v, cars, radius) match {
        case Some(car) =>
          val speedOfCarInFront = car.currentvitesse.dx
          if (v.position.distanceTo(car.position) >= minDistance) {
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

  def findCarInFrontRadius(v: Vehicule, cars: Array[Vehicule], radius: Float): Option[Vehicule] = {
    cars.filter(_.position.distanceTo(v.position) <= radius).sortBy(_.position.x).headOption
  }

  def findCarInFront(v: Vehicule, cars: Array[Vehicule]): Option[Vehicule] = {
    cars.filter(_.position.x > v.position.x).sortBy(_.position.x).headOption
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

  def moveOnLemniscate(v: Vehicule, deltaTime: Float, a: Float): Vehicule = {
    val t = v.position.x / a // Calculate parameter 't' based on the x-coordinate of the car
    val newX = a * Math.cos(t).toFloat / (1 + Math.sin(t).toFloat * Math.sin(t).toFloat) // Calculate new x-coordinate on the lemniscate
    val newY = a * Math.sin(t).toFloat * Math.cos(t).toFloat / (1 + Math.sin(t).toFloat * Math.sin(t).toFloat) // Calculate new y-coordinate on the lemniscate
    v.copy(position = Position(newX, newY)) // Update the position of the car
  }

}
