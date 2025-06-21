package it.unibo.party.model.items

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class CollectableTest extends AnyFlatSpec:
  import Collectable.{Monad, Rung}
  import CollectableOperations.*

  val monad: Collectable = Monad()
  val rungPrice: Int = 1
  val rung: Collectable = Rung(rungPrice)

  "A Monad" should "be collectible by any owned items" in:
    val owned: Seq[Collectable] = Seq.empty
    monad.tryCollectWith(owned).isDefined shouldBe true

  "A Rung" should "be collectible if enough Monads are owned" in:
    val owned: Seq[Collectable] = for i <- 1 to rungPrice yield Monad()
    rung.tryCollectWith(owned).isDefined shouldBe true

  "A collectable" should "be returned, if collected, in the sequence with the remaining items" in:
    val collectable: Collectable = monad
    val owned: Seq[Collectable] = Seq(monad, rung)
    collectable.tryCollectWith(owned).get should contain theSameElementsAs Seq(monad, rung, collectable)

  it should "return nothing if not collected" in:
    val collectable: Collectable = rung
    val owned: Seq[Collectable] = Seq.empty
    collectable.tryCollectWith(owned).isEmpty shouldBe true
