package Model

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

import javax.swing.JFrame
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection

import scala.collection.mutable.ArrayBuffer

class Sim2 extends PortableApplication(1980, 1080) {

  val carWidth = 10f
  val carHeight = 25f
  val safeDistance = 40f
  val minDistance = 70f
  val deltaTime = 0.1f

  val cars : ArrayBuffer[Vehicule] = new ArrayBuffer[Vehicule]()
  var accelerating = false

  def calculateTrafficDensity(cars: ArrayBuffer[Vehicule], numberRoadParts: Int, g: GdxGraphics, minDistance: Float): Unit = {
    val partsLength = getWindowWidth / numberRoadParts
    val partsCoordinates = Array.tabulate(numberRoadParts)(i => (i * partsLength, (i + 1) * partsLength))

    val totalDensityCount = (0 until numberRoadParts).map { partIndex =>
      val densityCount = cars.count { car =>
        car.position.x >= partsCoordinates(partIndex)._1 && car.position.x <= partsCoordinates(partIndex)._2 && {
          val carIndex = cars.indexOf(car)
          if (carIndex < cars.length - 1) Math.abs(cars(carIndex + 1).position.x - car.position.x) <= minDistance else true
        }
      }
      g.drawString(50f, 550f + 20f * partIndex, s"Part ${partIndex + 1} density = $densityCount ${partsCoordinates(partIndex)}")
      densityCount
    }.sum

    g.drawString(50f, 550f + 20f * numberRoadParts, s"Number of vehiclues = ${totalDensityCount / numberRoadParts}")
  }


  val maxNumberofVehicule: Int = (getWindowWidth / (carWidth + safeDistance)).toInt
  def addNewCar(): Unit = {
    if(cars.length < 36) {
      val newCarPositionX = if (cars.nonEmpty) cars.map(_.position.x).max + 40f else 0f
      val newCar = Vehicule(Velocity(2f, 0), 20f, Position(newCarPositionX, 270f))
      cars.append(newCar)
    }
  }

  override def onInit(): Unit = {
    setTitle("Traffic Simulation")
  }

  def drawRoad(g: GdxGraphics): Unit = {
    val dashLength = getWindowWidth
    val spaceLength = 30f
    val startY = 287f
    var xPos = 0f
    while (xPos < 500f) {
      g.drawLine(xPos, startY, xPos + dashLength, startY, Color.WHITE)
      g.drawLine(xPos, startY-40, xPos + dashLength, startY-40, Color.WHITE)
      xPos += dashLength + spaceLength
    }
  }


  def drawCars(cars: ArrayBuffer[Vehicule], g: GdxGraphics): Unit = {
    if (cars.nonEmpty) {
      // En öndeki aracı bulun
      val leadingCar = cars(cars.length-1)

      cars.foreach { car =>
        // Eğer araç en öndeki araç ise kırmızı, değilse beyaz olsun
        val color = if (car == leadingCar) Color.RED else Color.WHITE
        g.drawFilledRectangle(car.position.x, car.position.y, carWidth, carHeight, 90f, color)
        car.move(deltaTime)
        if (car.position.x > getWindowWidth) {
          car.position.x = -carWidth
        }
      }

      calculateTrafficDensity(cars, 4, g, minDistance)
    }
  }


  var timeElapsed = 0

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    drawRoad(g)
    drawCars(cars, g)
    checkCollision(cars,g)

    timeElapsed = (timeElapsed + deltaTime).toInt
    cars.foreach { car =>
      car.updateVelocity(deltaTime, cars, safeDistance, minDistance, g)
      car.move(deltaTime)
      if (car.position.x > getWindowWidth) {
        car.position.x = -carWidth
      }
    }

    if (accelerating) {
      val allSpeedsEqual = cars.forall(_.currentvitesse.dx == cars.head.currentvitesse.dx)
      if (allSpeedsEqual) {
        accelerating = false
      }
    }
  }

  override def onKeyDown(keycode: Int): Unit = {
    val aKeyCode = 29
    val bKeyCode = 30

    if (keycode == aKeyCode) {
      accelerating = true
      cars.sortBy(_.currentvitesse.dx).foreach { car =>
        car.currentvitesse.dx += 4.0f
      }
    }

    if (keycode == bKeyCode) {
      addNewCar()
    }
  }

  for( i <- cars) {
    println(i.currentvitesse)
  }

  // Check traficc fluid or collison
  def checkCollision(cars: ArrayBuffer[Vehicule], g: GdxGraphics): Unit = {
    var collision = false
    for (car <- cars) {
      if (car.currentvitesse.dx < (car.maxVitesse * 0.8).toInt) {
        collision = true
      }
    }
    if (collision) {
      g.drawString(150f, 150, "Il y a de collision")
    } else {
      g.drawString(150f, 150, "Il n'y a pas de collision, traffic fluid")
    }
  }



}

object SimulationApp2 extends App {
  new Sim2()
}
