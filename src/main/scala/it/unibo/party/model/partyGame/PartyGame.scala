package it.unibo.party.model.partyGame

import it.unibo.party.geometry.Direction
import it.unibo.party.model.board.BoardBox.BoardBox
import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition
import it.unibo.party.model.board.GameBoard.GameBoard
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.partyGame
import it.unibo.party.model.partyGame.MovementManager.movePawn
import it.unibo.party.model.player.Pocket

trait PartyGame:
  def board: GameBoard

  def dice: Dice

object PartyGame:
  def apply(board: GameBoard)(using dice: Dice): PartyGame = PartyGameImpl(board, dice)

  given defaultDice: Dice = Dice()

  private case class PartyGameImpl(board: GameBoard, dice: Dice) extends PartyGame

  def empty: PartyGame = PartyGameImpl(GameBoard(Map.empty, Map.empty), summon[Dice])

  given CanRoll[PartyGame] with
    extension (pg: PartyGame)
      def roll(n: Int): (PartyGame, List[Int]) =
        val (newDice, result) = pg.dice.roll(n)
        (PartyGame(pg.board)(using newDice), result)

  extension (pg: PartyGame)
    def movePlayer(id: Int, direction: Direction, steps: Int): MovementResult =
      val newPosition = pg.board.pawns.get(id) match
        case Some(pawn) =>
          val start = pawn.position
          (1 to steps).foldLeft(start)((pos, _) => pos + direction)
        case None => throw new NoSuchElementException(s"No pawn found with id $id")
      pg.movePawn(id, newPosition)
      
    def getPossibleDirections(id: Int): Set[Direction] = 
      pg.board.pawns.get(id) match
        case Some(pawn) => pg.board.availableDirections(pawn.position)
        case None => throw new NoSuchElementException(s"No pawn found with id $id")

    def getPlayersPosition: Map[Int, BoardPosition] =
      pg.board.pawns.map((id, pawn) => id -> pawn.position)

    def getBoardBoxes: Set[BoardPosition] =
      pg.board.board.keys.toSet

    def getItems: Map[BoardPosition, Collectable] =
      pg.board.board.collect:
        case (pos, BoardBox.FullBox(item)) => pos -> item

    def getPockets: Map[Int, Pocket] =
      pg.board.pawns.collect:
        case (id, pawn) => id -> pawn.pocket

