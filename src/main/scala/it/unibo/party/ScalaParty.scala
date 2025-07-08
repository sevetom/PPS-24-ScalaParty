package it.unibo.party

import it.unibo.party.common.*
import it.unibo.party.controller.*
import it.unibo.party.model.board.GameBoard.GameBoard
import it.unibo.party.model.partyGame.{Dice, PartyGame}
import it.unibo.party.view.GameView
import scalafx.application.JFXApp3
import it.unibo.party.model.board.GameBoard.standardBoard

object ScalaParty extends JFXApp3:
  override def start(): Unit =
    val game = PartyGame(standardBoard())(using Dice())
    val partyController = PartyController(game, List(Player(0), Player(1)))
    val mazeController = MazeController(0, 0, MazeState(MinigamePhase.Playing, Option(Player(0))))
    val memoryController = MemoryController(0, 0, MemoryState(MinigamePhase.Playing, Option(Player(0))))
    val puzzleController = PuzzleController(0, 0, PuzzleState(MinigamePhase.Playing, Option(Player(0))))
    val controller = Controller(Map(
      Minigame.Party -> partyController,
      Minigame.Maze -> mazeController,
      Minigame.Memory -> memoryController,
      Minigame.Puzzle -> puzzleController
    ))
    val player = PlayingAgent(0, controller)
    val gameView = GameView(player)
    val opponentAgent = PlayingAgent(1, controller)
    val opponent = OpponentLogic(opponentAgent)
    controller.addMoveListener(opponent)
    controller.addViewListener(gameView)
    stage = new JFXApp3.PrimaryStage:
      title = "Scala Party"
      width = 1080
      height = 720
      resizable = false
      scene = gameView.scene
