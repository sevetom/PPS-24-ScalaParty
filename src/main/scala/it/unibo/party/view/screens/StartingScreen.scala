package it.unibo.party.view.screens

import java.awt.{Color, Dimension, Font}
import javax.swing.BorderFactory
import scala.swing.*
import scala.swing.BorderPanel.Position

class StartingScreen extends BorderPanel:
  layout(new BoxPanel(Orientation.Vertical) {
    // Spazio per posizionare il contenuto al centro
    border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

    // aggiungo un elemento per centrare il contenuto
    contents += Swing.VStrut(400)


    // Titolo centrato
    contents += new Label("Scala Party") {
      font = new Font("Arial", Font.BOLD, 50)
      horizontalAlignment = Alignment.Center
      xLayoutAlignment = 0.5
      yLayoutAlignment = 0.5
    }
    // Spazio tra titolo e bottone
    contents += Swing.VStrut(10)
    // Bottone di start centrato

    val startButton: Button = new Button() {
      text = "Start Game"
      font = new Font("Arial", Font.PLAIN, 20)
      background = new Color(70, 130, 180) // SteelBlue
      foreground = Color.white
      xLayoutAlignment = 0.5
      yLayoutAlignment = 0.5
    }

    startButton.peer.setContentAreaFilled(false)
    startButton.peer.setOpaque(true)

    contents += startButton
  }) = Position.Center

