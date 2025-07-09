package it.unibo.party.view.panes

import it.unibo.party.common.MinigamePhase
import it.unibo.party.common.MinigamePhase.{GameOver, Playing}
import it.unibo.party.common.state.MazeState
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.model.maze.MazeTile
import it.unibo.party.view.components.Pawn.pawn
import it.unibo.party.view.input.MazeInputHandler
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{BorderPane, FlowPane, GridPane, HBox, Pane, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Box, Rectangle}
import scalafx.scene.input.InputIncludes.jfxKeyEvent2sfx
import scalafx.scene.text.Font

object MazePane:

  private def mazeBox(size: Int, tile: MazeTile): Rectangle =
    new Rectangle:
      styleClass += "maze-box"
      styleClass ++= (tile match
        case MazeTile.Wall => Seq("maze-box-wall")
        case MazeTile.Passage => Seq("maze-box-passage")
        case MazeTile.Entry => Seq("maze-box-entry")
        case MazeTile.Exit => Seq("maze-box-exit")
        )
      width = size
      height = size

  def apply(state: MazeState, agent: PlayingAgent, onExit: () => Unit): Pane =
    new BorderPane:
      top = new HBox:
        styleClass += "title-label-container"
        alignment = Pos.Center
        children +=
          new Label:
            text = state.phase match
              case Playing => "Find the exit!"
              case GameOver => if agent.id == state.winner.getOrElse(-1) then "You Won!" else "Game Over!"
            font = Font("Poppins", 30)
            textFill = Color.White
      center =
        new GridPane:
          styleClass += "maze"
          gridLinesVisible = false
          state.maze.foreach(
            (point, tile) =>
              val stack = new StackPane:
                styleClass += "maze-box-stack"
                children += mazeBox(48, tile)
                children += new FlowPane:
                  styleClass += "maze-box-content"
                  if state.playerPosition == point then
                    children += pawn(Color.web("#02838C"), 1.5)
                  if state.phase == GameOver && state.solution.contains(point) then
                    children += pawn(Color.web("#73028c"), 1)
              add(stack, point.x, point.y)
          )
      left = new VBox:
        styleClass += "side-container"
        children = new VBox:
          styleClass += "side-box"
          children += new HBox:
            children += new Label((state.timeRequired / 1000).toString + " seconds!"):
              styleClass += "side-box-title"
              font = Font("Poppins", 20)
              textFill = Color.White
      right = new VBox:
        styleClass += "side-container"
        children = new VBox:
          styleClass += "side-box"
          children += new Button("EXIT"):
            disable = state.phase != GameOver
            styleClass += "exit-button"
            onAction = _ => onExit()
      onKeyPressed = event => MazeInputHandler(event, state.phase, agent)
      focusTraversable = true
