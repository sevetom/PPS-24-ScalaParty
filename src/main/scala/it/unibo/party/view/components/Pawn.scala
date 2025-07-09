package it.unibo.party.view.components

import it.unibo.party.view.utils.Svg.loadSvgPath
import scalafx.scene.paint.Color
import scalafx.scene.shape.SVGPath

object Pawn:
  def pawn(color: Color, scale: Double): SVGPath = new SVGPath:
    styleClass += "pawn"
    content = loadSvgPath("./../resources/svg/pawn.svg", "pawn")
    fill = color
    scaleX = scale
    scaleY = scale
