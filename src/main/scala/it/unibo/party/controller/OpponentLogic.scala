package it.unibo.party.controller

import alice.tuprolog.{Struct, Term, Var}
import it.unibo.party.common.{PartyPhase, PartyState, Player, State}
import it.unibo.party.controller.Moves.PartyMove
import it.unibo.party.controller.pubsub.Subscriber
import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.CollectableType.MonadType
import it.unibo.party.{RichFile, extractTerm, mkPrologEngine, openTheoryFile}

trait OpponentLogic extends Subscriber[State]

object OpponentLogic:

  def apply(playingAgent: PlayingAgent): OpponentLogic = OpponentLogicImpl(playingAgent)

  private case class OpponentLogicImpl(playingAgent: PlayingAgent) extends OpponentLogic:
    private val player: Player = Player(playingAgent.id)

    override def notify(event: State): Unit =
      event match
        case ps: PartyState if ps.currentPlayer.id == playingAgent.id =>
          ps.phase match
            case PartyPhase.PlayerMoving =>
              val dir = chooseDirection(ps.board, ps.itemsPositions, ps.playersPositions(player), ps.itemsCollected(player).countByType(MonadType))
              playingAgent.makeMove(PartyMove.Movement(playingAgent.id, dir.getOrElse(ps.possibleDirections.get.head)))
            case PartyPhase.DiceRoll | PartyPhase.StartingRoll =>
              playingAgent.makeMove(PartyMove.DiceRoll(playingAgent.id))
            case _ =>
        case _ =>

    private def chooseDirection(board: Set[Point2D[Int]],
                                items: Map[Point2D[Int], Collectable],
                                opponentPosition: Point2D[Int],
                                monadsOwned: Int): Option[Direction] =
      var strBoard = board.map { pos =>
        val content = items.getOrElse(pos, "empty")
        s"cell((${pos.x}, ${pos.y}), $content)."
      }.mkString("\n")
      strBoard = strBoard.replace("Empty", "empty")
      strBoard = strBoard.replace("Monad()", "monad")
      strBoard = strBoard.replace("Rung(5)", "rung")
      val strOpponent = s"opponent_pos((${opponentPosition.x}, ${opponentPosition.y})).\n"
      val strMonads = s"monads_owned($monadsOwned).\n"
      val prologFacts =
        s"""
        $strBoard
        $strOpponent
        $strMonads
        """
      var prologRules = ""
      openTheoryFile("src/main/resources/prolog/opponentLogicRules.pl").read().foreach { line =>
        if !line.startsWith("%") || line.trim.nonEmpty then
          prologRules = prologRules.concat("\n" + line)
      }
      val prologTheory = prologFacts + prologRules
      val engine: Term => LazyList[Term] = mkPrologEngine(prologTheory)
      val input = Struct("best_path", Var("Path"))
      val results = engine(input)
      val strOutput = results.map(extractTerm(_, 0)).headOption.get.toString
      val pattern = """\((\d+),(\d+)\)""".r
      val result: List[(Int, Int)] = pattern.findAllMatchIn(strOutput).map { m =>
        (m.group(1).toInt, m.group(2).toInt)
      }.toList
      val nextPosition = result.drop(1).head
      val nextPoint2D = Point2D(nextPosition._1, nextPosition._2)
      if nextPoint2D - opponentPosition == Point2D(0, -1) then Some(Direction.Up)
      else if nextPoint2D - opponentPosition == Point2D(1, 0) then Some(Direction.Right)
      else if nextPoint2D - opponentPosition == Point2D(0, 1) then Some(Direction.Down)
      else if nextPoint2D - opponentPosition == Point2D(-1, 0) then Some(Direction.Left)
      else None