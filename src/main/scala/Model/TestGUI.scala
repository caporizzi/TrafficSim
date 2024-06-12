package Model

import Model.Behaviour.{NormalBehaviour, VehicleBehavior, VisiblyImpBehavior}
import Model.Vehicule._
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


class TestGUI extends PortableApplication(3800, 500) {

  val carWidth = 10f
  val carHeight = 15f
  val safeDistance = 100f
  val minDistance = 71f
  val deltaTime = 1f
  val badVisibilityMen = new VisiblyImpBehavior()
  val normalBehaviour = new NormalBehaviour()
  val maxVitesse = 5f
  val r: Float = Random.between(1.5f, 2.5f)
  val vitesseY = 0f
  var accelerating = false

  val behaviorColors: Map[VehicleBehavior, Color] = Map(
    badVisibilityMen -> Color.RED,
    normalBehaviour -> Color.GREEN
  )

  var cars_2: ArrayBuffer[Vehicule] = ArrayBuffer(
    Vehicule(Velocity(r, vitesseY), maxVitesse, Position(40f, 270f), badVisibilityMen, safeDistance),
    Vehicule(Velocity(r, vitesseY), maxVitesse, Position(200f, 270f), normalBehaviour, safeDistance),
    Vehicule(Velocity(r, vitesseY), maxVitesse, Position(500f, 270f), normalBehaviour, safeDistance))

  def drawRoad(g: GdxGraphics): Unit = {
    val dashLength = getWindowWidth
    val spaceLength = 30f
    val startY = 287f
    var xPos = 0f
    while (xPos < 500f) {
      g.drawLine(xPos, startY, xPos + dashLength, startY, Color.WHITE) // - - -
      xPos += dashLength + spaceLength
    }
  }

  def addNewNormalCar(): Unit = {
    if (cars_2.length < 36) {
      val newCarPositionX = if (cars_2.nonEmpty) cars_2.map(_.position.x).max + safeDistance else 0f
      val newCar = Vehicule(Velocity(2f, 0), 5f, Position(newCarPositionX, 270f), normalBehaviour, safeDistance)
      cars_2.append(newCar)
    }
  }

  def addNewVisiblyImpCar(): Unit = {
    if (cars_2.length < 36) {
      val newCarPositionX = if (cars_2.nonEmpty) cars_2.map(_.position.x).max + safeDistance else 0f
      val newCar = Vehicule(Velocity(2f, 0), 5f, Position(newCarPositionX, 270f), badVisibilityMen, safeDistance)
      cars_2.append(newCar)
    }
  }

  def drawCars(cars: ArrayBuffer[Vehicule], g: GdxGraphics): Unit = {
    cars.foreach { car =>
      // Get the color based on the car's behavior
      val color = behaviorColors.getOrElse(car.behaviour, Color.GRAY) // Default to GRAY if behavior is not found
      g.drawFilledRectangle(car.position.x, car.position.y, carWidth, carHeight, 90f, color)
    }
  }

  def updateCars(cars: ArrayBuffer[Vehicule], deltaTime: Float): ArrayBuffer[Vehicule] = {
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

  def updateDistancesAndAccelerate(cars: ArrayBuffer[Vehicule], safeDistance: Float): ArrayBuffer[Vehicule] = {
    cars.map { car =>
      findCarInFront(car, cars, getWindowWidth) match {
        case Some(frontCar) =>
          val distance = distanceToCar(car, frontCar)
          if (distance > safeDistance) {
            println(s"Car ${car.position} ${car.behaviour} is accelerating")
            val acceleratedCar = accelerate(car)
            acceleratedCar.copy(currentvitesse = acceleratedCar.currentvitesse.copy(dx = math.min(acceleratedCar.currentvitesse.dx + 1, acceleratedCar.maxVitesse)))
          } else {
            println(s"Car at ${car.position} is decelerating")
            car.copy(currentvitesse = car.currentvitesse.copy(dx = math.max(car.currentvitesse.dx - 1, 2f)))
          }
        case None =>
          car
      }
    }
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    drawRoad(g)
    cars_2 = updateCars(cars_2, deltaTime)
    cars_2 = updateDistancesAndAccelerate(cars_2, safeDistance)
    val roadLength = getWindowWidth
    val updatedCars = cars_2.map { car =>
      updateVelocity(car, deltaTime, cars_2, 20f, g, roadLength)
    }
    cars_2 = updatedCars
    drawCars(cars_2, g)
    if (accelerating) {
      val allSpeedsEqual = cars_2.forall(_.currentvitesse.dx == cars_2.head.currentvitesse.dx)
      if (allSpeedsEqual) {
        accelerating = false
      }
    }
  }

  override def onKeyDown(keycode: Int): Unit = {
    val aKeyCode = 29
    val bKeyCode = 30
    val cKeyCode = 31
    if (keycode == aKeyCode) {
      accelerating = true
      cars_2.sortBy(_.currentvitesse.dx).foreach { car =>
        car.currentvitesse.dx += 0.2f
      }
    }
    if (keycode == bKeyCode) {
      addNewNormalCar()
    }
    if (keycode == cKeyCode) {
      addNewVisiblyImpCar()
    }
  }

  override def onInit(): Unit = setTitle("TestGui")
}

object TestGUI extends App {
  var test: TestGUI = new TestGUI()
}