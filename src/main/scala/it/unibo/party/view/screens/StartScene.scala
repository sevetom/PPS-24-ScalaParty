package it.unibo.party.view.screens

import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.control.Label
import scalafx.geometry.Pos
import scalafx.scene.layout.{VBox, StackPane}
import scalafx.scene.text.Font
import scalafx.Includes._

class StartScene extends Scene:
  stylesheets += getClass.getResource("./../style/commonStyle.css").toExternalForm
  stylesheets += getClass.getResource("./../style/startStyle.css").toExternalForm
  fill = scalafx.scene.paint.Color.White
  root = new StackPane {
    children = Seq(
      new VBox {
        spacing = 20
        alignment = Pos.Center
        children = Seq(
          new Label("SCALA PARTY") {
            font = Font("Poppins", 50)
          },
          new Button("START") {
            font = Font("Poppins", 30)
            onAction = _ => println("Start button pressed")
          }
        )
      }
    )
  }
  

