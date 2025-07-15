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
  /**
   * The game board containing the pawns and items.
   * @return The current game board.
   */
  def board: GameBoard

  /**
   * The dice used for rolling in the game.
   * @return The current dice instance.
   */
  def dice: Dice

object PartyGame:
  /**
   * Creates a new PartyGame instance with the specified game board and dice.
   * @param board The game board to use.
   * @param dice The dice to use for rolling.
   * @return A new PartyGame instance.
   */
  def apply(board: GameBoard)(using dice: Dice): PartyGame = PartyGameImpl(board, dice)

  given defaultDice: Dice = Dice()

  private case class PartyGameImpl(board: GameBoard, dice: Dice) extends PartyGame

  /**
   * Creates an empty PartyGame instance with an empty game board and default dice.
   * @return An empty PartyGame instance.
   */
  def empty: PartyGame = PartyGameImpl(GameBoard(Map.empty, Map.empty), summon[Dice])

  given CanRoll[PartyGame] with
    extension (pg: PartyGame)
      def roll(n: Int): (PartyGame, List[Int]) =
        val (newDice, result) = pg.dice.roll(n)
        (PartyGame(pg.board)(using newDice), result)

  extension (pg: PartyGame)
    /**
     * Moves a player to a specified position on the board.
     * @param id The ID of the player to move.
     * @param direction: The direction in which to move the player.
     * @param steps The number of steps to move the player.
   */
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
    /**
     * Retrieves the possible movement directions for a pawn with the specified ID.
     * @param id The ID of the pawn.
     * @return A set of possible directions in which the pawn can move.
     * @throws NoSuchElementException if no pawn is found with the given ID.        
     */
    def getPossibleDirections(id: Int): Set[Direction] =
      pg.board.pawns.get(id) match
        case Some(pawn) => pg.board.availableDirections(pawn.position)
        case None => throw new NoSuchElementException(s"No pawn found with id $id")

    /**
     * Retrieves the current positions of all players in the game.
     * @return A map where keys are player IDs and values are their current positions on the board.
     */
    def getPlayersPosition: Map[Int, BoardPosition] =
      pg.board.pawns.map((id, pawn) => id -> pawn.position)

    /**
     * Retrieves the positions of all boxes on the board.
     * @return A set of positions representing all boxes on the board.
     */
    def getBoardBoxes: Set[BoardPosition] =
      pg.board.board.keys.toSet

    /**
     * Retrieves all items currently on the board.
     * @return A map where keys are positions on the board and values are the collectable items at those positions.
     */
    def getItems: Map[BoardPosition, Collectable] =
      pg.board.board.collect:
        case (pos, BoardBox.FullBox(item)) => pos -> item

    /**
     * Retrieves the pockets of all players in the game.
     * @return A map where keys are player IDs and values are their respective pockets containing collected items.
     */
    def getPockets: Map[Int, Pocket] =
      pg.board.pawns.collect:
        case (id, pawn) => id -> pawn.pocket

    /**
     * Regenerates the game board by clearing all items and adding new random items.
     * @return A new PartyGame instance with the regenerated board.
     */
    def regenerateBoard: PartyGame =
      val newBoard = pg.board.clearItems
        .addRandomItems(CollectableType.MonadType, monadsOnBoard)
        .addRandomItem(Collectable.Rung(rungPrice))
      PartyGame(newBoard)(using pg.dice)

    /**
     * Regenerates the monads on the board by adding a specified number of new monads.
     * @return A new PartyGame instance with the updated board containing new monads.
     */
    def regenerateMonads: PartyGame =
      val newBoard = pg.board.addRandomItems(CollectableType.MonadType, monadsOnBoard)
      PartyGame(newBoard)(using pg.dice)

