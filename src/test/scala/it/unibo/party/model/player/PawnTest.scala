package it.unibo.party.model.player

import it.unibo.party.geometry.Point2D
import it.unibo.party.model.items.Collectable
import org.scalatest.flatspec.AnyFlatSpec

class PawnTest extends AnyFlatSpec:
  val initialPosition: Point2D[Int] = Point2D(0, 0)
  val pawn: Pawn[Point2D[Int]] = Pawn.emptyPockets(initialPosition)
  
  "A starting Pawn" should "have an initial position and an empty pocket" in:
    assert(pawn.position == initialPosition)
    assert(pawn.pocket.isEmpty)
  
  "A Pawn" should "allow moving to a new position" in:
    val newPosition: Point2D[Int] = Point2D(1, 1)
    val movedPawn: Pawn[Point2D[Int]] = pawn moveTo newPosition
    assert(movedPawn.position == newPosition)
    assert(movedPawn.pocket == pawn.pocket)

  it should "allow updating its pocket" in:
    val newPocket: Pocket = Pocket.empty.add(Collectable.Monad())
    val updatedPawn: Pawn[Point2D[Int]] = pawn withPocket newPocket
    assert(updatedPawn.pocket == newPocket)
    assert(updatedPawn.position == pawn.position)