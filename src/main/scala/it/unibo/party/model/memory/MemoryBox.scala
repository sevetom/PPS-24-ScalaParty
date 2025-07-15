package it.unibo.party.model.memory

import it.unibo.party.geometry.Point2D

enum BoxState:
  case Hide, Show

sealed trait MemoryBox:
  /**
   * The position of the MemoryBox in the game grid.
   *
   * @return The Point2D representing the position of the MemoryBox.
   */
  def pos: Point2D[Int]

  /**
   * The current state of the MemoryBox, which can be either Hide or Show.
   *
   * @return The BoxState of the MemoryBox.
   */
  def state: BoxState

  private def isHide: Boolean = state == BoxState.Hide

  /**
   * Checks if the MemoryBox is currently visible.
   *
   * @return True if the box is in Show state, false otherwise.
   */
  def isShow: Boolean = state == BoxState.Show

  /**
   * Hides the MemoryBox, setting its state to Hide.
   *
   * @return A new MemoryBox with the state set to Hide.
   */
  def hide: MemoryBox = MemoryBox(pos, BoxState.Hide)

  /**
   * Sets the MemoryBox to Show state, making it visible.
   *
   * @return A new MemoryBox with the state set to Show.
   */
  def show: MemoryBox = MemoryBox(pos, BoxState.Show)

object MemoryBox:
  /**
   * Creates a new MemoryBox at the specified position with the given state.
   *
   * @param pos   The position of the MemoryBox.
   * @param state The initial state of the MemoryBox, defaulting to Hide.
   * @return A new MemoryBox instance.
   */
  def apply(pos: Point2D[Int], state: BoxState = BoxState.Hide): MemoryBox =
    MemoryBoxImpl(pos, state)

  private case class MemoryBoxImpl(pos: Point2D[Int], state: BoxState) extends MemoryBox
