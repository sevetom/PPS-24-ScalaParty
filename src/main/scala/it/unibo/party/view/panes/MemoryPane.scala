package it.unibo.party.view.panes

import it.unibo.party.common.MemoryState
import it.unibo.party.common.MinigamePhase.{GameOver, Playing}
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.geometry.Point2D
import it.unibo.party.model.memory.{Figure, MemoryBox}
import it.unibo.party.view.components.MemoryItems.figureIcon
import it.unibo.party.view.input.MemoryInputHandler
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Font

object MemoryPane:

  private val GRID_SIZE = 4
  private val BOX_SIZE = 100

  private def memoryBox(figure: Figure) =
    figureIcon(figure, GRID_SIZE)

  def apply(state: MemoryState, agent: PlayingAgent, onExit: () => Unit): Pane =
    new BorderPane:
      focusTraversable = true
      top = new HBox:
        styleClass += "title-label-container"
        alignment = Pos.Center
        children +=
          new Label:
            text = state.phase match
              case Playing => "Find all the matches"
              case GameOver => if agent.id == state.winner.get.id then "You Won!" else "Game Over!"
            font = Font("Poppins", 30)
            textFill = Color.White
      center =
        new GridPane:
          styleClass += "memory-grid"
          hgap = 10
          vgap = 10
          padding = Insets(20)
          alignment = Pos.Center

          val allBoxes: Seq[MemoryBox] = state.game.layout.values.flatMap(p => List(p._1, p._2)).toList

          for (x <- 0 until GRID_SIZE; y <- 0 until GRID_SIZE) do
            val currentPos = Point2D(x, y)
            val boxOnGrid = allBoxes.find(_.pos == currentPos)

            val isPermanentlyShown = boxOnGrid.exists(_.isShow)
            val isFirstSelection = state.firstSelection.contains(currentPos)
            val isMismatched = state.mismatchedPair.exists(p => p._1 == currentPos || p._2 == currentPos)
            val isShown = isPermanentlyShown || isFirstSelection || isMismatched

            val cardPane =
              if isShown && boxOnGrid.isDefined then
                val figure = state.game.layout
                  .find((_, pair) => pair._1.pos == currentPos || pair._2.pos == currentPos)
                  .map(_._1)
                  .get
                new StackPane:
                  children = Seq(
                    new Rectangle:
                      width = BOX_SIZE
                      height = BOX_SIZE
                      styleClass += "memory-box"
                    ,
                    figureIcon(figure, 2)
                    )
              else
                new StackPane:
                  onMouseClicked = _ =>
                    MemoryInputHandler.handleCardClick(currentPos, state.phase, agent)

                  children = new Rectangle:
                    width = BOX_SIZE
                    height = BOX_SIZE
                    styleClass += "memory-box-covered"
            add(cardPane, x, y)

      right = new VBox:
        styleClass += "side-container"
        children = new VBox:
          styleClass += "side-box"
          children += new Button("EXIT"):
            disable = state.phase != GameOver
            styleClass += "exit-button"
            onAction = _ => onExit()