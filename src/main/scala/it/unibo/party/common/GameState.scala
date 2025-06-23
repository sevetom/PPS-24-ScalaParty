package it.unibo.party.common

import it.unibo.party.geometry.Point2D
import it.unibo.party.model.items.Collectable


object GameState:
  
  enum GamePhase:
    case GAME_START, DICE_ROLL, PLAYER_MOVING, PLAYING_MINIGAME
    
  case class GameState(  
    phase: GamePhase,
    currentPlayer: Int,
    board: Seq[Point2D[Int]],
    playersPositions: Seq[(Int, Point2D[Int])],
    itemsPositions: Seq[(Collectable, Point2D[Int])],
  )