package it.unibo.party.model.partyGame

import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition

import scala.util.Try

given MovementManager:AnyRef with
  extension (pg: PartyGame)
    def movePawn(pawnId: Int, to: BoardPosition): MovementResult =
      Try {
        val oldPawn = pg.board.pawns(pawnId)
        val oldPocket = oldPawn.pocket.getAll
  
        val newBoard = pg.board.movePawn(pawnId, to)
        val newPawn = newBoard.pawns(pawnId)
        val collected = newPawn.pocket.getAll.diff(oldPocket)
  
        val availableDirs = newBoard.availableDirections(to)
  
        val updatedGame = PartyGame(newBoard)(using pg.dice)
        MovementResult.Moved(updatedGame, if availableDirs == Set.empty then None else Some(availableDirs))
      }.getOrElse(MovementResult.InvalidMove)

