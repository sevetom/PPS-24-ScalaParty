package it.unibo.party.controller

import alice.tuprolog.{Struct, Term, Var}
import it.unibo.party.common.{PartyPhase, PartyState}
import it.unibo.party.controller.Moves.{PartyMove, PartyMoveType}
import it.unibo.party.controller.pubsub.Subscriber
import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.items.CollectableType.MonadType
import it.unibo.party.{extractTerm, mkPrologEngine}

trait OpponentLogic extends Subscriber[PartyState]

object OpponentLogic:

  def apply(playingAgent: PlayingAgent): OpponentLogic = OpponentLogicImpl(playingAgent)

  private case class OpponentLogicImpl(playingAgent: PlayingAgent) extends OpponentLogic:
    override def notify(event: PartyState): Unit =
      if event.currentPlayer == playingAgent.id then
        event.phase match
          case PartyPhase.PlayerMoving =>
            /*val directionPriority = List(Direction.Up, Direction.Left, Direction.Right, Direction.Down)
            val sortedDirOpt =
              event.possibleDirections
                .flatMap(dirs => directionPriority.find(dirs.contains))
            sortedDirOpt.foreach:
              dir => */
            val dir = chooseDirection(event.possibleDirections.get, event.board, event.itemsPositions, event.playersPositions(playingAgent.id), event.diceResult.get, event.itemsCollected(playingAgent.id).countByType(MonadType))
            playingAgent.makeMove(PartyMove(playingAgent.id, PartyMoveType.Movement, Some(Direction.Down)))
          case PartyPhase.DiceRoll =>
            playingAgent.makeMove(PartyMove(playingAgent.id, PartyMoveType.DiceRoll, None))
          case _ => // Ignore other phases


    private def chooseDirection(possibleDirections: Set[Direction],
                                board: Set[Point2D[Int]],
                                items: Map[Point2D[Int], Collectable],
                                opponentPosition: Point2D[Int],
                                steps: Int,
                                monadsOwned: Int): Option[Direction] =
      var strBoard = ""
      board.foreach(
        box => items.foreach(
          item => if box == item._1 then {
            strBoard = strBoard++s"cell((${item._1.x}, ${item._1.y}), ${item._2}).\n"
          } else strBoard = strBoard++s"cell((${item._1.x}, ${item._1.y}), Empty).\n"))
      val strOpponent = s"opponent_pos((${opponentPosition.x}, ${opponentPosition.y})).\n"
      val strSteps = s"steps($steps).\n"
      val strMonads = s"monads_owned($monadsOwned).\n"
      strBoard = strBoard.replace("Empty", "empty")
      strBoard = strBoard.replace("Monad()", "monad")
      strBoard = strBoard.replace("Rung(5)", "rung")
      val prologTheory =
        s"""
$strBoard
$strOpponent
$strSteps
$strMonads
adjacent((X,Y), (X2,Y)) :- X2 is X + 1.
adjacent((X,Y), (X2,Y)) :- X2 is X - 1.
adjacent((X,Y), (X,Y2)) :- Y2 is Y + 1.
adjacent((X,Y), (X,Y2)) :- Y2 is Y - 1.

valid_position((X,Y)) :-
    X >= 0, X =< 3,
    Y >= 0, Y =< 3.

available_item(monad, _, Pos) :-
    \\+ already_collected(Pos).

available_item(rung, MonadsOwned, Pos) :-
    MonadsOwned >= 5,
    \\+ already_collected(Pos).

reachable(Start, Goal, MaxSteps, Path) :-
    reachable_aux(Start, Goal, [Start], Path, MaxSteps).

reachable_aux(Goal, Goal, Visited, Path, _) :-
    reverse(Visited, Path).

reachable_aux(Current, Goal, Visited, Path, StepsLeft) :-
    StepsLeft > 0,
    adjacent(Current, Next),
    valid_position(Next),
    \\+ member(Next, Visited),
    Steps1 is StepsLeft - 1,
    reachable_aux(Next, Goal, [Next|Visited], Path, Steps1).

best_target(Start, MaxSteps, Target, Path) :-
    monads_owned(M),
    findall((Pos, Item), (
        cell(Pos, Item),
        Item \\= empty,
        available_item(Item, M, Pos)
    ), Items),
    member((Target, _), Items),
    reachable(Start, Target, MaxSteps, Path),
    !.
"""
      println(prologTheory)
      val engine: Term => LazyList[Term] = mkPrologEngine(prologTheory)
      val input = Struct("opponent_pos(P), steps(S), best_target(P, S, Target, Path)",
        Var("P"), Var("S"), Var("Target"), Var("Path"))
      engine(input) map (extractTerm(_, 1)) foreach (println(_))
      possibleDirections.headOption

