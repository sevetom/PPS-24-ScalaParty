package it.unibo.party.view

import it.unibo.party.view.screens.StartingScreen

import scala.swing.*
import java.awt.Dimension

object GameView extends SimpleSwingApplication {
  def top: Frame = new MainFrame {
    title = "Scala Party"
    preferredSize = new Dimension(1440, 1024)
    resizable = false

    contents = StartingScreen()
  }
}
