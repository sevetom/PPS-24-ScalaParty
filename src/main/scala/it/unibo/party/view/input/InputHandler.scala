package it.unibo.party.view.input

import it.unibo.party.common.{PartyPhase, PartyState}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.Moves.PartyMoveType.*
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.geometry.Direction
import scalafx.scene.input.{KeyCode, KeyEvent}

object InputHandler:

  def apply(event: KeyEvent, state: PartyState, agent: PlayingAgent): Unit = {
    val directions = state.possibleDirections.getOrElse(Set.empty[Direction])
    if state.currentPlayer == agent.id && state.phase == PartyPhase.PlayerMoving then
      event.getCode match
        case KeyCode.Up.delegate if directions.contains(Direction.Up) => agent.makeMove(PartyMove(state.currentPlayer, Movement, Some(Direction.Up)))
        case KeyCode.Down.delegate if directions.contains(Direction.Down) => agent.makeMove(PartyMove(state.currentPlayer, Movement, Some(Direction.Down)))
        case KeyCode.Left.delegate if directions.contains(Direction.Left) => agent.makeMove(PartyMove(state.currentPlayer, Movement, Some(Direction.Left)))
        case KeyCode.Right.delegate if directions.contains(Direction.Right) => agent.makeMove(PartyMove(state.currentPlayer, Movement, Some(Direction.Right)))
        case KeyCode.Space.delegate =>
          agent.makeMove(PartyMove(state.currentPlayer, DiceRoll, Option.empty[Direction]))
        case _ => // Ignore other keys
  }
      


