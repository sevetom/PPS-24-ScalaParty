package it.unibo.party.common

trait Phase

enum PartyPhase extends Phase:
  case StartingRoll, DiceRoll, PlayerMoving, PlayingMinigame, GameOver
  
enum MinigamePhase extends Phase:
  case Playing, GameOver
