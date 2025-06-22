package it.unibo.party.controller

trait OpponentLogic:
  def update(gamePhase: GamePhase): Boolean

object OpponentLogic:
  def apply(): OpponentLogic = OpponentLogicImpl()

  private case class OpponentLogicImpl() extends OpponentLogic:
    override def update(gamePhase: GamePhase): Boolean =
      gamePhase match
        case GamePhase.INITIAL_ROLL => true
        case GamePhase.PLAYERS_TURN => true
        case GamePhase.PLAYER_MOVING => true
        case GamePhase.PLAYING_MINIGAME => true
        case GamePhase.GAME_OVER => true
        case _ => false // Handle other phases as needed
