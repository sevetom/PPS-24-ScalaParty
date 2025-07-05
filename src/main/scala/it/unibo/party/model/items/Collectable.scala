package it.unibo.party.model.items

import it.unibo.party.model.items.Collectable.Rung

enum Collectable:
  case Monad()
  case Rung(monadsNeeded: Int)

object Collectable:
  val freeRung: Rung = Rung(0)

enum CollectableType:
  case MonadType, RungType

object CollectableOperations:

  import Collectable.*

  extension (c: Collectable)
    def tryCollectWith(owned: Seq[Collectable]): Option[Seq[Collectable]] = c match
      case m: Monad => Some(owned :+ m)
      case r: Rung =>
        val monadsOwned: Seq[Monad] = owned.collect { case m: Monad => m }
        if monadsOwned.size >= r.monadsNeeded then
          val monadsPayed = monadsOwned take r.monadsNeeded
          Some(owned.diff(monadsPayed) :+ r)
        else
          Option.empty

    def getType: CollectableType = c match
      case _: Monad => CollectableType.MonadType
      case _: Rung => CollectableType.RungType

    def getPrice: Int = c match
      case _: Monad => 1
      case r: Rung => r.monadsNeeded
      
  extension(ct: CollectableType)
    def toCollectable: Collectable = ct match
      case CollectableType.MonadType => Monad()
      case CollectableType.RungType => freeRung




