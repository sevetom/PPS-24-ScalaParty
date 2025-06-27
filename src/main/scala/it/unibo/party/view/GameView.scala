package it.unibo.party.view

import it.unibo.party.common.GamePhase.PlayerMoving
import it.unibo.party.common.{GamePhase, GameState}
import it.unibo.party.common.GameState.*
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable.Monad
import it.unibo.party.view.panes.PartyPane
import it.unibo.party.view.screens.StartScene
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, StackPane}
import scalafx.scene.paint.Color
import it.unibo.party.view.panes.StartPane
import it.unibo.party.controller.PartyController
import it.unibo.party.controller.pubsub.Subscriber
import it.unibo.party.model.partyGame.PartyGame

import scala.swing.*
import java.awt.Dimension
import scalafx.Includes.*
import scalafx.animation.PauseTransition
import scalafx.util.Duration





class GameView(playingAgent: PlayingAgent) extends Subscriber[GameState]:
  private val container = new StackPane()
  private val commonStyleSheet = getClass.getResource("./style/commonStyle.css").toExternalForm
  private val gameStyleScheet = getClass.getResource("./style/partyStyle.css").toExternalForm
  val scene: Scene = new Scene {
    stylesheets += commonStyleSheet
    stylesheets += gameStyleScheet
    root = container
  }

  container.children.add(StartPane(() => playingAgent.controller.start()))

  override def notify(event: GameState): Unit = {
    scalafx.application.Platform.runLater {
      println(event.currentPlayer)
      container.children.clear()
      val pane: Pane = event.phase match
        // case GameStart => gameStartPane(state, playingAgent)
        // case PlayingMinigame => minigamePane(state, playingAgent)
        case _ => PartyPane(event, playingAgent)
      container.children.add(pane)
    }
  }
