package it.unibo.party.model.partyGame

import it.unibo.party.model.board.GameBoard.GameBoard
import it.unibo.party.model.partyGame

trait PartyGame:
  def board: GameBoard
  def dice: Dice

object PartyGame:
  def apply(board: GameBoard)(using dice: Dice): PartyGame = PartyGameImpl(board, dice)

  given defaultDice : Dice = Dice()

  private case class PartyGameImpl(board: GameBoard, dice: Dice) extends PartyGame

  def empty: PartyGame = PartyGameImpl(GameBoard(Map.empty, Map.empty), summon[Dice])

  given CanRoll[PartyGame] with
    extension (pg: PartyGame)
      def roll(n: Int): (PartyGame, List[Int]) =
        val (newDice, result) = pg.dice.roll(n)
        (PartyGame(pg.board)(using newDice), result)