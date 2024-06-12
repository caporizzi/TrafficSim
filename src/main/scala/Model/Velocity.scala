package Model


 case class Velocity(var dx: Float, var dy: Float) {
   require(dx >= 0)
   require(dy >= 0)
   def update(newDx: Float): Velocity = this.copy(dx = newDx)

  }