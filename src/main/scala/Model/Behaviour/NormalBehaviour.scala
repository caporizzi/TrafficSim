package Model.Behaviour
import Model.Vehicule

class NormalBehaviour extends VehicleBehavior {
  override def applyBehaviour(vehicle: Vehicule): Vehicule = vehicle
}
