package it.unibo.party.view.input
import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.Moves.PartyMoveType.Movement
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.geometry.Direction
import scalafx.scene.input.{KeyCode, KeyEvent}
object InputHandler:
  
  def apply(event: KeyEvent, state: GameState, agent: PlayingAgent): Unit = {
    if state.currentPlayer == agent.id && state.phase == GamePhase.PlayerMoving then
      event.getCode match
        case KeyCode.Up.delegate => agent.makeMove(PartyMove(state.currentPlayer, Movement, Some(Direction.Up)))
        case KeyCode.Down.delegate => agent.makeMove(PartyMove(state.currentPlayer, Movement, Some(Direction.Down))) 
        case KeyCode.Left.delegate => agent.makeMove(PartyMove(state.currentPlayer, Movement, Some(Direction.Left)))
        case KeyCode.Right.delegate => agent.makeMove(PartyMove(state.currentPlayer, Movement, Some(Direction.Right)))
        case _ => // Ignore other keys
  }
      


