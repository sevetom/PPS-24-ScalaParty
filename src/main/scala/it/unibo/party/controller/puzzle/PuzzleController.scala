package it.unibo.party.controller.puzzle

import it.unibo.party.common.{MinigamePhase, Player, PuzzleState}
import it.unibo.party.controller.Moves.{Move, PuzzleMove}
import it.unibo.party.controller.{MiniController, Moves, TimedMiniController}
import it.unibo.party.geometry.Point2D
import it.unibo.party.model.puzzle.{PuzzlePosition, PuzzleUtils}
import it.unibo.party.model.puzzle.PuzzleUtils.normalizePiece

private val winTime = 15000L // 15 seconds
private val standardWidth = 5
private val standardHeight = 5
private val maxPieceSize = 5

class PuzzleController:

  def apply(challenger: Player, challenged: Player): MiniController =
    PuzzleControllerImpl(challenger, challenged, 0L, winTime, PuzzleState.empty)

  private case class PuzzleControllerImpl(
                                         challenger: Player,
                                         challenged: Player,
                                         startTime: Long,
                                         winTime: Long,
                                         state: PuzzleState
                                         ) extends TimedMiniController:

    override def start(): MiniController =
      val pieces = PuzzleUtils.generateRandomSolution(standardWidth, standardHeight, maxPieceSize)
      copy(
        startTime = System.currentTimeMillis(),
        state = PuzzleState(
          phase = MinigamePhase.Playing,
          winnerId = None,
          currentSolution = Map.empty,
          currentPiece = None,
          nonPlacedPieces = pieces
            .map(normalizePiece(_)
              .map(_.toPoint2D))
            .zipWithIndex
            .map((piece, index) => index -> piece)
            .toMap
        )
      )

    override def handleMove(move: Move): MiniController = (move, state.currentPiece) match
      case (PuzzleMove.SelectPiece(pieceId), _) => copy(
        state = state.copy(
          currentPiece = Some(pieceId),
          phase = if isTimeUp then MinigamePhase.GameOver else MinigamePhase.Playing,
          winnerId = if isTimeUp then Some(challenger.id) else None
        )
      )
      case (PuzzleMove.PlacePiece(position), Some(pieceId)) =>
        val newSolution = state.currentSolution.updated(
          pieceId,
          state.nonPlacedPieces(pieceId)
            .map(
              p => Point2D[Int](
                position.x + p.x,
                position.y + p.y
              )
            )
        )
        val isValidSolution = PuzzleUtils.isValidSolution(
          newSolution.values.toSet.map(_.map(p => PuzzlePosition(p.x, p.y))),
          standardWidth,
          standardHeight
        )
        copy(
        state = state.copy(
          nonPlacedPieces = state.nonPlacedPieces - pieceId,
          currentPiece = None,
          currentSolution = newSolution,
          phase = if isValidSolution || isTimeUp then MinigamePhase.GameOver else MinigamePhase.Playing,
          winnerId = if isValidSolution then Some(challenged.id) else if isTimeUp then Some(challenger.id) else None
        )
      )
      case (PuzzleMove.RemovePiece, Some(pieceId)) => copy(
        state = state.copy(
          phase = if isTimeUp then MinigamePhase.GameOver else MinigamePhase.Playing,
          nonPlacedPieces = state.nonPlacedPieces.updated(
            pieceId,
            PuzzleUtils
              .normalizePiece(state.currentSolution(pieceId).map(p => PuzzlePosition(p.x, p.y))).map(_.toPoint2D)),
          currentPiece = None,
          currentSolution = state.currentSolution - pieceId,
          winnerId = if isTimeUp then Some(challenger.id) else None
        )
      )
      case _ => copy(
        state = state.copy(
          phase = if isTimeUp then MinigamePhase.GameOver else MinigamePhase.Playing,
          winnerId = if isTimeUp then Some(challenger.id) else None
        )
      )
