package it.unibo.party.model.player

import it.unibo.party.geometry.Point2D
import it.unibo.party.model.items.Collectable
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PawnTest extends AnyFlatSpec:
  val initialPosition: Point2D[Int] = Point2D(0, 0)
  val pawn: Pawn[Point2D[Int]] = Pawn.emptyPockets(initialPosition)
  
  "A Pawn" should "allow moving to a new position" in:
    val newPosition: Point2D[Int] = Point2D(1, 1)
    val movedPawn: Pawn[Point2D[Int]] = pawn moveTo newPosition
    movedPawn.position shouldEqual newPosition

  it should "allow updating its pocket" in:
    val newPocket: Pocket = Pocket.empty.add(Collectable.Monad())
    val updatedPawn: Pawn[Point2D[Int]] = pawn withPocket newPocket
    updatedPawn.pocket shouldEqual newPocket