package Model

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color


class SimulationGUI extends PortableApplication {


  override def onInit(): Unit = {
    setTitle("Traffic Simulation")
  }

  def drawCar(g:GdxGraphics , carNumber : Int) : Unit = ??? // TODO = to draw car automatiquement


  def dessinerHighWayCircle(g: GdxGraphics) : Unit = {
       g.drawCircle(250, 250, 150, Color.WHITE)
       g.drawCircle(250, 250, 225, Color.WHITE)
  }

  // Create the position and car
  /*
  All of car will start the position x = 0(ama hepsinin arasinda  , y = sabit kalacak)
   */
  val agent1 = Vehicule(Velocity(1, 0), 120f, Position(45f, 287f), Acceleration(0.0f, 0.0f))
  val agent2 = Vehicule(Velocity(1, 0), 150f, Position(100f, 287f), Acceleration(0.0f, 0.0f), Some(agent1))
  val agent3 = Vehicule(Velocity(1, 0), 90f, Position(165, 287f), Acceleration(0.0f, 0.0f), Some(agent2))


  // Car dimensions to draw
  var car_width = 30f
  var car_height = 40f
  var distance = 50f // TODO :: After you must check
  var carColor = Color.WHITE


  var car1X = 45f
  var car2X = 105f
  var car3X = 165f

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()



    // Dessiner straight road
    g.drawLine(0f, 250, 500f, 250, Color.WHITE)
    val dashLength = 10f
    val spaceLength = 10f
    val startY = 287f
    var xPos = 0f

    while (xPos < 500f) {
      g.drawLine(xPos, startY, xPos + dashLength, startY, Color.WHITE)
      xPos += dashLength + spaceLength
    }
    g.drawLine(0f, 325, 500f, 325, Color.WHITE)

    // Dessiner vehicule with created class
    g.drawFilledRectangle(agent1.position.x,agent1.position.y,car_width,car_height,90f,carColor)
    g.drawFilledRectangle(agent2.position.x,agent2.position.y,car_width,car_height,90f,carColor)
    g.drawFilledRectangle(agent3.position.x, agent3.position.y,car_width,car_height,90f,carColor)

    // Move cars
    agent1.position.x += 0.5f
    agent2.position.x += 0.5f
    agent3.position.x += 0.5f


  } // End of the onGraphicsRender method






}



object aa extends App {

  var a : SimulationGUI = new SimulationGUI()


}