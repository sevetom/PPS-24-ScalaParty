package it.unibo.party.view.components

import it.unibo.party.model.memory.Figure
import it.unibo.party.view.utils.Svg.loadSvgPath
import scalafx.scene.paint.Color
import scalafx.scene.shape.SVGPath

object MemoryItems:

  def figureIcon(figure: Figure, scale: Double = 1.0): SVGPath =
      new SVGPath:
        content = loadSvgPath(s"./../resources/svg/${figure.toString.toLowerCase}.svg", figure.toString.toLowerCase)
        fill = figure match
          case Figure.Square   => Color.ForestGreen
          case Figure.Triangle => Color.Coral
          case Figure.Star     => Color.Gold
          case Figure.Pentagon => Color.SaddleBrown
          case Figure.Hexagon  => Color.MediumSlateBlue
          case Figure.Diamond  => Color.Tomato
          case Figure.Heart   => Color.Red
          case Figure.Circle   => Color.Cyan
        scaleX = scale
        scaleY = scale