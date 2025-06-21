package it.unibo.party.model.board

import it.unibo.party.geometry.Point2D
import it.unibo.party.model.board.BoardBox.BoardBox.{EmptyBox, FullBox}
import it.unibo.party.model.items.*
import it.unibo.party.model.player.{Pawn, Pocket}
import it.unibo.party.model.items.CollectableOperations.collectibleBy

object BoardBox:
  enum BoardBox:
    case EmptyBox
    case FullBox(item: Collectable)
    
  extension(b: BoardBox)
    def isEmpty: Boolean = b match 
      case EmptyBox => true
      case _ => false
      
    def tryAcquireItem(pocket: Pocket): Option[Collectable] = b match 
      case EmptyBox => Option.empty
      case FullBox(item) => item.collectibleBy(pocket.getAll)


      
    




