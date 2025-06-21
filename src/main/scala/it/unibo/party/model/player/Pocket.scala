package it.unibo.party.model.player

import it.unibo.party.model.items.{Collectable, CollectableType}
import it.unibo.party.model.items.CollectableOperations.getType

trait Pocket:
  def getAll: Seq[Collectable]
  
  def getByType(itemType: CollectableType): Seq[Collectable]

  def add(item: Collectable): Pocket

  def remove(item: Collectable): Pocket

  def addMultiple(item: Collectable, count: Int): Pocket

  def removeMultipleByType(itemType: CollectableType, count: Int): Pocket

  def contains(item: Collectable): Boolean

  def countByType(itemType: CollectableType): Int

  def isEmpty: Boolean

  def size: Int

object Pocket:
  def apply(items: Seq[Collectable]): Pocket = PocketImpl(items)

  private case class PocketImpl(items: Seq[Collectable]) extends Pocket:
    override def getAll: Seq[Collectable] = items
    
    override def getByType(itemType: CollectableType): Seq[Collectable] = items.filter(_.getType == itemType)

    override def add(item: Collectable): Pocket = Pocket(items :+ item)

    override def remove(item: Collectable): Pocket = Pocket(items.filter(_ != item))

    override def addMultiple(item: Collectable, count: Int): Pocket = Pocket(items ++ Seq.fill(count)(item))

    override def removeMultipleByType(itemType: CollectableType, count: Int): Pocket =
      val typedItems = items.filter(_.getType == itemType)
      if typedItems.size <= count then
        Pocket(items.filterNot(_.getType == itemType))
      else
        Pocket(items.diff(typedItems.take(count)))

    override def contains(item: Collectable): Boolean = items.contains(item)

    override def countByType(itemType: CollectableType): Int = items.count(_.getType == itemType)

    override def isEmpty: Boolean = items.isEmpty

    override def size: Int = items.size

  val empty: Pocket = Pocket(List.empty[Collectable])




