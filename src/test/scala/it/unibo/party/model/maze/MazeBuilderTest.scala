package it.unibo.party.model.maze

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MazeBuilderTest extends AnyFlatSpec:

  "MazeBuilder" should "build a maze with the correct starting point" in:
    import MazeBuilder.DSL.*
    val builder = MazeBuilder.configure:
      W | W | W | W | W | E | W | W | W | W
      W | * | * | * | W | * | * | W | W | W
      W | * | W | W | W | * | W | * | W | W
      W | * | W | * | * | * | * | * | * | W
      W | * | W | W | * | W | W | W | * | W
      W | W | * | * | * | * | * | W | * | W
      W | * | * | W | W | * | W | W | * | W
      W | W | * | * | * | W | * | * | * | W
      W | * | * | W | * | W | W | W | * | W
      W | W | W | W | X | W | W | W | W | W
    val maze = builder.build()
    maze.get(MazePosition(5, 0)) shouldEqual Some(MazeTile.Entry)