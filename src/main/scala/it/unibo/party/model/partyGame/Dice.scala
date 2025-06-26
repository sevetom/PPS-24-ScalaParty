package it.unibo.party.model.partyGame

trait CanRoll[T]:
  extension (t: T) def roll(n: Int): (T, List[Int])

trait Dice:
  def lastRolled: List[Int]

  def roll(n: Int = 1): (Dice, List[Int])

object Dice:
  def apply(): Dice = DiceImpl(List.empty)

  private val DICE_DIM = 6

  private case class DiceImpl(_lastRolled: List[Int]) extends Dice:
    override def lastRolled: List[Int] = _lastRolled

    override def roll(n: Int): (Dice, List[Int]) =
      require(n > 0, "Number of rolls must be positive")
      val newRoll = List.fill(n)(scala.util.Random.nextInt(DICE_DIM) + 1)
      (DiceImpl(newRoll), newRoll)