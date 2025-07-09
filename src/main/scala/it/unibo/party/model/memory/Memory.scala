package it.unibo.party.model.memory

import it.unibo.party.geometry.Point2D

trait Memory:
  def isEmpty: Boolean = true

object Memory:
  def apply(data: Map[Figure, (Point2D[Int], Point2D[Int])]): Memory = MemoryImpl(data)

  def empty: Memory = Memory(Map.empty)

  private case class MemoryImpl(map: Map[Figure, (Point2D[Int], Point2D[Int])]) extends Memory
