package it.unibo.party.view

import it.unibo.party.view.screens.StartingScene
import scalafx.application.JFXApp3

import scala.swing.*
import java.awt.Dimension

object GameView extends JFXApp3:
  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      title = "Scala Party"
      width = 1440
      height = 1024
      resizable = false
      scene = new StartingScene
  

