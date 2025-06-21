package it.unibo.party.model.items

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class CollectableTest extends AnyFlatSpec:
  import Collectable.{Monad, Rung}
  import CollectableOperations.*

  "A Monad" should "be collectible by any owned items" in:
    val monad: Monad = Collectable.Monad()
    val owned: Seq[Collectable] = Seq(Collectable.Monad(), Collectable.Rung(1))
    monad.collectibleBy(owned).isDefined shouldBe true

  "A Rung" should "be collectible if enough Monads are owned" in:
    val rungPrice: Int = 2
    val rung: Rung = Rung(rungPrice)
    val owned: Seq[Collectable] = for i <- 1 to rungPrice yield Monad()
    rung.collectibleBy(owned).isDefined shouldBe true
