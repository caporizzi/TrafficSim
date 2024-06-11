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

//class SimulationGUI extends PortableApplication(1980, 1080) {
  /*
  var car_width = 10f //
  var car_height = 25f // arabinin uzunlugu
  var safeDistance = 40f // Araclar arasindaki mesafe bundan az olursa trafic baslayacak
  var minDistance = 71f
  var deltaTime = 0.5f // fix bir deger - araclarin ne kadar hizli gidecegi ekranda bu degere gore degisiyor cunku currenVitesse * deltaTime , her deltaTime surede o kadar yol alacak o anlamda

  val car1 = Vehicule(Velocity(1.4f, 0), 20f, Position(1f, 270f)) // Car following car1
  val car2 = Vehicule(Velocity(1.3f, 0), 20f, Position(40f, 270f)) // Car following car5
  val car3 = Vehicule(Velocity(1.5f, 0), 20f, Position(85f, 270f)) // Car following car4
  val car4 = Vehicule(Velocity(2.4f, 0), 20f, Position(120f, 270f)) // Car following car3
  val car5 = Vehicule(Velocity(1.1f, 0), 20f, Position(160f, 270f)) // Lead car, no car in front
  val car6 = Vehicule(Velocity(2.2f, 0), 20f, Position(200f, 270f)) // Lead car, no car in front
  val car7 = Vehicule(Velocity(2.4f, 0), 20f, Position(240f, 270f)) // Lead car, no car in front
  val car8 = Vehicule(Velocity(2.2f, 0), 20f, Position(280f, 270f)) // Lead car, no car in front

  var cars = Array(car1, car2, car3, car4, car5, car6, car7, car8)
  var accelerating = false

  def calculateTrafficDensity(cars: Array[Vehicule], numberRoadParts: Int, g: GdxGraphics): Unit = {
    val partsLength = getWindowWidth / numberRoadParts
    val partsCoordinates: Array[(Int, Int)] = Array.ofDim[(Int, Int)](numberRoadParts)

    for (i <- 0 until numberRoadParts) {
      partsCoordinates(i) = (i * partsLength, (i + 1) * partsLength)
    }

    var totalDensityCount = 0

    for (partIndex <- 0 until numberRoadParts) {
      var densityCount = 0
      for (car <- cars) {
        if (car.position.x >= partsCoordinates(partIndex)._1 && car.position.x <= partsCoordinates(partIndex)._2) {
          densityCount += 1
        }
      }
      totalDensityCount += densityCount
      val densityResult = s"Part ${partIndex + 1} density = $densityCount ${partsCoordinates(partIndex)}"
      g.drawString(50f, 550f + 20f * partIndex, densityResult) // 20f fark ile yazıların üst üste gelmesini engelle
    }

    val totalDensityResult = s"Total density = ${totalDensityCount / numberRoadParts}"
    g.drawString(50f, 550f + 20f * numberRoadParts, totalDensityResult) // Toplam yoğunluk için ek satır
  }

  override def onInit(): Unit = {
    setTitle("Traffic Simulation")
  }

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

  def drawCircleRoad(g: GdxGraphics): Unit = {
    g.drawCircle(250, 250, 150, Color.WHITE)
    g.drawCircle(250, 250, 225, Color.WHITE)
  }

  def drawCars(cars: Array[Vehicule], g: GdxGraphics): Unit = {
    val colors = Array(Color.RED, Color.GREEN, Color.BLUE, Color.PINK, Color.CORAL, Color.BROWN, Color.CYAN, Color.GOLD, Color.FOREST)

    for (i <- cars.indices) {
      val car = cars(i)
      g.drawFilledRectangle(car.position.x, car.position.y, car_width, car_height, 90f, colors(i)) // Assign color based on index
    }

    for (i <- cars.indices) {
      val car = cars(i)
      car.move(deltaTime)
      if (car.position.x > getWindowWidth) {
        car.position.x = -car_width
      }
      //println(s"Car $i - Position: ${car.position.x}, ${car.position.y}, Velocity: ${car.currentvitesse.dx}")
    }

    calculateTrafficDensity(cars, 4, g)
  }

  val velocitySeries: Array[XYSeries] = Array.tabulate(cars.length)(i => new XYSeries(s"Car $i Velocity"))

  val dataset = new XYSeriesCollection()
  velocitySeries.foreach(dataset.addSeries)

  val chart = ChartFactory.createXYLineChart(
    "Car Velocity vs. Time", // Chart title
    "Time", // X-axis label
    "Velocity", // Y-axis label
    dataset, // Dataset
    PlotOrientation.VERTICAL,
    true, // Show legend
    true,
    false
  )

  val chartPanel = new ChartPanel(chart)
  val frame = new JFrame("Car Velocity Graph")

  frame.getContentPane().add(chartPanel)
  frame.pack()
  frame.setVisible(true)
  var timeElapsed = 0

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    drawRoad(g)
    drawCars(cars, g)

    timeElapsed = (timeElapsed + deltaTime).toInt
    for (car <- cars) {
      car.updateVelocity(deltaTime, cars, safeDistance, minDistance, g)
      car.move(deltaTime)
      if (car.position.x > getWindowWidth) {
        car.position.x = -car_width
      }
      //println(s"Car - Position: ${car.position.x}, ${car.position.y}, Velocity: ${car.currentvitesse.dx}")
    }
    var i = 1
    while (i < cars.length) {
      val distance = cars(i).distance(cars(i - 1).position)
      //println(s"Distance between car $i and car ${i - 1}: $distance")
      if (distance > safeDistance) {
        //println(s"Car $i is accelerating")
        cars(i).accelerate()
      } else {
        //println(s"Car $i is decelerating")
        cars(i).currentvitesse = Velocity(2f, 0f)
      }
      i += 1
    }
    for (i <- cars.indices) {
      velocitySeries(i).add(timeElapsed, cars(i).currentvitesse.dx)
    }

    if (accelerating) {
      var allSpeedsEqual = true
      for (i <- 1 until cars.length) {
        if (cars(i).currentvitesse.dx != cars(0).currentvitesse.dx) {
          allSpeedsEqual = false
        }
      }
      if (allSpeedsEqual) {
        accelerating = false
      }
    }
  }

  override def onKeyDown(keycode: Int): Unit = {
    val aKeyCode = 29 // 'a' keycode in libGDX is 29
    if (keycode == aKeyCode) {
      accelerating = true
      println(keycode)
      val sortedCars = cars.sortBy(_.currentvitesse.dx)
      println(sortedCars.mkString("\n"))
      var acceleration = 2.0f
      for (car <- sortedCars) {
        car.currentvitesse.dx = car.currentvitesse.dx + acceleration
      }
    }
  }
}

object SimulationApp extends App {
  new SimulationGUI()
}


/*
class SimulationGUI extends PortableApplication(1980,1080) {

  var car_width = 10f //
  var car_height = 25f // arabinin uzunlugu
  var safeDistance = 40f // Araclar arasindaki mesafe bundan az olursa trafic baslayacak
  var minDistance = 55f
  // fix bir deger - araclarin ne kadar hizli gidecegi ekranda bu degere gore degisiyor cunku currenVitesse * deltaTime , her deltaTime surede o kadar yol alacak o anlamda
  var deltaTime = 0.1f

  /*
  You have to put the cars in lanes, posy = 305 to be in the left lane, posy = 270 to be in the right lane.
   */
  val car1 = Vehicule(Velocity(1.4f, 0), 20f, Position(1f, 270f)) // Car following car1
  val car2 = Vehicule(Velocity(1.3f, 0), 20f, Position(40f, 270f)) // Car following car5
  val car3 = Vehicule(Velocity(1.5f, 0), 20f, Position(85f, 270f)) // Car following car4
  val car4 = Vehicule(Velocity(2.4f, 0), 20f, Position(120f, 270f)) // Car following car3
  val car5 = Vehicule(Velocity(1.1f, 0), 20f, Position(160f, 270f)) // Lead car, no car in front
  val car6 = Vehicule(Velocity(2.2f, 0), 20f, Position(200f, 270f)) // Lead car, no car in front
  val car7 = Vehicule(Velocity(2.4f, 0), 20f, Position(240f, 270f)) // Lead car, no car in front
  val car8 = Vehicule(Velocity(2.2f, 0), 20f, Position(280f, 270f)) // Lead car, no car in front
  //val car9 = Vehicule(Velocity(1f, 0), 20f, Position(320f, 270f)) // Lead car, no car in front
  //val car10 = Vehicule(Velocity(1f, 0), 20f, Position(360f, 270f)) // Lead car, no car in front
  //val car11 = Vehicule(Velocity(1f, 0), 20f, Position(45f, 270f)) // Lead car, no car in front
  //val car12 = Vehicule(Velocity(1f, 0), 20f, Position(45f, 270f))



  var cars = Array(car1, car2, car3, car4, car5, car6, car7, car8)


  def calculateTrafficDensity(cars: Array[Vehicule], numberRoadParts: Int, g: GdxGraphics): Unit = {
    val partsLength = getWindowHeight / numberRoadParts
    val partsCoordinates: Array[(Int, Int)] = Array.ofDim[(Int, Int)](numberRoadParts)

    for (i <- 0 until numberRoadParts) {
      partsCoordinates(i) = (i * partsLength, (i + 1) * partsLength)
    }

    for (partIndex <- 0 until numberRoadParts) {
      var densityCount = 0
      for (car <- cars) {
        if (car.position.x >= partsCoordinates(partIndex)._1 && car.position.x <= partsCoordinates(partIndex)._2) {
          densityCount += 1
        }
      }
      val densityResult = s"Part ${partIndex + 1} density = $densityCount ${partsCoordinates(partIndex)}"
      g.drawString(50f, 550f + 20f * partIndex, densityResult) // 20f fark ile yazıların üst üste gelmesini engelle
    }
  }




  override def onInit(): Unit = {
    setTitle("Traffic Simulation")
  }

  // Draw Road Serit genisligi 325 - 250 = 75 / 2 = 37.5
  // Right lane between 250-287, left lane 287 - 325
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

  def drawCircleRoad(g: GdxGraphics): Unit = {
    g.drawCircle(250, 250, 150, Color.WHITE)
    g.drawCircle(250, 250, 225, Color.WHITE)
  }

  // Draw and Moves the Cars
  def drawCars(cars: Array[Vehicule], g: GdxGraphics): Unit = {

    val colors = Array(Color.RED, Color.GREEN, Color.BLUE, Color.PINK, Color.CORAL, Color.BROWN, Color.CYAN, Color.GOLD, Color.FOREST)

    for (i <- cars.indices) {
      val car = cars(i)
      g.drawFilledRectangle(car.position.x, car.position.y, car_width, car_height, 90f, colors(i)) // Assign color based on index
    }

    for (i <- cars.indices) {
      val car = cars(i)
      car.move(deltaTime)
      if (car.position.x > getWindowWidth) {
        car.position.x = -car_width
      }
      println(s"Car $i - Position: ${car.position.x}, ${car.position.y}, Velocity: ${car.currentvitesse.dx}")
    }


    calculateTrafficDensity(cars,4,g)
  } // End of the drawCars method
  //Graphs

  val velocitySeries: Array[XYSeries] = Array.tabulate(cars.length)(i => new XYSeries(s"Car $i Velocity"))

  // Create the chart dataset
  val dataset = new XYSeriesCollection()
  velocitySeries.foreach(dataset.addSeries)

  // Create the chart
  val chart = ChartFactory.createXYLineChart(
    "Car Velocity vs. Time", // Chart title
    "Time", // X-axis label
    "Velocity", // Y-axis label
    dataset, // Dataset
    PlotOrientation.VERTICAL,
    true, // Show legend
    true,
    false
  )

  // Create GUI components
  val chartPanel = new ChartPanel(chart)
  val frame = new JFrame("Car Velocity Graph")

  frame.getContentPane().add(chartPanel)
  frame.pack()
  frame.setVisible(true)
  var timeElapsed = 0
  override def onGraphicRender(g: GdxGraphics): Unit = {
      g.clear()
      drawRoad(g)
      drawCars(cars, g)

    timeElapsed = (timeElapsed+ deltaTime).toInt
    for (car <- cars) {
      car.updateVelocity(deltaTime, cars, safeDistance,minDistance, g)
      car.move(deltaTime)
      if (car.position.x > getWindowWidth) {
        car.position.x = -car_width
      }
      println(s"Car - Position: ${car.position.x}, ${car.position.y}, Velocity: ${car.currentvitesse.dx}")
    }
    var i = 1 // Initialize index variable outside the loop
    while (i < cars.length) {
      val distance = cars(i).distance(cars(i - 1).position)
      println(s"Distance between car $i and car ${i - 1}: $distance")
      if (distance > safeDistance) {
        println(s"Car $i is accelerating")
        cars(i).accelerate()
      } else {
        println(s"Car $i is decelerating")
        cars(i).currentvitesse = Velocity(2f,0f)
      }
      i += 1 // Increment index variable
    }
    for (i <- cars.indices) {
      // Update velocity series
      velocitySeries(i).add(timeElapsed, cars(i).currentvitesse.dx) // Replace /*xValue*/ with appropriate value
    }
  }// End of the onGraphicsRender method


  override def onKeyDown(keycode: Int): Unit = {
    val aKeyCode = 29 // 'a' keycode in libGDX is 29
    if (keycode == aKeyCode) {
      var velocity_x = 2.0f
      for (car <- cars) {
        car.currentvitesse.dx = velocity_x
        velocity_x += 0.2f
      }
    }
  }


}// End of the class







 */


 */
//}
/*
object SimulationApp extends App {
  var a : SimulationGUI = new SimulationGUI()
}

 */