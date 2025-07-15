package it.unibo.party.model.partyGame

trait MovementResult

object MovementResult:
  /**
   * Represents a successful movement of a pawn in the game.
   * @param updatedGame The updated game state after the movement.
   */
  case class Moved(updatedGame: PartyGame) extends MovementResult

  /**
   * Represents a situation where the movement is invalid
   */
  case object InvalidMove extends MovementResult
