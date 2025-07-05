package it.unibo.party.common

trait Phase

enum PartyPhase extends Phase:
  case StartingRoll, DiceRoll, PlayerMoving, PlayingMinigame, GameOver

enum PuzzlePhase extends Phase:
  case Playing, GameOver

enum MemoryPhase extends Phase:
  case Playing, GameOver

enum MazePhase extends Phase:
  case Playing, GameOver
