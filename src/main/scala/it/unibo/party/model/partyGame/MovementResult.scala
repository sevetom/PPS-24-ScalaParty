package it.unibo.party.model.partyGame

import it.unibo.party.geometry.Direction

sealed trait MovementResult

object MovementResult:
  case class Moved(
                    updatedGame: PartyGame,
                    availableDirections: Option[List[Direction]]
                  ) extends MovementResult

  case object InvalidMove extends MovementResult
