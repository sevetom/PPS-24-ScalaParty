package it.unibo.party.model.maze

import it.unibo.party.geometry.Direction
import it.unibo.party.model.maze.MazeBuilder.DSL.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class MazeGameTest extends AnyFlatSpec:
  val emptyMazeGame: MazeGame = MazeGame.empty
  val startingPlayerPosition: MazePosition.MazePosition = MazePosition(1, 0)
  val gameWithMaze: MazeGame = MazeGame(
    startingPlayerPosition,
    Maze(3, 3):
      W | E | W
      W | * | W
      W | X | W
  )

  "A MazeGame" should "generate a new maze and place the player at the entry" in:
    val generatedGame = emptyMazeGame.generateMaze()
    generatedGame.maze.get(generatedGame.player) shouldBe Some(MazeTile.Entry)

  it should "move the player in the specified direction if the move is valid" in:
    val movedGame = gameWithMaze.movePlayer(Direction.Down)
    movedGame.player shouldBe startingPlayerPosition + Direction.Down

  it should "not move the player if the direction is invalid" in :
    val movedGame = gameWithMaze.movePlayer(Direction.Right)
    movedGame.player shouldBe startingPlayerPosition

  it should "recognize if the player has not reached the exit" in :
    gameWithMaze.reachedEnd() shouldBe false

  it should "recognize if the player has reached the exit" in:
    val gameAtExit = gameWithMaze.movePlayer(Direction.Down).movePlayer(Direction.Down)
    gameAtExit.reachedEnd() shouldBe true
