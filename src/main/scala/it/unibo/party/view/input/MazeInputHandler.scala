package it.unibo.party.view.input

import it.unibo.party.common.MinigamePhase
import it.unibo.party.controller.Moves.MazeMove
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.geometry.Direction
import scalafx.scene.input.{KeyCode, KeyEvent}

object MazeInputHandler:
  /**
   * Handles key events for the maze minigame.
   * Accepted keys are:
   *  - Up, Down, Left, and Right arrows for moving.
   *
   * @param event The key event to handle.
   * @param phase The current phase of the minigame.
   * @param agent The playing agent that will perform the move.
   */
  def apply(event: KeyEvent, phase: MinigamePhase, agent: PlayingAgent): Unit =
    phase match
      case MinigamePhase.Playing =>
        val direction: Option[Direction] =
          event.getCode match
            case KeyCode.Up.delegate => Some(Direction.Up)
            case KeyCode.Down.delegate => Some(Direction.Down)
            case KeyCode.Left.delegate => Some(Direction.Left)
            case KeyCode.Right.delegate => Some(Direction.Right)
            case _ => Option.empty
        if direction.isDefined then
          agent.makeMove(MazeMove.Movement(direction.get))
      case _ =>