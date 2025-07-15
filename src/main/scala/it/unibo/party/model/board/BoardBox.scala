package it.unibo.party.model.board

import it.unibo.party.model.board.BoardBox.BoardBox.{EmptyBox, FullBox}
import it.unibo.party.model.items.*
import it.unibo.party.model.items.CollectableOperations.*

object BoardBox:
  enum BoardBox:
    case EmptyBox
    case FullBox(item: Collectable)
    
  extension(b: BoardBox)
    /**
     * Checks if the box is empty.
     * @return true if the box is empty, false otherwise.
     */
    def isEmpty: Boolean = b match 
      case EmptyBox => true
      case _ => false

    /**
     * Tries to acquire an item from the box with the items owned by the player.
      * @param owned the sequence of items owned by the player.
     * @return a tuple containing the updated sequence of owned items and the new state of the box.
     */  
    def tryAcquireItem(owned: Seq[Collectable]): (Seq[Collectable], BoardBox) = b match 
      case EmptyBox => (owned, EmptyBox)
      case FullBox(item) => 
        val result = item.tryCollectWith(owned)
        if result.isDefined then (result.get, EmptyBox) else (owned, FullBox(item))


      
    




