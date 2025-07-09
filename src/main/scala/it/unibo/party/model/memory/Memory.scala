package it.unibo.party.model.memory

import it.unibo.party.geometry.Point2D

trait Memory:
  def layout: Map[Figure, (Point2D[Int], Point2D[Int])]

  def isEmpty: Boolean = layout.isEmpty

object Memory:
  def apply(data: Map[Figure, (Point2D[Int], Point2D[Int])]): Memory = MemoryImpl(data)

  def empty: Memory = Memory(Map.empty)

  private case class MemoryImpl(layout: Map[Figure, (Point2D[Int], Point2D[Int])]) extends Memory
