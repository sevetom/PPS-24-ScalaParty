package it.unibo.party

import it.unibo.party.controller.{OpponentLogic, PartyController, PlayingAgent}
import it.unibo.party.model.board.GameBoard.GameBoard
import it.unibo.party.model.partyGame.{Dice, PartyGame}
import it.unibo.party.view.GameView
import scalafx.application.JFXApp3
import it.unibo.party.model.board.GameBoard.standardBoard

object ScalaParty extends JFXApp3:
  override def start(): Unit =
    val game = PartyGame(standardBoard())(using Dice())
    val controller = PartyController(game, Seq(0, 1))
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
