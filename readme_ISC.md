# TraffISC 
 
## Building tool
First of all, please ensure you have runned build.sbt. This file contains the configuration of the project.
 ![enter image description here](https://i.imgur.com/k4MmBNu.png)
 ## Modelling Classes
 To model our traffic project we choose 4 different classes: Position, Velocity, Acceleration, Vehicle.
 ### Position
The `Position` class is designed to represent the coordinates of a point in a 2D space, allowing for manipulation of the position and calculation of distances between points. This is particularly useful for modeling the movement of objects, such as cars, in a 2D plane

```
case class Position(var x: Float, var y: Float) {
  def move(dx: Float, dy: Float): Position = Position(x + dx, y + dy)

  def distance(position1: Position, position2: Position): Double = {
    Math.sqrt(
      Math.pow(position1.x - position2.x, 2) +
      Math.pow(position1.y - position2.y, 2)
    )
  }
}
```
### Velocity
The `Velocity` class represents the velocity of an object in a 2D space, with components along the x-axis (`dx`) and y-axis (`dy`). It ensures that the velocity components are non-negative and calculates the total speed (`vitesseTotal`).
```
case class Velocity(var dx: Float, var dy: Float) {
  require(dx >= 0, "dx must be non-negative")
  require(dy >= 0, "dy must be non-negative")

  var vitesseTotal: Float = Math.sqrt(dx * dx + dy * dy).toFloat
}
```
### Acceleration
This class is useful for representing and manipulating the acceleration of an object moving in a 2D plane.
```
case class Acceleration(var ax: Float, var ay: Float){}
```

### Vehicle
The `Vehicule` class models a vehicle in a 2D space, managing its position, velocity, and acceleration.
```
	 case class Vehicule(var currentvitesse: Velocity,  
                    var maxVitesse: Float,  
                    var position: Position)
```                    
#### findCarInFront()

This function will:
* sort cars by their x-coordinate
* returns the index of the first element that satisfies the condition,
* return the Car in Front or no car
```
def findCarInFront(cars: ArrayBuffer[Vehicule]): Option[Vehicule] = {
val sortedCars = cars.sortBy(_.position.x)  
val index = sortedCars.indexWhere(_.position.x > position.x)  
if (index != -1) Some(sortedCars(index)) else None }
```
Unfortunately, this create some inconsistencies each time a car is in the right most position, not detecting that there is a car but at the beginning of the left most position.

#### updateVelocity()
The `updateVelocity` method in the `Vehicule` class is responsible for updating the velocity of the vehicle based on its surroundings and the presence of other vehicles.
If no cars are detected within the threshold, the vehicle accelerates by increasing its velocity (`currentvitesse.dx`) based on its acceleration (`acceleration.ax`) and the time elapsed (`deltaTime`).

If a car is detected within the threshold, the method tries to find the car directly in front using the `findCarInFront` method. 
If a car in front is found:
*   It checks the distance to the car in front.
*   If the distance is greater than `minDistance`, the vehicle accelerates.
*   If the distance is less than `minDistance`, the vehicle decelerates to match the speed of the car in front.
*   If no car is found in front (which theoretically shouldn't happen if `detectCar` returned true), the vehicle accelerates.

## Simulation
To simulate our traffic, we selected JFreeChart, which allows for real-time simulation. 
![enter image description here](https://i.imgur.com/w2kj5yU.png)
For visualizing our traffic, we chose GdxGraphics, a tool we are already familiar with.
![enter image description here](https://i.imgur.com/4Sy0bgf.png)

To compare different state of our simulation, we had to fix all the parameters aside one and modify the last one. 
Some parameters we found relevant to increase or decrease are the following.
```
//params to modify   
val carWidth = 10f  
val numCars = 40  
val density = numCars*carWidth / roadLength  
val minDistance = 30f
```
For example, reducing the number of cars will result in less stopped car overall.

## Functional Programming ( lemniscate branch )
To meet the physics course requirements, we had to make some compromise to the functional programming world.
The same project, can be found in the branch called lemniscate, written in functional programming. 
Unfortunately, some implementations didn't make it. 
Here are some of the functional programming paradigm that we respected:
### Immutability
We return new instances of `Vehicule` with the updated state instead of modifying the existing instance.
### Pure Functions
They take inputs and return outputs without modifying any state outside their scope.
```
def accelerate(v: Vehicule): Vehicule = {  
  if (v.currentvitesse.dx < v.maxVitesse) {  
    v.copy(currentvitesse = v.currentvitesse.update(v.currentvitesse.dx + 0.01f))  
  } else v  
}
```
### Helper Methods
It encapsulates specific functionalities. This promotes code reusability and makes the code easier to reason about.
```
def updateVelocity(v: Vehicule, deltaTime: Float, cars: ArrayBuffer[Vehicule], threshold: Float, g: GdxGraphics, roadLength: Float): Vehicule = {
  val updatingCondition = detectCar(v, cars, threshold, g)
  val updatedSpeed = if (!updatingCondition) {
    val newDx = math.min(v.currentvitesse.dx + 0.01f * deltaTime, v.maxVitesse)
    v.copy(currentvitesse = v.currentvitesse.update(newDx))
  } else {
    findCarInFront(v, cars, roadLength) match {
      case Some(car) =>
        val speedOfCarInFront = car.currentvitesse.dx
        if (distanceToCar(v, car) >= minDistance) {
          accelerate(v)
        } else {
          decelerate(v, speedOfCarInFront)
        }
      case None =>
        accelerate(v)
    }
  }
  updatedSpeed
}
```
Here, we establish a condition. Then we calculate a new state and we return a new `Vehicle` object.

                   
              
