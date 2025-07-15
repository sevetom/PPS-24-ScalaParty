package it.unibo.party.model.memory

import it.unibo.party.geometry.Point2D

import scala.util.Random

trait Memory:
  /** The layout of the memory game, mapping each Figure to a pair of MemoryBoxes.
   * Each MemoryBox contains its position and state (Hide or Show).
   */
  def layout: Map[Figure, (MemoryBox, MemoryBox)]

  /**
   * True if the memory game is empty, meaning there are no pairs of MemoryBoxes.
   */
  def isEmpty: Boolean = layout.isEmpty

  /**
   * Checks if the given couple of MemoryBoxes matches any pair in the layout.
   * If a match is found, it updates the state of the boxes to Show.
   *
   * @param couple A tuple containing two MemoryBoxes to check.
   * @return A tuple containing a Boolean indicating if a match was found and the updated Memory.
   * @throws IllegalStateException if at least one box in the couple is already revealed.
   */
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

  /**
   * Generates a new Memory game layout with a shuffled set of Figures
   * @return A new Memory instance with a randomized layout.
   */
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

  /**
   * Checks if the memory game is over, meaning all pairs of MemoryBoxes
   * @return True if all MemoryBoxes are shown, false otherwise.
   */
  def isOver: Boolean =
    layout.values.forall:
      case (box1, box2) => box1.isShow && box2.isShow

object Memory:
  /**
   * Creates a Memory instance with the provided layout.
   *
   * @param data A map of Figure to a pair of MemoryBoxes representing the layout.
   * @return A Memory instance with the specified layout.
   */
  def apply(data: Map[Figure, (MemoryBox, MemoryBox)]): Memory = MemoryImpl(data)

  /**
   * Creates an empty Memory instance with no layout.
   *
   * @return An empty Memory instance.
   */
  def empty: Memory = Memory(Map.empty)

  private case class MemoryImpl(layout: Map[Figure, (MemoryBox, MemoryBox)]) extends Memory
