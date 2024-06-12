package Model

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

import javax.swing.JFrame
import org.jfree.chart.{ChartFactory, ChartPanel, JFreeChart}
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


// augemente le lang route 10 20 km km basian dusen arac sayisi
class Sim2 extends PortableApplication(2500, 800) {

  object TrafficPhase extends Enumeration {
    val FreeFlow, Synchronized, Jammed = Value
  }
  var currentTrafficPhase: TrafficPhase.Value = TrafficPhase.FreeFlow

  val carWidth = 5f
  val carHeight = 5f
  val safeDistance = 10f
  val minDistance = 5f
  val deltaTime = 2f
  val r: Float = Random.between(15f, 22f)


  val car1 = Vehicule(Velocity(1.4f, 0), 20f, Position(1f, 270f)) // Car following car1
  val car2 = Vehicule(Velocity(1.3f, 0), 20f, Position(40f, 270f)) // Car following car5
  val cars : ArrayBuffer[Vehicule] = new ArrayBuffer[Vehicule]()
  cars.append(car1)
  cars.append(car2)

  var velocityValue: Float = 15f//calculateAverageSpeed(cars).toFloat

  var accelerating = false

  def drawRoad(g: GdxGraphics): Unit = {
    val dashLength = getWindowWidth
    val spaceLength = 30f
    val startY = 287f
    var xPos = 0f
    while (xPos < 500f) {
      g.drawLine(xPos, startY, xPos + dashLength, startY, Color.WHITE)
      g.drawLine(xPos, startY - 40, xPos + dashLength, startY - 40, Color.WHITE)
      xPos += dashLength + spaceLength
    }
  }

/*
  def addNewCar(): Unit = {

    if (cars.length < 325) {
      val newCarPositionX = if (cars.nonEmpty) cars.map(_.position.x).max + 250f else 0f
      val newCar = Vehicule(Velocity(velocityValue, 0), 20f, Position(newCarPositionX, 270f))
      cars.append(newCar)
    }
  }
 */
def addNewCar(): Unit = {
  if (cars.length < 1525) {
    val newCarPositionX = if (cars.nonEmpty) {
      cars.head.position.x - carWidth - safeDistance // Baş aracın önüne güvenli mesafe ekleyerek yeni araç ekler
    } else {
      0f // Eğer araç yoksa başlangıç pozisyonu 0
    }
    val newCar = Vehicule(Velocity(r, 0), 20f, Position(newCarPositionX, 270f))
    cars.prepend(newCar) // cars dizisine başa araç ekler
  }
}


  def drawCars(cars: ArrayBuffer[Vehicule], g: GdxGraphics): Unit = {
    if (cars.nonEmpty) {
      val leadingCar = cars(cars.length - 1)

      cars.foreach { car =>
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
  def calculateTrafficDensity(cars: ArrayBuffer[Vehicule], numberRoadParts: Int, g: GdxGraphics, minDistance: Float): Unit = {
    val roadLength = getWindowWidth // Longueur totale de la route
    val partLength = roadLength / numberRoadParts // Longueur de chaque section
    val partsCoordinates = Array.tabulate(numberRoadParts)(i => (i * partLength, (i + 1) * partLength))

    val densityCounts = partsCoordinates.map { case (start, end) =>
      val count = cars.count(car => car.position.x >= start && car.position.x < end)
      g.drawString(50f, 550f + 20f * (partsCoordinates.indexOf((start, end)) + 1), s"Part ${partsCoordinates.indexOf((start, end)) + 1} density = $count [$start, $end]")
      count
    }

    val totalDensity = densityCounts.sum
    val averageDensityPerPart = totalDensity.toDouble / numberRoadParts
    g.drawString(50f, 550f + 20f * (numberRoadParts + 1),  s"Average vehicle density per piece= $averageDensityPerPart")
  }
  def updateTrafficPhase(): Unit = {
    val totalSpeed = cars.foldLeft(0.0)((acc, car) => acc + car.currentvitesse.dx)
    val avgSpeed = if (cars.nonEmpty) totalSpeed / cars.length else 0
    val density = cars.length.toDouble / getWindowWidth // Assumes the road width as the unit length

    currentTrafficPhase = if (avgSpeed >= 8 * 0.8 && density < 0.1) TrafficPhase.FreeFlow
    else if (avgSpeed >= 8 * 0.5 && density < 0.3) TrafficPhase.Synchronized
    else TrafficPhase.Jammed
  }
  def calculateEntropy(cars: ArrayBuffer[Vehicule]): Double = {
    val speedIntervals = cars.groupBy(car => (car.currentvitesse.dx / 10).toInt) //  Group the speed in multiples of 10
    val probabilities = speedIntervals.map { case (interval, groupedCars) =>
      groupedCars.length.toDouble / cars.length
    }
    probabilities.map(p => -p * math.log(p)).sum
  }

  val phaseSeries = new XYSeries("Traffic Phase")
  val phaseDataset = new XYSeriesCollection(phaseSeries)
  val phaseChart: JFreeChart = ChartFactory.createXYLineChart(
    "Traffic Phase Transition",
    "Nombre de Voiture per 10 m.",
    "Vitesse Moyen",
    phaseDataset,
    PlotOrientation.VERTICAL,
    true, false, false)

  val frame = new JFrame("Traffic Phase Chart")
  frame.getContentPane.add(new ChartPanel(phaseChart))
  frame.setSize(800, 400)
  frame.setVisible(true)

  // Araç sayısını hesapla ve serilere ekle
  def updateChartData(time: Double, carCount: Int, averageSpeed: Double): Unit = {
    phaseSeries.add(cars.length, calculateAverageSpeed(cars))
    phaseChart.fireChartChanged() // Grafiği güncelle
  }

  def calculateAverageSpeed(cars: ArrayBuffer[Vehicule]): Double = {
    var a: Float = 0
    for (i <- 0 until (cars.length)) {
      a += cars(i).currentvitesse.dx
    }

    var result = a / cars.length
    return result
  }


  override def onInit(): Unit = {
    setTitle("Traffic Simulation")
  }


  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    drawRoad(g)
    drawCars(cars, g)
    updateChartData(0.1f,50, calculateAverageSpeed(cars))
    updateTrafficPhase() // Update the traffic phase based on current conditions
    g.drawString(50, 50, s"Entropy is = ${calculateEntropy(cars)}")
    g.drawString(50, 70, s"Current Traffic Phase: $currentTrafficPhase")

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

    for( i <- 0 until  cars.length) {
      println(s" $i .nci arabanin hizi : ${cars(i).currentvitesse.dx}")
    }
  }

  override def onKeyDown(keycode: Int): Unit = {
    val aKeyCode = 29
    val bKeyCode = 30
    val cKeyCode = 31

    if (keycode == aKeyCode) {
      accelerating = true
      cars.sortBy(_.currentvitesse.dx).foreach { car =>
        car.currentvitesse.dx += 1.0f
      }
    }

    if (keycode == bKeyCode) {
      addNewCar()
    }

    if(keycode == cKeyCode) {
      if(cars.length > 3){
        cars(0).decelerateConstant()
        cars(cars.length / 2 ).decelerateConstant()
      }
      cars(cars.length-1).decelerateConstant()
    }
  }

  // Check traficc fluid or collison



} // End of the Class


object SimulationApp2 extends App {
  new Sim2()
}