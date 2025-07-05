package it.unibo.party.controller

import it.unibo.party.common.{PartyPhase, PartyState, Player, State}
import it.unibo.party.controller.Moves.*
import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import it.unibo.party.model.partyGame.PartyGame

import scala.util.Random

enum Minigame:
  case Party, Maze, Memory, Puzzle

trait Controller:
  def addViewListener(listener: Subscriber[State]): Unit
  def addMoveListener(listener: Subscriber[State]): Unit
  def handleMove(move: Move): Unit


object Controller:

  def apply(miniControllers: Map[Minigame, MiniController]): Controller = ControllerImpl(miniControllers)

  private class ControllerImpl(private var miniControllers: Map[Minigame, MiniController]) extends Controller:
    private var statePublisher: Publisher[State] = Publisher(List.empty)

    override def addViewListener(listener: Subscriber[State]): Unit =
      statePublisher = statePublisher.subscribeFirst(listener)

    override def addMoveListener(listener: Subscriber[State]): Unit =
      statePublisher = statePublisher.subscribeLast(listener)

    override def handleMove(move: Move): Unit = 
      var minigameKey = Minigame.Party
      move match
        case _: StartMove =>
          miniControllers = miniControllers.updatedWith(minigameKey)(_.map(_.start()))
        case _ =>
          minigameKey = move match
              case _: PartyMove => Minigame.Party
              case _: MazeMove => Minigame.Maze
              case _: MemoryMove => Minigame.Memory
              case _: PuzzleMove => Minigame.Puzzle
          miniControllers = miniControllers.updatedWith(minigameKey)(_.map(_.handleMove(move)))
  
          if miniControllers(Minigame.Party).state.phase == PartyPhase.PlayingMinigame then
            minigameKey = Random.nextInt(miniControllers.size - 1) match
              case 0 => Minigame.Maze
              case 1 => Minigame.Memory
              case 2 => Minigame.Puzzle
          miniControllers = miniControllers.updatedWith(minigameKey)(_.map(_.start()))
      statePublisher.publish(miniControllers(minigameKey).state)











  