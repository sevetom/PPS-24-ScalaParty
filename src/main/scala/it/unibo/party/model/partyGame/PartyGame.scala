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

trait Dice:
  def lastRolled: List[Int]
  def roll(n: Int): (Dice, List[Int])

object Dice:
  def apply(): Dice = DiceImpl(List.empty)

  private val DICE_DIM = 6

  private case class DiceImpl(_lastRolled: List[Int]) extends Dice:
    override def lastRolled: List[Int] = _lastRolled

    override def roll(n: Int): (Dice, List[Int]) =
      require(n > 0, "Number of rolls must be positive")
      val newRoll = List.fill(n)(scala.util.Random.nextInt(DICE_DIM) + 1)
      (DiceImpl(newRoll), newRoll)


