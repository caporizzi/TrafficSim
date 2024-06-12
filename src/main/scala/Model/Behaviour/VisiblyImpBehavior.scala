package Model.Behaviour
import Model.Vehicule

class VisiblyImpBehavior extends VehicleBehavior {
  override def applyBehaviour(vehicle: Vehicule): Vehicule = {
    vehicle.copy(safeDistance = 1000f)
  }
}