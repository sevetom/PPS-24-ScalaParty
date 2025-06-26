package it.unibo.party.view

import it.unibo.party.common.GameState.GamePhase.*
import it.unibo.party.common.GameState.{GamePhase, GameState}
import it.unibo.party.geometry.Point2D
import it.unibo.party.model.items.Collectable.Monad
import it.unibo.party.view.panes.PartyPane
import it.unibo.party.view.screens.StartScene
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, StackPane}
import scalafx.scene.paint.Color
import it.unibo.party.view.panes.StartPane

import scala.swing.*
import java.awt.Dimension
import scalafx.Includes.*
import scalafx.animation.PauseTransition
import scalafx.util.Duration

class GameView():
  //private val playingAgent = PlayingAgent()
  private val container = new StackPane()
  private val commonStyleSheet = getClass.getResource("./style/commonStyle.css").toExternalForm
  private val gameStyleScheet = getClass.getResource("./style/partyStyle.css").toExternalForm
  val scene: Scene = new Scene {
    stylesheets += commonStyleSheet
    stylesheets += gameStyleScheet
    root = container
  }

  container.children.add(StartPane(() => this.update(
    GameState(
      PlayerMoving,
      0,
      Seq(
        Point2D(0, 0), Point2D(1, 0), Point2D(2, 0), Point2D(3, 0), Point2D(4, 0), Point2D(5, 0), Point2D(6, 0), Point2D(7, 0),
        Point2D(0, 1), Point2D(4, 1), Point2D(7, 1),
        Point2D(0, 2), Point2D(4, 2), Point2D(7, 2),
        Point2D(0, 3), Point2D(1, 3), Point2D(2, 3), Point2D(3, 3), Point2D(4, 3), Point2D(5, 3), Point2D(6, 3), Point2D(7, 3), Point2D(8, 3), Point2D(9, 3),
        Point2D(0, 4), Point2D(4, 4), Point2D(7, 4), Point2D(9, 4),
        Point2D(0, 5), Point2D(4, 5), Point2D(7, 5), Point2D(9, 5),
        Point2D(0, 6), Point2D(4, 6), Point2D(7, 6), Point2D(9, 6),
        Point2D(0, 7), Point2D(1, 7), Point2D(2, 7), Point2D(3, 7), Point2D(4, 7), Point2D(5, 7), Point2D(6, 7), Point2D(7, 7), Point2D(8, 7), Point2D(9, 7),
        Point2D(3, 8), Point2D(9, 8),
        Point2D(3, 9), Point2D(4, 9), Point2D(5, 9), Point2D(6, 9), Point2D(7, 9), Point2D(8, 9), Point2D(9, 9)
      ),
      Seq(
        (0, Point2D(9, 9)), (1, Point2D(9, 9))
      ),
      Seq(
        (Monad(), Point2D(0, 0)),
        (Monad(), Point2D(4, 1)),
        (Monad(), Point2D(4, 2)),
        (Monad(), Point2D(2, 3))
      )
    )
  )))

  def update(state: GameState): Unit = {
    container.children.clear()
    val pane: Pane = state.phase match
      // case GameStart => gameStartPane(state, playingAgent)
      // case PlayingMinigame => minigamePane(state, playingAgent)
      case _ => PartyPane(state/*, playingAgent*/)
    container.children.add(pane)
  }



object Main extends JFXApp3:
  override def start(): Unit =
    val gameView = new GameView()
    // publisher.subscribe(gameView)
    stage = new JFXApp3.PrimaryStage:
      title = "Scala Party"
      width = 1440
      height = 1024
      resizable = false
      scene = gameView.scene

//    val delay = new PauseTransition(Duration.apply(5000))
//    delay.onFinished = _ => {
//      // questo codice verrà eseguito dopo 5 secondi
//      gameView.update(
//        GameState(
//          GameStart,
//          0,
//          Seq.empty,
//          Seq.empty,
//          Seq.empty
//        )
//      )
//    }
//    delay.play() // fa partire il conto alla rovescia



