package it.unibo.party.model.player

/**
 * Represents a Pawn in the game, which has a position and a pocket.
 *
 * @tparam P the type of the position, which can be any type that represents a game position.
 */
trait Pawn[P]:
  /**
   * @return the position of the pawn.
   */
  def position: P

  /**
   * @return the pocket of the pawn.
   */
  def pocket: Pocket

  /**
   * Moves the pawn to a new position.
   *
   * @param newPosition the new position to move to.
   * @return a new Pawn instance with the updated position.
   */
  def moveTo(newPosition: P): Pawn[P]

  /**
   * Updates the pocket of the pawn.
   *
   * @param newPocket the new pocket to set.
   * @return a new Pawn instance with the updated pocket.
   */
  def withPocket(newPocket: Pocket): Pawn[P]

object Pawn:
  /**
   * Creates a new Pawn with the given position and pocket.
   *
   * @param position the position of the pawn.
   * @param pocket   the pocket of the pawn.
   * @tparam P the type of the position.
   * @return a new Pawn instance.
   */
  def apply[P](position: P, pocket: Pocket): Pawn[P] = PawnImpl(position, pocket)

  private case class PawnImpl[P](position: P, pocket: Pocket) extends Pawn[P]:

    override def moveTo(newPosition: P): Pawn[P] = this.copy(position = newPosition)

    override def withPocket(newPocket: Pocket): Pawn[P] = this.copy(pocket = newPocket)

  def emptyPockets[P](position: P): Pawn[P] = Pawn(position, Pocket.empty)


