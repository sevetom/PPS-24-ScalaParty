package it.unibo.party.model.player

trait Pawn[P]:
  def position: P
  def pocket: Pocket

  def moveTo(newPosition: P): Pawn[P]

  def withPocket(newPocket: Pocket): Pawn[P]

object Pawn:
  def apply[P](position: P, pocket: Pocket): Pawn[P] = PawnImpl(position, pocket)

  private case class PawnImpl[P](position: P, pocket: Pocket) extends Pawn[P]:

    override def moveTo(newPosition: P): Pawn[P] = this.copy(position = newPosition)

    override def withPocket(newPocket: Pocket): Pawn[P] = this.copy(pocket = newPocket)
    
  def emptyPockets[P](position: P): Pawn[P] = Pawn(position, Pocket.empty)


