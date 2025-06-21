package it.unibo.party.model.partyGame

import it.unibo.party.model.board.GameBoard.GameBoard
import it.unibo.party.model.partyGame

trait PartyGame:
  def board: GameBoard
  def dice: Dice

object PartyGame:
  def apply(board: GameBoard, dice: Dice): PartyGame = PartyGameImpl(board, dice)

  private case class PartyGameImpl(board: GameBoard, dice: Dice) extends PartyGame

  def empty: PartyGame = PartyGameImpl(GameBoard(Map.empty, Map.empty), Dice())

trait Dice:
  def lastRolled: List[Int]
  def roll(n: Int): List[Int]

object Dice:
  def apply(): Dice = DiceImpl()

  private val DICE_DIM = 6

  case class DiceImpl() extends Dice:
    private var _lastRolled: List[Int] = List.empty

    override def lastRolled: List[Int] = _lastRolled

    override def roll(n: Int): List[Int] =
      require(n > 0, "Number of rolls must be positive")
      _lastRolled = List.fill(n)(scala.util.Random.nextInt(DICE_DIM) + 1)
      _lastRolled


