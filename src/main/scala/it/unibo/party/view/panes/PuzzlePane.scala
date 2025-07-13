package it.unibo.party.view.panes

import it.unibo.party.common.PuzzleState
import it.unibo.party.controller.Moves.PuzzleMove.*
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.geometry.Point2D
import scalafx.scene.control.Button
import scalafx.scene.layout.{BackgroundFill, BorderPane, FlowPane, GridPane, Pane}
import scalafx.scene.paint.Color

object PuzzlePane:

  def apply(onFinish: () => Unit, state: PuzzleState, playingAgent: PlayingAgent): Pane =
    val emptyId = -1
    val grid = (for
      y <- 0 until state.height
      x <- 0 until state.width
    yield (x, y)).map(p => (p, emptyId)).toMap ++ state.currentSolution.flatMap( _ match
      case (pieceId, piece) => piece.map(pos => ((pos.x, pos.y), pieceId)
    ))
    new BorderPane:
      onMouseClicked = _ => playingAgent.makeMove(RemovePiece)
      center = new GridPane:
        alignment = scalafx.geometry.Pos.Center
        grid.foreach(_ match
          case ((x, y), pieceId) if pieceId == emptyId =>
            add(new Button:
              id = s"piece-empty"
              styleClass += "piece-button"
              minWidth = 50
              minHeight = 50
              onAction = _ => playingAgent.makeMove(PlacePiece(x, y))
            , x, y)
          case ((x, y), pieceId) =>
            add(new Button:
              id = s"piece-$pieceId"
              styleClass += "piece-button"
              minWidth = 50 
              minHeight = 50
              onAction = _ => playingAgent.makeMove(SelectPiece(pieceId))
            , x, y)
        )

      bottom = new FlowPane:
        alignment = scalafx.geometry.Pos.Center
        hgap = 4
        state.nonPlacedPieces
          .map(_ match
            case (pieceId, piece) => (pieceId, piece.map(pos => Point2D(pos.x - piece.map(_.x).min, pos.y - piece.map(_.y).min))
          ))
          .foreach(_ match
        case (pieceId, piece) =>
            children.add(
              new GridPane:
                alignment = scalafx.geometry.Pos.Center
                piece.foreach(pos =>
                  add(
                    new Button:
                      id = s"piece-$pieceId"
                      styleClass += "piece-button"
                      minWidth = 50
                      minHeight = 50
                      onAction = _ => playingAgent.makeMove(SelectPiece(pieceId))
                    , pos.x, pos.y
                  )
            )
            ))