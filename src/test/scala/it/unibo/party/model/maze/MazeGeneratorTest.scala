package it.unibo.party.model.maze

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MazeGeneratorTest extends AnyFlatSpec:
  "A MazeGenerator" should "generate a maze with the specified dimensions with no errors" in:
    val width: Int = 11
    val height: Int = 11
    val maze: Maze = MazeGenerator.NorthEastBinaryTree(width, height).generate
    maze.size shouldEqual (width * height)

  it should "not allow even dimensions" in:
    assertThrows[IllegalArgumentException]:
      MazeGenerator.NorthEastBinaryTree(10, 10)

  it should "not allow dimensions <= 2" in:
    assertThrows[IllegalArgumentException]:
      MazeGenerator.NorthEastBinaryTree(2, 2)
