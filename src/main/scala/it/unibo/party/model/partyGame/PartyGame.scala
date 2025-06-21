package it.unibo.party.model.partyGame

import it.unibo.party.model.board.GameBoard.GameBoard

trait PartyGame:
  def board: GameBoard

object PartyGame:
  def apply(board: GameBoard): PartyGame = PartyGameImpl(board)

  private case class PartyGameImpl(board: GameBoard) extends PartyGame

  def empty: PartyGame = PartyGameImpl(GameBoard(Map.empty, Map.empty))

