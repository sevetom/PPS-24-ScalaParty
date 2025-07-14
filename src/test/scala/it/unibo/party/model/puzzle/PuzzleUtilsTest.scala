package it.unibo.party.model.puzzle

import it.unibo.party.model.puzzle.PuzzlePosition.PuzzlePosition
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PuzzleUtilsTest extends AnyFlatSpec:
  val width = 4
  val height = 4

  "A Solution" should "be correctly validated when valid" in:
    val validSolution: Set[Set[PuzzlePosition]] = Set(
      Set(PuzzlePosition(0, 0), PuzzlePosition(0, 1)),
      Set(PuzzlePosition(1, 0), PuzzlePosition(1, 1), PuzzlePosition(2, 1), PuzzlePosition(3, 1)),
      Set(PuzzlePosition(2, 0), PuzzlePosition(3, 0)),
      Set(PuzzlePosition(0, 2), PuzzlePosition(1, 2), PuzzlePosition(0, 3), PuzzlePosition(1, 3)),
      Set(PuzzlePosition(2, 2), PuzzlePosition(3, 2), PuzzlePosition(2, 3), PuzzlePosition(3, 3))
    )
    PuzzleUtils.isValidSolution(validSolution, width, height) shouldBe true

  it should "not be correctly validated when incomplete" in:
    val incompleteSolution: Set[Set[PuzzlePosition]] = Set(
      Set(PuzzlePosition(1, 0), PuzzlePosition(1, 1), PuzzlePosition(2, 1), PuzzlePosition(3, 1)),
      Set(PuzzlePosition(2, 0), PuzzlePosition(3, 0)),
      Set(PuzzlePosition(0, 2), PuzzlePosition(1, 2), PuzzlePosition(0, 3), PuzzlePosition(1, 3)),
      Set(PuzzlePosition(2, 2), PuzzlePosition(3, 2), PuzzlePosition(2, 3), PuzzlePosition(3, 3))
    )
    PuzzleUtils.isValidSolution(incompleteSolution, width, height) shouldBe false

  it should "not be correctly validated when blocks are overlapping" in:
    val overlappingSolution: Set[Set[PuzzlePosition]] = Set(
      Set(PuzzlePosition(0, 0), PuzzlePosition(0, 1)),
      Set(PuzzlePosition(1, 0), PuzzlePosition(1, 1), PuzzlePosition(2, 1), PuzzlePosition(3, 1)),
      Set(PuzzlePosition(2, 0), PuzzlePosition(3, 0), PuzzlePosition(3, 1)),
      Set(PuzzlePosition(0, 2), PuzzlePosition(1, 2), PuzzlePosition(0, 3), PuzzlePosition(1, 3)),
      Set(PuzzlePosition(2, 2), PuzzlePosition(3, 2), PuzzlePosition(2, 3), PuzzlePosition(3, 3))
    )
    PuzzleUtils.isValidSolution(overlappingSolution, width, height) shouldBe false

  it should "not be correctly validated when contains invalid pieces" in:
    val invalidSolution: Set[Set[PuzzlePosition]] = Set(
      Set(PuzzlePosition(0, 0), PuzzlePosition(0, 1), PuzzlePosition(3, 3)),
      Set(PuzzlePosition(1, 0), PuzzlePosition(1, 1), PuzzlePosition(2, 1), PuzzlePosition(3, 1)),
      Set(PuzzlePosition(2, 0), PuzzlePosition(3, 0)),
      Set(PuzzlePosition(0, 2), PuzzlePosition(1, 2), PuzzlePosition(0, 3), PuzzlePosition(1, 3)),
      Set(PuzzlePosition(2, 2), PuzzlePosition(3, 2), PuzzlePosition(2, 3))
    )
    PuzzleUtils.isValidSolution(invalidSolution, width, height) shouldBe false

  it should "not be correctly validated when contains out of bounds pieces" in:
    val outOfBoundsSolution: Set[Set[PuzzlePosition]] = Set(
      Set(PuzzlePosition(-1, 18)),
      Set(PuzzlePosition(0, 0), PuzzlePosition(0, 1)),
      Set(PuzzlePosition(1, 0), PuzzlePosition(1, 1), PuzzlePosition(2, 1), PuzzlePosition(3, 1)),
      Set(PuzzlePosition(2, 0), PuzzlePosition(3, 0)),
      Set(PuzzlePosition(0, 2), PuzzlePosition(1, 2), PuzzlePosition(0, 3), PuzzlePosition(1, 3)),
      Set(PuzzlePosition(2, 2), PuzzlePosition(3, 2), PuzzlePosition(2, 3), PuzzlePosition(3, 3))
    )
    PuzzleUtils.isValidSolution(outOfBoundsSolution, width, height) shouldBe false

