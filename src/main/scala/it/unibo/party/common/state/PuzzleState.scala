package it.unibo.party.common.state

import it.unibo.party.common.{MinigamePhase, MinigameState, Player}
import it.unibo.party.geometry.Point2D

case class PuzzleState(
                        phase: MinigamePhase,
                        winner: Option[Player],
                        currentSolution: Map[Int, Set[Point2D[Int]]],
                        currentPiece: Option[Int],
                        nonPlacedPieces: Map[Int, Set[Point2D[Int]]],
                        width: Int,
                        height: Int,
                        maxTime: Long
                      ) extends MinigameState

object PuzzleState:
  def empty: PuzzleState =
    PuzzleState(MinigamePhase.Playing, None, Map.empty, None, Map.empty, 0, 0, 0L)
