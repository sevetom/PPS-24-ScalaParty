package it.unibo.party.view.panes

import it.unibo.party.common.MinigamePhase.GameOver
import it.unibo.party.common.PuzzleState
import it.unibo.party.controller.Moves.PuzzleMove.*
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.geometry.Point2D
import scalafx.geometry.Insets
import scalafx.scene.control.Button
import scalafx.scene.layout.{BorderPane, FlowPane, GridPane, Pane}


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
      onMouseClicked = _ => if state.phase != GameOver then playingAgent.makeMove(RemovePiece)
      center = new GridPane:
        alignment = scalafx.geometry.Pos.Center
        grid.foreach(_ match
          case ((x, y), pieceId) if pieceId == emptyId =>
            add(new Button:
              id = s"piece-empty"
              styleClass += "piece-button"
              minWidth = 70
              minHeight = 70
              onAction = _ => if state.phase != GameOver then playingAgent.makeMove(PlacePiece(x, y))
            , x, y)
          case ((x, y), pieceId) =>
            if x >= 0 && y >= 0 then
              add(new Button:
                id = s"piece-$pieceId"
                styleClass += "piece-button"
                minWidth = 70
                minHeight = 70
                onAction = _ => playingAgent.makeMove(SelectPiece(pieceId))
              , x, y)
        )

      bottom = new FlowPane:
        alignment = scalafx.geometry.Pos.Center
        hgap = 20
        padding = Insets(0, 0, 40, 0)
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
                      minWidth = 40
                      maxWidth = 40
                      minHeight = 40
                      maxHeight = 40
                      onAction = _ => if state.phase != GameOver then playingAgent.makeMove(SelectPiece(pieceId))
                    , pos.x, pos.y
                  )
            )
            ))