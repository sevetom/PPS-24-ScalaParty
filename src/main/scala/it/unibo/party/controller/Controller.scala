package it.unibo.party.controller

import it.unibo.party.common.{MinigameState, PartyPhase, Player, State}
import it.unibo.party.controller.Moves.*
import it.unibo.party.controller.pubsub.{Publisher, Subscriber}

import scala.util.Random

enum Minigame:
  case Party, Maze, Memory, Puzzle

trait Controller:
  /**
   * Adds a view listener to the controller that will be notified of state changes.
   * @param listener The listener to be added.
   */
  def addViewListener(listener: Subscriber[State]): Unit

  /**
   * Adds a move listener to the controller that will be notified of state changes.
   * @param listener The listener to be added.
   */
  def addMoveListener(listener: Subscriber[State]): Unit
  /**
   * Handles a move, updates the state accordingly and publishes the new state.
   * @param move The move to be handled.
   */
  def handleMove(move: Move): Unit


object Controller:

  def apply(miniControllers: Map[Minigame, MiniController]): Controller = ControllerImpl(miniControllers)

  private class ControllerImpl(private var miniControllers: Map[Minigame, MiniController]) extends Controller:
    private var statePublisher: Publisher[State] = Publisher(List.empty)
    private var currentMinigame: Minigame = Minigame.Party
    private var minigameIndex: Int = 0

    override def addViewListener(listener: Subscriber[State]): Unit =
      statePublisher = statePublisher.subscribeFirst(listener)

    override def addMoveListener(listener: Subscriber[State]): Unit =
      statePublisher = statePublisher.subscribeLast(listener)

    override def handleMove(move: Move): Unit = 
      var minigameKey = Minigame.Party
      move match
        case _: StartMove =>
          miniControllers = miniControllers.updatedWith(minigameKey)(_.map(_.start()))
        case PartyMove.Resume =>
          miniControllers = miniControllers.updatedWith(minigameKey)(_.map:
            case controller: PartyController =>
              controller.doubleRollNextTurn(
                Player(miniControllers(currentMinigame).state match
                  case s: MinigameState => s.winner.get.id
                )
              )
          )
          miniControllers = miniControllers.updatedWith(minigameKey)(_.map(_.handleMove(move)))
        case _ =>
          minigameKey = move match
              case _: PartyMove => Minigame.Party
              case _: MazeMove => Minigame.Maze
              case _: MemoryMove => Minigame.Memory
              case _: PuzzleMove => Minigame.Puzzle
          miniControllers = miniControllers.updatedWith(minigameKey)(_.map(_.handleMove(move)))
  
          if miniControllers(Minigame.Party).state.phase == PartyPhase.PlayingMinigame && currentMinigame == Minigame.Party then
            minigameKey = minigameIndex match
              case 0 => Minigame.Maze
              case 1 => Minigame.Memory
              case 2 => Minigame.Puzzle
            minigameIndex += 1
            minigameIndex = if minigameIndex >= miniControllers.size - 1 then 0 else minigameIndex
            miniControllers = miniControllers.updatedWith(minigameKey)(_.map(_.start()))

      currentMinigame = minigameKey
      statePublisher.publish(miniControllers(minigameKey).state)











  