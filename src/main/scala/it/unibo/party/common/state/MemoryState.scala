package it.unibo.party.common.state

import it.unibo.party.common.{MinigamePhase, MinigameState, Player}
import it.unibo.party.geometry.Point2D
import it.unibo.party.model.memory.Memory

case class MemoryState(
                        game: Memory,
                        phase: MinigamePhase,
                        winner: Option[Player],
                        firstSelection: Option[Point2D[Int]],
                        mismatchedPair: Option[(Point2D[Int], Point2D[Int])] = None,
                        winTime: Long
                      ) extends MinigameState
