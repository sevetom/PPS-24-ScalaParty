package it.unibo.party.view.utils

import scala.xml.XML

object Svg:
  def loadSvgPath(resourcePath: String, elementId: String): String =
    val svgXml = XML.load(getClass.getResourceAsStream(resourcePath))
    (svgXml \\ "path")
      .find(node => (node \ "@id").text == elementId)
      .map(node => (node \ "@d").text.trim)
      .getOrElse(
        throw new RuntimeException(s"Path id='$elementId' non trovato in $resourcePath")
      )

