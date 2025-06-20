package it.unibo.party.model

import it.unibo.party.model.GameBoard.BoardBox.EmptyBox

object GameBoard {
  type Collectable
  
  enum BoardBox:
    case EmptyBox
    case FullBox(item: Collectable)
    
  extension(b: BoardBox)
    def isEmpty: Boolean = b match 
      case EmptyBox => true
      case _ => false
      
    def tryAcquireItem: Option[Collectable] = b match 
      case EmptyBox => Option.empty
      case _ => ???
    
      
    



}
