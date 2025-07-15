package it.unibo.party.model.partyGame

import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition

import scala.util.Try

given MovementManager:AnyRef with
  extension (pg: PartyGame)
    /**
     * Moves a pawn to a specified position on the board.
     * @param pawnId The ID of the pawn to move.
     * @param to The target position on the board.
     * @return A MovementResult indicating the outcome of the move.
     */
    def movePawn(pawnId: Int, to: BoardPosition): MovementResult =
      Try:
        val oldPawn = pg.board.pawns(pawnId)
        val oldPocket = oldPawn.pocket.getAll
        val newBoard = pg.board.movePawn(pawnId, to)
        val newPawn = newBoard.pawns(pawnId)
        val collected = newPawn.pocket.getAll.diff(oldPocket)
        val updatedGame = PartyGame(newBoard)(using pg.dice)
        MovementResult.Moved(updatedGame)
      .getOrElse(MovementResult.InvalidMove)

