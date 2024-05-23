package Model

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color


class SimulationGUI extends PortableApplication {

  val screenWidth = getWindowWidth.toFloat
  val screenHeight = getWindowHeight.toFloat
  println(screenWidth)

  override def onInit(): Unit = {
    setTitle("Traffic Simulation")
  }


  var car1X = 45f
  var car2X = 105f
  var car3X = 165f

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()

    /* Designer Autoroute Circle
    g.drawCircle(250, 250, 150, Color.WHITE)
    g.drawCircle(250, 250, 225, Color.WHITE)
     */

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


    // Dessiner vehicule
    g.drawFilledRectangle(45,290,30f,40f,90f,Color.BLUE)
    g.drawFilledRectangle(105,290,30f,40f,90f,Color.DARK_GRAY)
    g.drawFilledRectangle(165,290,30f,40f,90f,Color.GRAY)

    // Move to Car






  } // End of the onGraphicsRender method






}



object aa extends App {

  var a : SimulationGUI = new SimulationGUI()


}