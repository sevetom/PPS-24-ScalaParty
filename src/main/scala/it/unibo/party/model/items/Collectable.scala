package it.unibo.party.model.items

import it.unibo.party.model.items.Collectable.Rung

/**
 * Represents a collectable item in the party game.
 */
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
    /**
     * Tries to collect the given collectable, returning an Option with the updated owned items.
     *
     * @param owned the items already owned by the collector
     * @return Some updated owned items if the collectable can be collected, None otherwise
     */
    def tryCollectWith(owned: Seq[Collectable]): Option[Seq[Collectable]] = c match
      case m: Monad => Some(owned :+ m)
      case r: Rung =>
        val monadsOwned: Seq[Monad] = owned.collect:
          case m: Monad => m
        if monadsOwned.size >= r.monadsNeeded then
          val monadsPayed = monadsOwned take r.monadsNeeded
          Some(owned.diff(monadsPayed) :+ r)
        else
          Option.empty

    /**
     * @return the type of the collectable.
     */
    def getType: CollectableType = c match
      case Monad() => CollectableType.MonadType
      case Rung(_) => CollectableType.RungType

    /**
     * @return the price of the collectable.
     */
    def getPrice: Int = c match
      case Monad() => 0
      case Rung(price) => price

  extension (ct: CollectableType)
    /**
     * Converts the CollectableType to a Collectable.
     *
     * @return the corresponding Collectable base instance.
     */
    def toCollectable: Collectable = ct match
      case CollectableType.MonadType => Monad()
      case CollectableType.RungType => freeRung




