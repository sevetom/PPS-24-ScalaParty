package it.unibo.party.controller

enum GamePhase:
  case INITIAL_ROLL
  case PLAYERS_TURN
  case PLAYER_MOVING
  case PLAYING_MINIGAME
  case GAME_OVER