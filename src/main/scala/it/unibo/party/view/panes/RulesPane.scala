package it.unibo.party.view.panes

import scalafx.scene.layout.{Pane, StackPane, VBox}

import scala.io.Source
import play.api.libs.json.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label, ScrollPane}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, Text}

object RulesPane:
  /**
   * Creates a pane to display the rules of the game.
   *
   * @return a Pane containing the rules of the game
   */
  def apply(jsonPath: String, onClose: () => Unit): Pane =
    val rulesData: Map[String, (String, String)] =
      val source = Source.fromFile(jsonPath)
      val jsonString = try source.mkString finally source.close()
      val json = Json.parse(jsonString)
      json.as[Map[String, JsValue]].map:
        case (key, value) =>
          val title = (value \ "title").as[String]
          val rules = (value \ "rules").as[String]
          key -> (title, rules)

    val closeButton = new Button("✖"):
      onAction = _ => onClose()

    val scrollPaneContent: VBox = new VBox(15):
      padding = Insets(20)
      alignment = Pos.TopLeft

      rulesData.foreach:
        case (_, (title, rules)) =>
          children ++= Seq(
            new Text(title):
              font = Font.font("Poppins", 20)
              fill = Color.White
              style = "-fx-font-weight: bold;"
            ,
            new Text(rules):
              font = Font.font("Poppins", 15)
              fill = Color.White
              wrappingWidth = 950
          )

    val scrollPane: ScrollPane = new ScrollPane:
      content = scrollPaneContent
      fitToWidth = true

    val layout: VBox = new VBox(10):
      padding = Insets(10)
      alignment = Pos.TopRight
      children = Seq(closeButton, scrollPane)

    new StackPane:
      children.add(layout)
      style = "-fx-background-color: rgba(0, 0, 0, 0.5);"

