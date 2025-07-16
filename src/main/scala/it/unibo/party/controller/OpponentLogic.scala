package it.unibo.party.controller

import alice.tuprolog.{Struct, Term, Var}
import it.unibo.party.common.{PartyPhase, PartyState, Player, State}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.pubsub.Subscriber
import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.Collectable.Rung
import it.unibo.party.model.items.CollectableType.MonadType
import it.unibo.party.{RichFile, extractTerm, mkPrologEngine, openTheoryFile}
import scalafx.animation.PauseTransition
import scalafx.util.Duration

trait OpponentLogic extends Subscriber[State]

object OpponentLogic:

  /**
   * Creates an instance of OpponentLogic.
   *
   * @param playingAgent the agent that controls the opponent's moves
   * @return an instance of OpponentLogic
   */
  def apply(playingAgent: PlayingAgent): OpponentLogic = OpponentLogicImpl(playingAgent)

  private case class OpponentLogicImpl(playingAgent: PlayingAgent) extends OpponentLogic:
    private val player: Player = Player(playingAgent.id)

    override def notify(event: State): Unit =
      event match
        case ps: PartyState if ps.currentPlayer.id == playingAgent.id =>
          ps.phase match
            case PartyPhase.PlayerMoving =>
              val delay = PauseTransition(Duration(300))
              delay.onFinished = _ =>
                val dir = chooseDirection(ps.board, ps.itemsPositions, ps.playersPositions(player), ps.itemsCollected(player).countByType(MonadType))
                playingAgent.makeMove(PartyMove.Movement(playingAgent.id, dir.getOrElse(ps.possibleDirections.get.head)))
              delay.play()
            case PartyPhase.DiceRoll | PartyPhase.StartingRoll =>
              val delay = PauseTransition(Duration(800))
              delay.onFinished = _ => playingAgent.makeMove(PartyMove.DiceRoll(playingAgent.id))
              delay.play()
            case _ =>
        case _ =>

    private def chooseDirection(board: Set[Point2D[Int]],
                                items: Map[Point2D[Int], Collectable],
                                opponentPosition: Point2D[Int],
                                monadsOwned: Int): Option[Direction] =
      // Convert the board and items to Prolog facts
      var strBoard = board.map: pos =>
        val content = items.getOrElse(pos, "empty")
        s"cell((${pos.x}, ${pos.y}), $content)."
      .mkString("\n")
      strBoard = strBoard.replace("Empty", "empty")
      strBoard = strBoard.replace("Monad()", "monad")
      strBoard = strBoard.replaceAll("Rung\\(\\d+\\)", "rung")
      val strOpponent = s"opponent_pos((${opponentPosition.x}, ${opponentPosition.y})).\n"
      val strMonads = s"monads_owned($monadsOwned).\n"
      val strRungCost = s"rung_cost(${items.filter((_, item) => item.isInstanceOf[Rung]).head._2.asInstanceOf[Rung]._1})."
      val prologFacts =
        s"""
        $strBoard
        $strOpponent
        $strMonads
        $strRungCost
        """
      val prologRules =
        openTheoryFile("src/main/resources/prolog/opponentLogicRules.pl")
          .read()
          .filter(line => !line.startsWith("%") && line.trim.nonEmpty)
          .mkString("\n")
      val prologTheory = prologFacts + prologRules
      // Invoke the Prolog engine to find the best path
      val engine: Term => LazyList[Term] = mkPrologEngine(prologTheory)
      val input = Struct("best_path", Var("Path"))
      val results = engine(input)
      // Convert the Prolog result to a Direction
      val strOutput = results.map(extractTerm(_, 0)).headOption.get.toString
      val pattern = """\((\d+),(\d+)\)""".r
      val result: List[(Int, Int)] = pattern.findAllMatchIn(strOutput).map: m =>
        (m.group(1).toInt, m.group(2).toInt)
      .toList
      val nextPosition = result.drop(1).head
      val nextPoint2D = Point2D(nextPosition._1, nextPosition._2)
      Some(computeDirection(opponentPosition, nextPoint2D))

    private def computeDirection(from: Point2D[Int], to: Point2D[Int]): Direction =
      (to - from) match
        case Point2D(0, -1) => Direction.Up
        case Point2D(1, 0) => Direction.Right
        case Point2D(0, 1) => Direction.Down
        case Point2D(-1, 0) => Direction.Left
        case _ => throw new IllegalArgumentException(s"Invalid direction from $from to $to")