package it.unibo.party.view

import it.unibo.party.common.PartyPhase.{GameOver, PlayerMoving}
import it.unibo.party.common.*
import it.unibo.party.common.PartyState.*
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.controller.Moves.{PartyMove, StartMove}
import it.unibo.party.view.panes.{EndPane, MinigamePane, PartyPane, StartPane}
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, StackPane}
import it.unibo.party.controller.pubsub.Subscriber
import scalafx.Includes.*


class GameView(playingAgent: PlayingAgent) extends Subscriber[State]:
  private val container = new StackPane()
  private val commonStyleSheet = getClass.getResource("./style/commonStyle.css").toExternalForm
  private val partyStyleSheet = getClass.getResource("./style/partyStyle.css").toExternalForm
  private val boardStyleSheet = getClass.getResource("./style/boardStyle.css").toExternalForm
  val scene: Scene = new Scene {
    stylesheets += commonStyleSheet
    root = container
  }

  container.children.add(StartPane(() => playingAgent.makeMove(StartMove())))

  override def notify(event: State): Unit = {
    scalafx.application.Platform.runLater {
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
        case e: MinigameState => MinigamePane(() => playingAgent.makeMove(PartyMove.Resume))
      container.children.add(pane)
    }
  }
