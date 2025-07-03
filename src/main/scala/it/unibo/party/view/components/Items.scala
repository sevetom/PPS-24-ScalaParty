package it.unibo.party.view.components

import it.unibo.party.view.utils.Svg.loadSvgPath
import scalafx.geometry.Insets
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, SVGPath}

object Items:
  def monad(radiusVal: Int = 8): Circle = new Circle:
    margin = Insets(5)
    radius = radiusVal
    fill = Color.MediumOrchid


  def rung(scale: Double) : SVGPath = new SVGPath:
    styleClass += "pawn"
    content = loadSvgPath("./../resources/svg/rung.svg", "rung")
    fill = Color.web("DB7500")
    rotate = 45
    scaleX = scale
    scaleY = scale
