package it.unibo.party.view.components

import it.unibo.party.geometry.Point2D
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.Collectable.{Monad, Rung}
import it.unibo.party.view.components.Items.*
import it.unibo.party.view.utils.Svg.loadSvgPath
import scalafx.geometry.Pos
import scalafx.scene.layout.{FlowPane, GridPane, HBox, Pane, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle, SVGPath}

object Board:

  def pawn(color: Color, scale: Double): SVGPath = new SVGPath:
    styleClass += "pawn"
    content = loadSvgPath("./../resources/svg/pawn.svg", "pawn")
    fill = color
    scaleX = scale
    scaleY = scale

  private def boardBox(size: Int): Rectangle = new Rectangle:
    styleClass += "board-box"
    width = size
    height = size


  def apply(
             board: Set[Point2D[Int]],
             playersPositions: Map[Int, Point2D[Int]],
             itemsPositions: Map[Point2D[Int], Collectable],
             gridSize: Int = 10
           ): Pane =

    val playersMap: Map[Point2D[Int], Seq[Int]] =
      playersPositions.groupBy(_._2).map((_, _) match
        case (point, players) => point -> players.keys.toSeq)
    
    val itemsMap: Map[Point2D[Int], Seq[Collectable]] =
      itemsPositions.groupBy(_._1).map ((_, _) match
        case (point, items) => point -> items.values.toSeq)

    new GridPane:
      styleClass += "board"

      board.foreach(
        point =>
          val stack = new StackPane:
            styleClass += "board-box-stack"
            children += boardBox(48)
            children += new FlowPane:
              styleClass += "board-box-content"
              val players: Seq[Int] = playersMap.getOrElse(point, Seq.empty)
              val items: Seq[Collectable] = itemsMap.getOrElse(point, Seq.empty)
              val numChildren: Int = players.size + items.size
              items.foreach(
                item =>
                  val itemShape = (item, numChildren) match
                    case (Monad(), _) => monad(10)
                    case (Rung(_), 1) => rung(1.5)
                    case (Rung(_), _) => rung(1)
                  children += itemShape
              )
              players.foreach(
                playerId =>
                  val playerColor = playerId match
                    case 0 => Color.web("#02838C")
                    case 1 => Color.web("#BA0013")
                    case 2 => Color.LightGreen
                    case 3 => Color.LightBlue
                  if numChildren > 1 then
                    children += pawn(playerColor, 1)
                  else
                    children += pawn(playerColor, 1.5)
                )

          add(stack, point.x, point.y)
      )

    
  
    
