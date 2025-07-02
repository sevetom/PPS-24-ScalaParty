package it.unibo.party.view.components

import scalafx.geometry.Insets
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

object Items:
  def monad(radiusVal: Int = 8): Circle = new Circle {
    margin = Insets(5)
    radius = radiusVal
    fill = Color.MediumOrchid
  }

  def rung: Circle = new Circle {
    radius = 8
    fill = Color.Orange
  }
