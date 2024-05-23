package Model

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics


class SimulationGUI extends PortableApplication {

  override def onInit(): Unit = {
    setTitle("Traffic Simulation")
  }


  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()

  }



}


object aa extends App {

  var a : SimulationGUI = new SimulationGUI()


}