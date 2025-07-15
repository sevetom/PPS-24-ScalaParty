package it.unibo.party.common

/**
 * Base trait for all phases in the game.
 */
trait Phase

/**
 * Enum representing the different phases of the party game.
 */
enum PartyPhase extends Phase:
  case StartingRoll, DiceRoll, PlayerMoving, WaitingMinigame, PlayingMinigame, GameOver
  
enum MinigamePhase extends Phase:
  case Playing, GameOver
