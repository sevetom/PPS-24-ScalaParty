package it.unibo.party.model.maze

import it.unibo.party.model.maze.MazeBuilder.DSL.*
import it.unibo.party.model.maze.MazePosition.MazePosition
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MazeTest extends AnyFlatSpec:
  val emptyMaze: Maze = Maze.empty
  val genericPos: MazePosition = MazePosition(0, 0)
  val genericTile: MazeTile = MazeTile.Passage

  "A maze" should "be empty when created as such" in:
    emptyMaze.isEmpty shouldBe true
  
  it should "allow adding a new position and tile" in:
    val updatedMaze = emptyMaze + (genericPos, genericTile)
    updatedMaze.contains(genericPos) shouldBe true

  it should "not be empty after adding a tile" in:
    val updatedMaze = emptyMaze + (genericPos, genericTile)
    updatedMaze.isEmpty shouldBe false

  it should "retrieve a tile at a given position" in:
    val updatedMaze = emptyMaze + (genericPos, genericTile)
    updatedMaze.get(genericPos) shouldBe Some(genericTile)

  it should "be able to calculate a solution to a solvable maze" in:
    val width = 10
    val height = 11
    val maze = Maze(width, height):
      W | W | W | W | E | W | W | W | W | W
      W | * | * | * | * | * | * | * | * | W
      W | * | W | W | W | W | W | W | * | W
      W | * | W | * | * | * | * | W | * | W
      W | * | W | W | * | W | * | W | * | W
      W | * | * | W | * | W | * | * | * | W
      W | W | * | W | * | W | * | W | W | W
      W | * | * | W | * | W | * | W | * | W
      W | * | W | W | W | W | * | W | * | W
      W | * | * | * | * | W | * | * | * | W
      W | W | W | W | X | W | W | W | W | W
    maze.solution shouldBe defined