package it.unibo.party.model.maze

import it.unibo.party.model.maze.MazePosition.MazePosition
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MazeTest extends AnyFlatSpec:
  val maze: Maze = Maze.empty
  val pos: MazePosition = MazePosition(0, 0)
  val tile: MazeTile = MazeTile.Passage

  "A maze" should "be empty when created as such" in:
    maze.isEmpty shouldBe true
  
  it should "allow adding a new position and tile" in:
    val updatedMaze = maze + (pos, tile)
    updatedMaze.contains(pos) shouldBe true

  it should "not be empty after adding a tile" in:
    val updatedMaze = maze + (pos, tile)
    updatedMaze.isEmpty shouldBe false

  it should "retrieve a tile at a given position" in:
    val updatedMaze = maze + (pos, tile)
    updatedMaze.get(pos) shouldBe Some(tile)