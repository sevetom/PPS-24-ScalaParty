package it.unibo.party.model.board

import it.unibo.party.geometry.Point2D
import it.unibo.party.model.board.BoardBox.BoardBox.{EmptyBox, FullBox}
import it.unibo.party.model.items.*
import it.unibo.party.model.player.{Pawn, Pocket}
import it.unibo.party.model.items.CollectableOperations.*

object BoardBox:
  enum BoardBox:
    case EmptyBox
    case FullBox(item: Collectable)
    
  extension(b: BoardBox)
    def isEmpty: Boolean = b match 
      case EmptyBox => true
      case _ => false
      
    def tryAcquireItem(owned: Seq[Collectable]): (Seq[Collectable], BoardBox) = b match 
      case EmptyBox => (owned, EmptyBox)
      case FullBox(item) => 
        val result = item.tryCollectWith(owned)
        if result.isDefined then (result.get, EmptyBox) else (owned, FullBox(item))


      
    




