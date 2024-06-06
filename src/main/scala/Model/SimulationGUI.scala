package Model
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color


class SimulationGUI extends PortableApplication {

  var car_width = 15f //
  var car_height = 25f // arabinin uzunlugu
  var safeDistance = 30f // Araclar arasindaki mesafe bundan az olursa trafic baslayacak
  var traficDensity = 0.0f

  // fix bir deger - araclarin ne kadar hizli gidecegi ekranda bu degere gore degisiyor cunku currenVitesse * deltaTime , her deltaTime surede o kadar yol alacak o anlamda
  var deltaTime = 01.1f

  /*
  You have to put the cars in lanes, posy = 305 to be in the left lane, posy = 270 to be in the right lane.
   */
  val car1 = Vehicule(Velocity(0, 0), 20f, Position(45f, 305f), new Acceleration(0.0f, 0.0f)) // Sol serit
  val car2 = Vehicule(Velocity(2, 0), 20f, Position(10f, 270f), new Acceleration(1.0f, 0.0f) /*Some(car1)*/) // arkadaki arac
  val car3 = Vehicule(Velocity(2, 0), 10f, Position(165f, 270f), new Acceleration(0.0f, 0.0f) /* Some(car2)*/) // ortadaki arac
  val car4 = Vehicule(Velocity(2, 0), 10f, Position(198f, 270f), new Acceleration(0.0f, 0.0f) /* Some(car2)*/) // en ondeki arac
  var cars = Array(car1, car2, car3,car4)

  override def onInit(): Unit = {
    setTitle("Traffic Simulation")
  }

  // Draw Road Serit genisligi 325 - 250 = 75 / 2 = 37.5
  // Right lane between 250-287, left lane 287 - 325
  def drawRoad(g: GdxGraphics): Unit = {
    g.drawLine(0f, 250, 500f, 250, Color.BLUE) // Bottom Line - Alt Cizgi
    val dashLength = 10f
    val spaceLength = 30f
    val startY = 287f
    var xPos = 0f
    while (xPos < 500f) {
      g.drawLine(xPos, startY, xPos + dashLength, startY, Color.WHITE) // - - -
      xPos += dashLength + spaceLength
    }
    g.drawLine(0f, 325, 500f, 325, Color.WHITE) // Top Line - Ust cizgi
  }

  def drawCircleRoad(g: GdxGraphics): Unit = {
    g.drawCircle(250, 250, 150, Color.WHITE)
    g.drawCircle(250, 250, 225, Color.WHITE)
  }

  // Draw and Moves the Cars
  def drawCars(cars: Array[Vehicule], g: GdxGraphics): Unit = {
    for (car <- cars) {
      g.drawFilledRectangle(car.position.x, car.position.y, car_width, car_height, 90f, Color.CHARTREUSE)
    }

    for(i<- 0 until(cars.length)){
      cars(i).move(deltaTime)
      if (cars(i).position.x > getWindowWidth) {
        cars(i).position.x = -car_width
      }
    }
      println(s"Ilk aracin hizi = ${cars(0).currentvitesse.dx} ve konumu ${cars(0).position.x}")
      println(s"Ikinci aracin aracin hizi = ${cars(1).currentvitesse.dx}  ve konumu ${cars(1).position.x}")
      println(s"Ucuncu aracin aracin hizi = ${cars(2).currentvitesse.dx} ve konumu ${cars(2).position.x}")
      println(s"Dorduncu aracin aracin hizi = ${cars(3).currentvitesse.dx} ve konumu ${cars(3).position.x}")

  } // End of the drawCars method

  override def onGraphicRender(g: GdxGraphics): Unit = {
      g.clear()
      drawRoad(g)
      drawCars(cars, g)

    //TODO :: BURADA DETECT OLURSA YAVASLAMASI LAZIM O OLMADI BI TURLU ..
      for (i <- 1 until cars.length) { // Start from 1 to avoid index out of bounds
        if (cars(i).position.distance(cars(i).position, cars(i - 1).position) > safeDistance) {
          cars(i).accelerate()
        } else {
          cars(i).currentvitesse = Velocity(2f,0f)
          //cars(i).updateVelocity(deltaTime, cars, safeDistance, g)

        }
      }

  }// End of the onGraphicsRender method
}// End of the class




object SimulationApp extends App {
  var a : SimulationGUI = new SimulationGUI()
}
