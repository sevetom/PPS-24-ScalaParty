package it.unibo.party.model.partyGame

trait Dice:
  def lastRolled: Int
  def roll(n: Int): Int

object Dice:
  def apply(): Dice = DiceImpl()

  case class DiceImpl() extends Dice:
    private var _lastRolled: Int = 0

    override def lastRolled: Int = _lastRolled

    override def roll(n: Int): Int =
      require(n > 0, "Dice must be rolled with a positive number of sides")
      _lastRolled = scala.util.Random.nextInt(n) + 1
      _lastRolled

