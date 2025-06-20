package it.unibo.party.model.player

import it.unibo.party.model.items.Collectable

trait Pocket:
  def getAll: Seq[Collectable]
  
  def getByType(item: Collectable): Seq[Collectable]

  def add(item: Collectable): Pocket

  def remove(item: Collectable): Pocket

  def contains(item: Collectable): Boolean

  def countByType(item: Collectable): Int

  def isEmpty: Boolean

  def size: Int

object Pocket:
  def apply(items: Seq[Collectable]): Pocket = PocketImpl(items)

  private case class PocketImpl(items: Seq[Collectable]) extends Pocket:
    override def getAll: Seq[Collectable] = items
    
    override def getByType(item: Collectable): Seq[Collectable] = items.filter(_.getClass == item.getClass)

    override def add(item: Collectable): Pocket = Pocket(items :+ item)

    override def remove(item: Collectable): Pocket = Pocket(items.filter(_ != item))

    override def contains(item: Collectable): Boolean = items.contains(item)

    override def countByType(item: Collectable): Int = items.count(_.getClass == item.getClass)

    override def isEmpty: Boolean = items.isEmpty

    override def size: Int = items.size

  val empty: Pocket = Pocket(List.empty[Collectable])




