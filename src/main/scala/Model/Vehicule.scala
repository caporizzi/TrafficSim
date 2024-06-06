package Model

import ch.hevs.gdx2d.lib.GdxGraphics

case class Vehicule(var currentvitesse: Velocity,
                    maxVitesse: Float,
                    var position: Position,
                    var acceleration: Acceleration
                    /*next: Option[Vehicule] = None*/) {

  // Evet burada next'i silmeliyim cunku araclari birbirinden bagimsiz olmali gecebilmeli birbirini
  // Detects whether there is a vehicle in front of the car by checking the distance in front of the car
  def detectCar(cars: Array[Vehicule], threshold: Float, g: GdxGraphics): Boolean = {
    for (otherCar <- cars) {
      if (otherCar.position.y == this.position.y &&
        otherCar.position.x > this.position.x &&
        (otherCar.position.x - this.position.x) <= threshold) {
        g.drawString(200f,50f,"Crash Detected-- !!")
        return true
      }
    }
    false
  }

  /*
  // TODO SIMDI detectCar true olunca serit degistirme secenegi ekle
  // Araç öndeki bir aracı tespit ederse ve serit değiştirebilirse, bu metodu çağırır
  def avoidTraffic(cars: Array[Vehicule], threshold: Float,g: GdxGraphics): Unit = {
    if (detectCar(cars, threshold,g) && canChangeLane(cars)) {
      changeLane()
    }
  }

  // Şerit değiştirme imkanını kontrol eder
  def canChangeLane(cars: Array[Vehicule]): Boolean = {
    !cars.exists(car =>
      Math.abs(car.position.y - this.position.y) < 20 &&  // Yatay pozisyonlar yeterince yakın mı?
        Math.abs(car.position.x - this.position.x) < 50    // Dikey pozisyonlar yeterince yakın mı?
    )
  }

  // Şerit değiştirme işlemi
  def changeLane(): Unit = {
    position.y = if (position.y == 270)
      305
    else 250
  }
   */


  def calculateTimeToReachMaxSpeed(initialSpeed: Float = currentvitesse.vitesseTotal,
                                   maxSpeed: Float = maxVitesse,
                                   acc: Float = acceleration.accelerationTotal.toFloat): Float = {
    if (acc > 0) (maxSpeed - initialSpeed) / acc else 0f  // İvme 0'dan büyükse süreyi hesapla, değilse 0 dön.
  }

  /*
    def updateVelocity(deltaTime: Float): Unit = {
        currentvitesse.dx += acceleration.ax * deltaTime
        currentvitesse.dx = Math.min(currentvitesse.dx, maxVitesse)
    }
   */

    def updateVelocity(deltaTime: Float, cars: Array[Vehicule], threshold: Float, g: GdxGraphics): Unit = {
      val updatingCondition = this.detectCar(cars, threshold, g)
      if (!updatingCondition) {
        currentvitesse.dx += acceleration.ax * deltaTime
        currentvitesse.dx = Math.min(currentvitesse.dx, maxVitesse)
      } else {
        // Decelerate if a car is detected in front within the threshold
        decelerate()
      }
    }

  def move(deltaTime: Float): Unit = {
    position.x += currentvitesse.dx * deltaTime
  }
/*
  def move(): Unit = {
    position = position.move(currentvitesse.dx, currentvitesse.dy)
  }

 */

  def accelerate(): Unit = {
    if (acceleration.ax > 0 && currentvitesse.dx < maxVitesse) {
      currentvitesse.dx += acceleration.ax
    }
  }

  def decelerate(emergency: Boolean = false): Unit = {
    if (emergency) {
      acceleration.ax *= 0.5f
    } else {
      acceleration.ax = Math.max(0, acceleration.ax - 0.1f)
    }
    //updateVelocity()
  }


}