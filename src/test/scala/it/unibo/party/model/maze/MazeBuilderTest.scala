package it.unibo.party.model.maze

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MazeBuilderTest extends AnyFlatSpec:

  import MazeBuilder.DSL.*

  val mazeDimensions: (Int, Int) = (5, 5)

  "MazeBuilder" should "build a maze with the correct size" in:
    val builder: MazeBuilder = MazeBuilder.construct(mazeDimensions._1, mazeDimensions._2):
      W | W | E | W | W
      W | * | * | * | W
      W | * | W | W | W
      W | * | * | * | W
      W | W | W | X | W
    val maze = builder.build()
    maze.size shouldEqual (mazeDimensions._1 * mazeDimensions._2)

  it should "place the tiles in the correct positions" in:
    val builder: MazeBuilder = MazeBuilder.construct(mazeDimensions._1, mazeDimensions._2):
      W | W | E | W | W
      W | * | * | * | W
      W | * | W | W | W
      W | * | * | * | W
      W | W | W | X | W
    val maze = builder.build()
    val exitPosition = MazePosition(3, 4)
    maze.get(exitPosition) shouldEqual Some(MazeTile.Exit)

  it should "not allow for a maze with different dimensions then required" in:
    val builder: MazeBuilder = MazeBuilder.construct(mazeDimensions._1, mazeDimensions._2):
      E | * | X
    an[IllegalArgumentException] should be thrownBy builder.build()

  it should "not allow a maze with no entry" in:
    val builder: MazeBuilder = MazeBuilder.construct(mazeDimensions._1, mazeDimensions._2):
      W | W | W | W | W
      W | * | * | * | W
      W | * | W | W | W
      W | * | * | * | W
      W | W | W | X | W
    an[IllegalArgumentException] should be thrownBy builder.build()

  it should "not allow a maze with no exit" in:
    val builder: MazeBuilder = MazeBuilder.construct(mazeDimensions._1, mazeDimensions._2):
      W | W | E | W | W
      W | * | * | * | W
      W | * | W | W | W
      W | * | * | * | W
      W | W | W | W | W
    an[IllegalArgumentException] should be thrownBy builder.build()

  it should "not allow a maze that is not solvable" in:
    val builder: MazeBuilder = MazeBuilder.construct(mazeDimensions._1, mazeDimensions._2):
      W | W | E | W | W
      W | * | * | * | W
      W | * | W | W | W
      W | * | W | W | W
      W | W | X | W | W
    an[IllegalArgumentException] should be thrownBy builder.build()

  "A maze" can "be directly built with the structure" in:
    val maze = Maze(mazeDimensions._1, mazeDimensions._2):
      W | W | E | W | W
      W | * | * | * | W
      W | * | W | W | W
      W | * | * | * | W
      W | W | W | X | W
    maze.size shouldEqual (mazeDimensions._1 * mazeDimensions._2)