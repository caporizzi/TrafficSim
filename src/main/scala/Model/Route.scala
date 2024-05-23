package Model

import scala.collection.mutable.ArrayBuffer

class Route(var length: Double,
            var voies: Int,
            var vehicules : ArrayBuffer[Vehicule] = ArrayBuffer[Vehicule]()) {

  require(voies >= 1, "Number of lanes (voies) must be at least 1")
  require(length > 0.0, "Length of the route must be positive")


  def addAgent(newVehicule: Vehicule): Unit = {
    vehicules += newVehicule
  }

}
