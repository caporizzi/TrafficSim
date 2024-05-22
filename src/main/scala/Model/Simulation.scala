import Model.{Acceleration, Position, Route, Velocity}

object SimulationApp extends App {

  val agent1 = new Vehicule(Velocity(1, 0),120d, Position(0, 0),Acceleration(0.0,0.0))
  val agent2 = new Vehicule(Velocity(1, 0),150d, Position(10, 0),Acceleration(0.0,0.0), Some(agent1))
  val agent3 = new Vehicule(Velocity(1, 0), 90, Position(20, 0),Acceleration(0.0,0.0), Some(agent2))

  def runSimulation(): Unit = {
    agent3.moveNext()
    println(s"Agent 1: ${agent1.position}")
    println(s"Agent 2: ${agent2.position}")
    println(s"Agent 3: ${agent3.position}")
  }



  //run to simulation
  for (_ <- 1 to 10) {
    runSimulation()
    Thread.sleep(500)
  }


  val autoRoute : Route = new Route(100,2) // it has 2 voies and length is 100 km
  autoRoute.addAgent(Vehicule)




}