package it.unibo.party.model.partyGame

import it.unibo.party.model.board.GameBoard.GameBoard
import it.unibo.party.model.partyGame
import it.unibo.party.model.partyGame.Dice

trait PartyGame:
  def board: GameBoard
  def dice: Dice

object PartyGame:
  def apply(board: GameBoard, dice: Dice): PartyGame = PartyGameImpl(board, dice)

  private case class PartyGameImpl(board: GameBoard, dice: Dice) extends PartyGame

  def empty: PartyGame = PartyGameImpl(GameBoard(Map.empty, Map.empty), Dice())

trait Dice:
  def lastRolled: Int
  def roll(n: Int): Int

object Dice:
  def apply(): Dice = DiceImpl()

  private val DICE_DIM = 6

  case class DiceImpl() extends Dice:
    private var _lastRolled: Int = 0

    override def lastRolled: Int = _lastRolled

    override def roll(n: Int): Int = {
      _lastRolled = 0
      _lastRolled
    }

