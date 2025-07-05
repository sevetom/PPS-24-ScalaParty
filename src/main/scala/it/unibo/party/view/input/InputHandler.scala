package it.unibo.party.view.input

import it.unibo.party.common.{PartyPhase, PartyState}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.Moves.PartyMoveType.*
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.geometry.Direction
import scalafx.scene.input.{KeyCode, KeyEvent}

object InputHandler:

  def apply(event: KeyEvent, state: PartyState, agent: PlayingAgent): Unit =
    if state.currentPlayer.id == agent.id then
      val directions = state.possibleDirections.getOrElse(Set.empty[Direction])
        state.phase match
          case PartyPhase.PlayerMoving =>
            event.getCode match
              case KeyCode.Up.delegate if directions.contains(Direction.Up) => 
                agent.makeMove(PartyMove(state.currentPlayer.id, Movement, Some(Direction.Up)))
              case KeyCode.Down.delegate if directions.contains(Direction.Down) => 
                agent.makeMove(PartyMove(state.currentPlayer.id, Movement, Some(Direction.Down)))
              case KeyCode.Left.delegate if directions.contains(Direction.Left) => 
                agent.makeMove(PartyMove(state.currentPlayer.id, Movement, Some(Direction.Left)))
              case KeyCode.Right.delegate if directions.contains(Direction.Right) => 
                agent.makeMove(PartyMove(state.currentPlayer.id, Movement, Some(Direction.Right)))
              case _ =>
          case PartyPhase.StartingRoll | PartyPhase.DiceRoll =>
            event.getCode match
              case KeyCode.Space.delegate =>
                agent.makeMove(PartyMove(state.currentPlayer.id, DiceRoll, Option.empty[Direction]))
              case _ =>
          case _ =>