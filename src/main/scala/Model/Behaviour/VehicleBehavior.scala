package Model.Behaviour

import Model.Vehicule

trait VehicleBehavior {
  def applyBehaviour(vehicle: Vehicule): Vehicule
}

