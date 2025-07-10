package it.unibo.party.model.memory

import it.unibo.party.geometry.Point2D

import scala.util.Random

trait Memory:
  def layout: Map[Figure, (MemoryBox, MemoryBox)]

  def isEmpty: Boolean = layout.isEmpty

  def check(couple: (MemoryBox, MemoryBox)): (Boolean, Memory) =
    val (b1, b2) = couple
    layout.find:
      case (_, (x, y)) =>
        (x.pos == b1.pos && y.pos == b2.pos) || (x.pos == b2.pos && y.pos == b1.pos)
    match
      case Some((fig, (x, y))) =>
        if x.isShow || y.isShow then
          throw IllegalStateException("At least one box already revealed")
        val updated = layout.updated(fig,
          (x.show, y.show)
        )
        (true, Memory(updated))
      case None =>
        (false, this)

  def generate: Memory =
    val allFigures = Figure.values.toList
    val allPositions = Random.shuffle(
      (for x <- 0 until 4; y <- 0 until 4 yield Point2D(x, y)).toList
    )

    val pairs = allPositions.grouped(2).toList
    val figureMap = allFigures.zip(pairs).map:
      case (fig, List(p1, p2)) =>
        fig -> (
          MemoryBox(p1, BoxState.Hide),
          MemoryBox(p2, BoxState.Hide)
        )
      case _ => throw IllegalStateException("Invalid grid configuration.")
    .toMap

    Memory(figureMap)

object Memory:
  def apply(data: Map[Figure, (MemoryBox, MemoryBox)]): Memory = MemoryImpl(data)

  def empty: Memory = Memory(Map.empty)

  private case class MemoryImpl(layout: Map[Figure, (MemoryBox, MemoryBox)]) extends Memory
