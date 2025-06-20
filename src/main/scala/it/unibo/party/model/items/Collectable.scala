package it.unibo.party.model.items

enum Collectable:
  case Monad()
  case Rung(price: Int)

object CollectableOperations:
  import Collectable.*

  extension (c: Collectable)
    def collectibleBy(owned: Seq[Collectable]): Option[Collectable] = c match
      case m: Monad => Some(m)
      case r: Rung =>
        val monadsOwned = owned.count(_.isInstanceOf[Monad])
        if monadsOwned <= r.price then Some(r) else Option.empty


