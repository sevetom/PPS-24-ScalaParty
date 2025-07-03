package it.unibo.party.view.components

import it.unibo.party.common.Player
import it.unibo.party.geometry.Point2D
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.Collectable.{Monad, Rung}
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.layout.{GridPane, HBox, Pane, Priority, Region, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle, SVGPath, TriangleMesh}

object Board:

  def pawn(color: Color): SVGPath = new SVGPath {
    content = "M 71.938747,85.361188 H 52.020238 c -2.225794,0 -6.001995,0.08077 -7.847298,-1.108256 -1.91429,-1.233476 -3.138918,-3.354596 -3.249995,-5.629157 -0.107074,-2.192591 1.850974,-5.422492 2.963872,-7.350087 L 53.84607,54.023755 c 1.112898,-1.927595 2.93105,-5.238265 4.883428,-6.241832 2.025366,-1.041085 4.474623,-1.041085 6.49999,0 1.952376,1.003567 3.770528,4.314237 4.883426,6.241832 l 9.959254,17.249933 c 1.112898,1.927595 3.070946,5.157496 2.963872,7.350088 -0.111077,2.274562 -1.335706,4.395681 -3.249996,5.629157 -1.845303,1.189024 -5.621503,1.108255 -7.847297,1.108255 z"
    fill = color
    // dimensions
    scaleX = 0.4
    scaleY = 0.4
  }

  def monad: Circle = new Circle {
    radius = 8
    fill = Color.MediumOrchid
  }

  def rung: Circle = new Circle {
    radius = 8
    fill = Color.Orange
  }

  private def tile: Rectangle = new Rectangle {
    width = 40
    height = 40
    fill = Color.web("#cfcfcf")
    arcWidth = 15
    arcHeight = 15
  }

  def apply(
             board: Set[Point2D[Int]],
             playersPositions: Map[Player, Point2D[Int]],
             itemsPositions: Map[Point2D[Int], Collectable],
             gridSize: Int = 10
           ): Pane = {

    val playersMap: Map[Point2D[Int], Seq[Player]] = playersPositions.groupBy(_._2).map {
      case (point, players) => point -> players.keys.toSeq
    }
    
    val itemsMap: Map[Point2D[Int], Seq[Collectable]] = itemsPositions.groupBy(_._1).map {
      case (point, items) => point -> items.values.toSeq
    }

    new GridPane {
      hgap = 8
      vgap = 8
      alignment = Pos.Center

      board.foreach(
        point =>
          val stack = new StackPane {
            children += tile
            children += new HBox {
              alignment = Pos.Center

              itemsMap.get(point).foreach(
                list => list.foreach(
                  item =>
                    val itemShape = item match {
                      case Monad() => monad
                      case Rung(_) => rung
                    }
                    children += itemShape
                )
              )

              playersMap.get(point).foreach(
                list => list.foreach(
                  playerId =>
                    val playerColor = playerId.id match
                      case 0 => Color.MediumOrchid
                      case 1 => Color.Orange
                      case 2 => Color.LightGreen
                      case 3 => Color.LightBlue
                    children += pawn(playerColor)
                )
              )
            }
          }

          add(stack, point.x, point.y)

      )


    }
  }
    
  
    
