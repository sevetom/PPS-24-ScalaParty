package it.unibo.party.model.player

import it.unibo.party.model.items.{Collectable, CollectableType}
import it.unibo.party.model.items.CollectableOperations.getType

/**
 * Represents a container that can hold collectable items.
 */
trait Pocket:
  /**
   * Retrieves all collectable items in the pocket.
   *
   * @return a sequence of all collectable items.
   */
  def getAll: Seq[Collectable]
  
  /**
   * Retrieves collectable items of a specific type.
   *
   * @param itemType the type of collectable items to retrieve.
   * @return a sequence of collectable items of the specified type.
   */
  def getByType(itemType: CollectableType): Seq[Collectable]

  /**
   * Adds a collectable item to the pocket.
   *
   * @param item the collectable item to add.
   * @return a new Pocket instance with the item added.
   */
  def add(item: Collectable): Pocket

  /**
   * Removes a collectable item from the pocket.
   *
   * @param item the collectable item to remove.
   * @return a new Pocket instance with the item removed.
   */
  def remove(item: Collectable): Pocket

  /**
   * Adds multiple instances of a collectable item to the pocket.
   *
   * @param item  the collectable item to add.
   * @param count the number of instances to add.
   * @return a new Pocket instance with the items added.
   */
  def addMultiple(item: Collectable, count: Int): Pocket

  /**
   * Removes multiple instances of collectable items of a specific type from the pocket.
   *
   * @param itemType the type of collectable items to remove.
   * @param count    the number of instances to remove.
   * @return a new Pocket instance with the items removed.
   */
  def removeMultipleByType(itemType: CollectableType, count: Int): Pocket

  /**
   * Checks if the pocket contains a specific collectable item.
   *
   * @param item the collectable item to check for.
   * @return true if the item is in the pocket, false otherwise.
   */
  def contains(item: Collectable): Boolean

  /**
   * Counts the number of collectable items of a specific type in the pocket.
   *
   * @param itemType the type of collectable items to count.
   * @return the number of collectable items of the specified type.
   */
  def countByType(itemType: CollectableType): Int

  /**
   * Checks if the pocket is empty.
   *
   * @return true if the pocket is empty, false otherwise.
   */
  def isEmpty: Boolean

  /**
   * Gets the total number of collectable items in the pocket.
   *
   * @return the size of the pocket.
   */
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
      Pocket(items.diff(typedItems.take(count)))

    override def contains(item: Collectable): Boolean = items.contains(item)

    override def countByType(itemType: CollectableType): Int = items.count(_.getType == itemType)

    override def isEmpty: Boolean = items.isEmpty

    override def size: Int = items.size

  val empty: Pocket = Pocket(List.empty[Collectable])




