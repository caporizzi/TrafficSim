// Define a trait for behavior
trait VehicleBehavior {
def applyBehavior(vehicle: Vehicule): Vehicule
}

// Implement different behaviors as classes
class AggressiveBehavior extends VehicleBehavior {
override def applyBehavior(vehicle: Vehicule): Vehicule = {
// Define behavior for aggressive vehicles
}
}

class CautiousBehavior extends VehicleBehavior {
override def applyBehavior(vehicle: Vehicule): Vehicule = {
// Define behavior for cautious vehicles
}
}

// Modify the Vehicule class to take behavior as a parameter
case class Vehicule(
currentvitesse: Velocity,
maxVitesse: Float,
position: Position,
behavior: VehicleBehavior
) {
def applyBehavior(): Vehicule = {
behavior.applyBehavior(this)
}
}

// Usage example
val aggressiveVehicule = Vehicule(Velocity(5f, 0), 10f, Position(100f, 100f), new AggressiveBehavior())
val cautiousVehicule = Vehicule(Velocity(3f, 0), 8f, Position(200f, 200f), new CautiousBehavior())

// Applying behavior
val newAggressiveVehicule = aggressiveVehicule.applyBehavior()
val newCautiousVehicule = cautiousVehicule.applyBehavior()