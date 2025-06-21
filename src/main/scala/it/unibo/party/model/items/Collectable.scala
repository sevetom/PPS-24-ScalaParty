package it.unibo.party.model.items

enum Collectable:
  case Monad()
  case Rung(monadsNeeded: Int)

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
          
  val freeRung: Rung = Rung(0)




