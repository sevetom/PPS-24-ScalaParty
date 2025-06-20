package it.unibo.party.model.items

import org.scalatest.flatspec.AnyFlatSpec

class CollectableTest extends AnyFlatSpec:
  import Collectable.{Monad, Rung}
  import CollectableOperations.*

  "A Monad" should "be collectible by any owned items" in {
    val monad: Monad = Collectable.Monad()
    val owned: Seq[Collectable] = Seq(Collectable.Monad(), Collectable.Rung(1))
    assert(monad.collectibleBy(owned).isDefined)
  }

  "A Rung" should "be collectible if enough Monads are owned" in {
    val rungPrice: Int = 2
    val rung: Rung = Rung(rungPrice)
    val owned: Seq[Collectable] = for i <- 1 to rungPrice yield Monad()
    assert(rung.collectibleBy(owned).isDefined)
  }
