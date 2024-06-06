package Model.Indicator

import Model.{Position, Route, Velocity}




class TrafficIndicator(route: Route) {
  //chatgpt
  /*
  def isJam: Boolean = {

    route.vehicules.combinations(2).exists {
      case Seq(v1, v2) =>
        v1.velocity.vitesseTotal == 0 && v2.velocity.vitesseTotal == 0 &&
          distanceBetween(v1.position, v2.position) <= 10 // Example distance threshold for jam
    }
  }
  //chatgpt
  def isStable: Boolean = {
    val distinctSpeeds = route.vehicules.map(_.velocity.vitesseTotal).distinct
    distinctSpeeds.length == 1 && route.vehicules.combinations(2).forall {
      case Seq(v1, v2) =>
        distanceBetween(v1.position, v2.position) >= 10 // Example distance threshold for stable
    }
  }
  //chatgpt
  private def distanceBetween(p1: Position, p2: Position): Double = {
    sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2))
  }

   */
}
