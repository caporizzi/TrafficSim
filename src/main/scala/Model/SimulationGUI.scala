package Model

import Model.Behaviour.StressedBehaviour
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
  val agent1 = Vehicule(Velocity(1, 0), 120f, Position(45f, 287f), Acceleration(0.0f, 0.0f),reactionTime=0)
  val agent2 = Vehicule(Velocity(1, 0), 150f, Position(100f, 287f), Acceleration(0.0f, 0.0f), Some(agent1),reactionTime=0)
  val agent3 = Vehicule(Velocity(1, 0), 90f, Position(165, 287f), Acceleration(0.0f, 0.0f), Some(agent2),reactionTime=0)

  // Car dimensions to draw
  var car_width = 15f
  var car_height = 20f

  // Car location l'axe_x
  var car1X = 45f
  var car2X = 105f
  var car3X = 165f

  var distanceNormal = 50f  // Entre les distance des voitures 20 pixel
  def setDistance(vehicule1: Vehicule, vehicule2: Vehicule) : Unit = {
    var p1 : Position = vehicule1.position
    var p2 : Position = vehicule2.position
    var distanceActuel = p1.distance(p1,p2)

    if(distanceActuel > distanceNormal)
      println("FIFI")
  }

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
    g.drawFilledRectangle(agent1.position.x, agent1.position.y, car_width, car_height, 90f, Color.BLUE)
    g.drawFilledRectangle(agent2.position.x, agent2.position.y, car_width, car_height, 90f, Color.WHITE)
    g.drawFilledRectangle(agent3.position.x, agent3.position.y, car_width, car_height, 90f, Color.CHARTREUSE)


    if (agent1.position.x > getWindowWidth) agent1.position.x = -car_width
    if (agent2.position.x > getWindowWidth) agent2.position.x = -car_width
    if (agent3.position.x > getWindowWidth) agent3.position.x = -car_width

    // Move cars
    for( i <- 0 until(10)){
      agent1.position.x += (i * 0.3).toFloat
      agent2.position.x += 0.5f
      agent3.position.x += 0.5f
    }

    var StressedBehaviour = new StressedBehaviour
    agent1.currentvitesse == StressedBehaviour.stressed(agent1.currentvitesse)



  } // End of the onGraphicsRender method


}

object aa extends App {
  var a : SimulationGUI = new SimulationGUI()
}