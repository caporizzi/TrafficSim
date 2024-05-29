package Model

import Model.Behaviour.StressedBehaviour
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import jdk.internal.agent.resources.agent

class SimulationGUI extends PortableApplication {
  override def onInit(): Unit = {
    setTitle("Traffic Simulation")
  }
  // Draw Road
  def drawRoad(g: GdxGraphics): Unit = {
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
  }

  def drawCircleRoad(g: GdxGraphics) : Unit = {
       g.drawCircle(250, 250, 150, Color.WHITE)
       g.drawCircle(250, 250, 225, Color.WHITE)
  }

  // Create the position and car
  /*
  All of car will start the position x = 0(ama hepsinin arasinda  , y = sabit kalacak)
   */
  val agent1 = Vehicule(Velocity(10, 0), 120f, Position(45f, 287f), Acceleration(0.0f, 0.0f),reactionTime=0)
  val agent2 = Vehicule(Velocity(15, 0), 150f, Position(75f, 287f), Acceleration(0.0f, 0.0f), Some(agent1),reactionTime=0)
  //val agent3 = Vehicule(Velocity(1, 0), 90f, Position(165, 287f), Acceleration(0.0f, 0.0f), Some(agent2),reactionTime=0)
  var cars : Array[Vehicule] = Array(agent1,agent2)//agent2,agent3

  // Car dimensions to draw
  var car_width = 15f
  var car_height = 20f
  var distanceNormal = 30f

  var sensorFront : Array[Float] = Array(agent1.position.x + car_width/2,agent2.position.x + car_width/2) // ,agent2.position.x,agent3.position.x
  println(s"Baslangicta sensor konumu : ${sensorFront.mkString(" - ")}")
  var detecteJam : Boolean = false


  def setDistance(vehicule1: Vehicule, vehicule2: Vehicule) : Unit = {
    var p1 : Position = vehicule1.position
    var p2 : Position = vehicule2.position
    var distanceActuel = p1.distance(p1,p2)

    if(distanceActuel > distanceNormal)
      println("FIFI")
  }
  var color : Array[Color] = Array(Color.WHITE,Color.CHARTREUSE)

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    drawRoad(g)
    //drawCircleRoad(g)

    // Draw the Cars
    for(car <- cars){
      g.drawFilledRectangle(car.position.x, car.position.y, car_width, car_height, 91f, Color.CHARTREUSE)
    }

    // Moves Car
    if (agent1.position.x > getWindowWidth) {
      agent1.position.x = -car_width
    }
    if (agent2.position.x > getWindowWidth)
      agent2.position.x = -car_width

/*
    if (agent3.position.x > getWindowWidth)
      agent3.position.x = -car_width
      sensorFront(2) = agent2.position.x
     */

    // Araba 10 saniye boyunca hareket etsin saatte ki hizi saniye de 10 pixel x ekseninde
    // Move cars with a vitesse ( pas constant )
    for (i <- 0 until (1)) {
      agent1.position.x += agent1.currentvitesse.dx
      agent2.position.x += agent2.currentvitesse.dx
      sensorFront(0) = agent1.position.x + car_width/2
      sensorFront(1) = agent2.position.x + car_width/2

      //agent2.position.x += 1.5f
      //agent3.position.x += 0.5f
    }

  } // End of the onGraphicsRender method


}

object aa extends App {
  var a : SimulationGUI = new SimulationGUI()
}