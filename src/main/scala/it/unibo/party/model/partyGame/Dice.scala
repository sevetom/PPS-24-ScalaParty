package it.unibo.party.model.partyGame

trait CanRoll[T]:
  extension (t: T) def roll(n: Int): (T, List[Int])

trait Dice:
  /**
   * The last rolled values.
   * @return A list of integers representing the last rolled values.
   */
  def lastRolled: List[Int]

  /**
   * Rolls the dice a specified number of times.
   * @param n The number of times to roll the dice. Must be positive.
   * @return A tuple containing the new Dice instance and a list of the rolled values.
   */
  def roll(n: Int = 1): (Dice, List[Int])

object Dice:
  /**
   * Creates a new instance of Dice with no previous rolls.
   * @return A new Dice instance.
   */
  def apply(): Dice = DiceImpl(List.empty)

  private val DICE_DIM = 6

  private case class DiceImpl(_lastRolled: List[Int]) extends Dice:
    override def lastRolled: List[Int] = _lastRolled

    override def roll(n: Int): (Dice, List[Int]) =
      require(n > 0, "Number of rolls must be positive")
      val newRoll = List.fill(n)(scala.util.Random.nextInt(DICE_DIM) + 1)
      val newDice = copy(_lastRolled = newRoll)
      (newDice, newRoll)