package it.unibo.party.model

import it.unibo.party.model.GameBoard.BoardBox.EmptyBox

object GameBoard {
  
  enum BoardBox:
    case EmptyBox
    
  extension(b: BoardBox)
    def isEmpty: Boolean = b match 
      case EmptyBox => true
      case _ => false
    



}
