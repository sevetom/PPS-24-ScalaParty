package it.unibo.party.model.partyGame

import it.unibo.party.geometry.Direction
import it.unibo.party.model.board.BoardBox.BoardBox
import it.unibo.party.model.board.GameBoard.BoardPosition.BoardPosition
import it.unibo.party.model.board.GameBoard.GameBoard
import it.unibo.party.model.items.{Collectable, CollectableType}
import it.unibo.party.model.partyGame
import it.unibo.party.model.partyGame.MovementManager.movePawn
import it.unibo.party.model.player.Pocket

private val monadsOnBoard = 10
private val rungPrice = 5

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
      pg.board.pawns.get(id).map(_.position).fold(MovementResult.InvalidMove: MovementResult)
        (start =>
          (1 to steps).foldLeft(Option(pg -> start))
            ((acc, _) =>
              acc.flatMap:
                (game, currentPos) =>
                  val nextPos = currentPos + direction
                  game.board.board.get(nextPos)
                    .flatMap(_ => game.movePawn(id, nextPos) match
                      case MovementResult.Moved(updatedGame) => Some(updatedGame -> nextPos)
                      case _ => None
                    )
            ).map(_._1)
            .map(MovementResult.Moved.apply)
            .getOrElse(MovementResult.InvalidMove)
        )
    
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

    def regenerateBoard: PartyGame =
      var newBoard = pg.board.clearItems
      newBoard = newBoard.addRandomItems(CollectableType.MonadType, monadsOnBoard)
      newBoard = newBoard.addRandomItem(Collectable.Rung(rungPrice))
      PartyGame(newBoard)(using pg.dice)

    def regenerateMonads: PartyGame =
      var newBoard = pg.board.clearItems
      newBoard = newBoard.addRandomItems(CollectableType.MonadType, monadsOnBoard)
      PartyGame(newBoard)(using pg.dice)

