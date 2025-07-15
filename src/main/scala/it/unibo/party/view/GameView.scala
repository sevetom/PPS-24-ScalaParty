package it.unibo.party.view

import it.unibo.party.common.*
import it.unibo.party.common.PartyPhase.GameOver
import it.unibo.party.common.state.MazeState
import it.unibo.party.controller.Moves.{PartyMove, StartMove}
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.controller.pubsub.Subscriber
import it.unibo.party.view.panes.{EndPane, MemoryPane, MinigamePane, PartyPane, StartPane}
import it.unibo.party.view.panes.*
import scalafx.Includes.*
import it.unibo.party.view.panes.{EndPane, MinigamePane, PartyPane, StartPane, PuzzlePane}
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, StackPane}

class GameView(playingAgent: PlayingAgent) extends Subscriber[State]:
  private val container = new StackPane()
  private val commonStyleSheet = getClass.getResource("/style/commonStyle.css").toExternalForm
  private val partyStyleSheet = getClass.getResource("/style/partyStyle.css").toExternalForm
  private val boardStyleSheet = getClass.getResource("/style/boardStyle.css").toExternalForm
  private val puzzleStyleSheet = getClass.getResource("/style/puzzleStyle.css").toExternalForm
  private val mazeStyleSheet = getClass.getResource("/style/mazeStyle.css").toExternalForm
  private val memoryStyleSheet = getClass.getResource("/style/memoryStyle.css").toExternalForm
  val scene: Scene = new Scene:
    stylesheets += commonStyleSheet
    root = container

  container.children.add(StartPane(() => playingAgent.makeMove(StartMove())))

  override def notify(event: State): Unit =
    scalafx.application.Platform.runLater:
      container.children.clear()
      val pane: Pane = event match
        case e: PartyState =>
          e.phase match
            case PartyPhase.GameOver =>
              EndPane(
                playingAgent.id,
                e.currentPlayer.id,
                () => System.exit(0)
              )
            case _ =>
              scene.stylesheets = Seq(commonStyleSheet, partyStyleSheet, boardStyleSheet)
              PartyPane(e, playingAgent)
        case e: MinigameState =>
          e match
            case maze: MazeState =>
              scene.stylesheets = Seq(commonStyleSheet, mazeStyleSheet)
              MazePane(maze, playingAgent, () => playingAgent.makeMove(PartyMove.Resume))
            case memory: MemoryState =>
              scene.stylesheets = Seq(commonStyleSheet, memoryStyleSheet)
              MemoryPane(memory, playingAgent, () => playingAgent.makeMove(PartyMove.Resume))
            case puzzle: PuzzleState =>
              scene.stylesheets = Seq(commonStyleSheet, puzzleStyleSheet)
              PuzzlePane(() => playingAgent.makeMove(PartyMove.Resume), puzzle, playingAgent)
            case _ => MinigamePane(() => playingAgent.makeMove(PartyMove.Resume))
      container.children.add(pane)
