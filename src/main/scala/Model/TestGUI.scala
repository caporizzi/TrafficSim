

package Model

import Model.Vehicule._
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color


class TestGUI extends PortableApplication(1980, 1020) {

  val carWidth = 20f //
  val carHeight = 25f // arabinin uzunlugu
  val safeDistance = 100f // Araclar arasindaki mesafe bundan az olursa trafic baslayacak
  val traficDensity = 0.0f
  val minDistance = 71f
  val deltaTime = 1f

  var cars_2 = Array(
    Vehicule(Velocity(4f, 0), 10f, Position(40f, 270f)),
    Vehicule(Velocity(6f, 0), 10f, Position(200f, 270f)),
    Vehicule(Velocity(2f, 0), 10f, Position(1000f, 270f)))

  def drawRoad(g: GdxGraphics): Unit = {
    val windowHeight = getWindowHeight
    val windowWidth = getWindowWidth
    val dashLength = getWindowWidth
    val spaceLength = 30f
    val startY = 287f
    var xPos = 0f
    while (xPos < 500f) {
      g.drawLine(xPos, startY, xPos + dashLength, startY, Color.WHITE) // - - -
      xPos += dashLength + spaceLength
    }
  }

  def drawCars(cars: Array[Vehicule], g: GdxGraphics): Unit = {
    val colors = Array(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CORAL, Color.BROWN, Color.CYAN, Color.GOLD)
    cars.zip(colors).foreach { case (car, color) =>
      g.drawFilledRectangle(car.position.x, car.position.y, carWidth, carHeight, 90f, color)
    }
  }

  def updateCars(cars: Array[Vehicule], deltaTime: Float): Array[Vehicule] = {
    cars.map { car =>
      val movedCar = Vehicule.move(car, deltaTime)
      val newPosition = if (movedCar.position.x > getWindowWidth) {
        Position(-carWidth, movedCar.position.y)
      } else if (movedCar.position.x < -carWidth) {
        Position(getWindowWidth, movedCar.position.y)
      } else {
        movedCar.position
      }
      movedCar.copy(position = newPosition)
    }
  }

  def updateDistancesAndAccelerate(cars: Array[Vehicule], safeDistance: Float): Array[Vehicule] = {
    cars.map { car =>
      findCarInFront(car, cars) match {
        case Some(frontCar) =>
          val distance = distanceToCar(car, frontCar)
          if (distance > safeDistance) {
            println(s"Car at ${car.position} is accelerating")
            val acceleratedCar = accelerate(car)
            acceleratedCar.copy(currentvitesse = acceleratedCar.currentvitesse.copy(dx = math.min(acceleratedCar.currentvitesse.dx + 1, acceleratedCar.maxVitesse)))
          } else {
            println(s"Car at ${car.position} is decelerating")
            car.copy(currentvitesse = car.currentvitesse.copy(dx = math.max(car.currentvitesse.dx - 1, 2f)))
          }
        case None =>
          car // No car in front, so no need to adjust speed
      }
    }
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    drawRoad(g)
    cars_2 = updateCars(cars_2, deltaTime)
    cars_2 = updateDistancesAndAccelerate(cars_2, safeDistance)

    drawCars(cars_2, g)

  }

  override def onInit(): Unit = setTitle("TestGui")
}

object TestGUI extends App {

  var test: TestGUI = new TestGUI()
}