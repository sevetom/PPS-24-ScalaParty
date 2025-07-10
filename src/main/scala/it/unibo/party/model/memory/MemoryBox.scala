package it.unibo.party.model.memory

import it.unibo.party.geometry.Point2D

enum BoxState:
  case Hide, Show

trait MemoryBox:
  def pos: Point2D[Int]
  def state: BoxState

  def isHide: Boolean = state == BoxState.Hide
  def isShow: Boolean = state == BoxState.Show

  def tryShow: MemoryBox =
    if isHide then MemoryBox(pos, BoxState.Show) else this

  def hide: MemoryBox = MemoryBox(pos, BoxState.Hide)
  def show: MemoryBox = MemoryBox(pos, BoxState.Show)

object MemoryBox:
  def apply(pos: Point2D[Int], state: BoxState = BoxState.Hide): MemoryBox =
    MemoryBoxImpl(pos, state)

  private case class MemoryBoxImpl(pos: Point2D[Int], state: BoxState) extends MemoryBox
