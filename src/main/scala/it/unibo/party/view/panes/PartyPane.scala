package it.unibo.party.view.panes

import it.unibo.party.common.GameState
import it.unibo.party.controller.PlayingAgent
import it.unibo.party.view.components.Board
import it.unibo.party.view.input.InputHandler
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Label
import scalafx.scene.input.KeyCode
import scalafx.scene.layout.{BorderPane, GridPane, HBox, Pane, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle}
import scalafx.scene.text.Font
import scalafx.scene.input.InputIncludes.jfxKeyEvent2sfx

object PartyPane:

  def apply(state: GameState , playingAgent: PlayingAgent): Pane =
    new BorderPane {
      padding = Insets(20)

      // ---------- Top title ----------
      top = new Label("Scala Party") {
        font = Font("Cocogose", 40)
        textFill = Color.White
        BorderPane.setAlignment(this, Pos.TopLeft)
      }

      // ---------- Center board ----------
      center = Board(
        state.board,
        state.playersPositions,
        state.itemsPositions
      )

      // ---------- Left UI ----------
      left = new VBox {
        minWidth = 300
        padding = Insets(44, 24, 44, 24)
        spacing = 20
        alignment = Pos.TopCenter

        children = Seq(
          new VBox {
            style = "-fx-background-color: #006d6d; -fx-background-radius: 10;"
            padding = Insets(15)
            spacing = 10
            children = Seq(
              new Label("Your pocket") {
                textFill = Color.White
                font = Font("Arial", 16)
              },
              new HBox {
                spacing = 10
                alignment = Pos.Center
                children = Seq(
                  labelValue("3", Color.MediumOrchid),
                  labelValue("0", Color.Orange)
                )
              }
            )
          },
          new VBox {
            style = "-fx-background-color: #6d0015; -fx-background-radius: 10;"
            padding = Insets(10)
            spacing = 8
            children = Seq(
              new Label("Enemy's pocket") {
                textFill = Color.White
                font = Font("Arial", 16)
              },
              new HBox {
                spacing = 10
                alignment = Pos.Center
                children = Seq(
                  labelValue("0", Color.MediumOrchid),
                  labelValue("0", Color.Orange)
                )
              }
            )
          }
        )
      }

      // ---------- Right UI ----------
      right = new VBox {
        minWidth = 300
        padding = Insets(44, 24, 44, 24)
        spacing = 20
        alignment = Pos.TopCenter
        children = Seq(
          new VBox {
            style = "-fx-background-color: #3d3d3d; -fx-background-radius: 10;"
            padding = Insets(10)
            spacing = 10
            alignment = Pos.Center
            children = Seq(
              new Label("Rung Price") {
                textFill = Color.White
                font = Font("Arial", 16)
              },
              new Label("5") {
                textFill = Color.White
                font = Font("Arial", 30)
              },
              new Circle {
                radius = 8
                fill = Color.MediumOrchid
              }
            )
          },
          new VBox {
            style = "-fx-background-color: #6b4413; -fx-background-radius: 10;"
            padding = Insets(10)
            spacing = 10
            alignment = Pos.Center
            children = Seq(
              new Label("🎲 Roll") {
                textFill = Color.White
                font = Font("Arial", 16)
              },
              new Label("6") {
                textFill = Color.White
                font = Font("Arial", 30)
              }
            )
          }
        )
      }

      def labelValue(text: String, color: Color): Label = new Label(text) {
        textFill = color
        font = Font("Arial", 40)
      }

      // ---------- Input handling ----------


      onKeyPressed = (event) => {
        InputHandler(event, state, playingAgent)
      }

      focusTraversable = true


    }
