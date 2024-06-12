package Model

import ch.hevs.gdx2d.lib.GdxGraphics

import scala.collection.mutable.ArrayBuffer

case class Vehicule(var currentvitesse: Velocity,
                    var maxVitesse: Float,
                    var position: Position
                    ) {

  var acceleration:Acceleration = Acceleration(0.5f, 0.0f)

  var sameSpeedDuration: Float = 4
  val sameSpeedThreshold: Float = 10
  // Evet burada next'i silmeliyim cunku araclari birbirinden bagimsiz olmali gecebilmeli birbirini
  // Detects whether there is a vehicle in front of the car by checking the distance in front of the car
  def detectCar(cars: ArrayBuffer[Vehicule], threshold: Float, g: GdxGraphics): Boolean = {
    for (otherCar <- cars) {
      var numCarsinTraf : Int = 0
      if (otherCar.position.y == this.position.y &&
        otherCar.position.x > this.position.x &&
        (otherCar.position.x - this.position.x) <= threshold) {
        numCarsinTraf += 1
        //g.drawString(200f,50f,s"${numCarsinTraf} is on the trafic")
        return true
      }
    }
    false
  }

  def updateVelocity(deltaTime: Float, cars: ArrayBuffer[Vehicule], threshold: Float, minDistance: Float, g: GdxGraphics): Unit = {
    val updatingCondition = this.detectCar(cars, threshold, g)

    // Check if there's no obstacle detected
    if (!updatingCondition) {
      currentvitesse.dx += acceleration.ax * deltaTime
      currentvitesse.dx = Math.min(currentvitesse.dx, maxVitesse)
      sameSpeedDuration = 0 // Reset the same speed duration
    } else {
      // If there's an obstacle, find the car in front
      val carInFront = findCarInFront(cars)
      carInFront match {
        case Some(car) =>
          val speedOfCarInFront = car.currentvitesse.dx
          // Check if the distance to the car in front is greater than minDistance
          if ((distanceToCar(car)) >= minDistance) {
            // If so, accelerate
            accelerate()
          } else {
            // Otherwise, decelerate based on the speed of the car in front
            decelerate(speedOfCarInFront)
          }
        case None =>
          // No car in front, maintain current speed or decelerate further if needed
          accelerate() // or set a minimum speed
      }
    }

    // Check if the current velocity is the same as the previous one
    if (cars.forall(_.currentvitesse.dx == currentvitesse.dx)) {
      sameSpeedDuration += deltaTime // Increment the same speed duration
      // If the same speed duration exceeds the threshold, decelerate
      if (sameSpeedDuration >= sameSpeedThreshold) {
        decelerate(currentvitesse.dx*0.8f) // Decelerate by reducing speed by 1 unit
      }
    } else {
      sameSpeedDuration = 0 // Reset the same speed duration if speeds are different
    }
  }


  def distanceToCar(otherCar: Vehicule): Float = {
    // Calculate the distance between this car and the other car
    distance(otherCar.position)
  }

  def findCarInFront(cars: ArrayBuffer[Vehicule]): Option[Vehicule] = {
    val sortedCars = cars.sortBy(_.position.x) // Sort cars by their x-coordinate
    val index = sortedCars.indexWhere(_.position.x > position.x) // Find the index of the first car in front
    if (index != -1) Some(sortedCars(index)) else None
  }


  def move(deltaTime: Float): Unit = {
    position.x += currentvitesse.dx * deltaTime
  }

  def accelerate(): Unit = {
    if (acceleration.ax > 0 && currentvitesse.vitesseTotal < maxVitesse) {
      currentvitesse.dx += acceleration.ax
    }
  }

  def decelerate(speedOfCarInFront: Float): Unit = {
    // Reduce velocity to the speed of the car in front
    if (currentvitesse.dx > speedOfCarInFront) {
      currentvitesse.dx = speedOfCarInFront
    }
  }

  def decelerateConstant(x : Float = 0.5f) : Unit = {
    if(currentvitesse != 0.0){
      currentvitesse.dx -= (currentvitesse.dx * x)
    }
  }

  def distance(other: Position): Float = {
    Math.sqrt(Math.pow(position.x - other.x, 2) + Math.pow(position.y - other.y, 2)).toFloat
  }

}
