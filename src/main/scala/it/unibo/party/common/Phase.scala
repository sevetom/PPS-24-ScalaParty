package it.unibo.party.common

trait Phase

enum PartyPhase extends Phase:
  case StartingRoll, DiceRoll, PlayerMoving, WaitingMinigame, PlayingMinigame, GameOver
  
enum MinigamePhase extends Phase:
  case Playing, GameOver
