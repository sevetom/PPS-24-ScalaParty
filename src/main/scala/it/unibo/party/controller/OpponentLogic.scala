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
            val dir = chooseDirection(event.possibleDirections.get, event.board, event.itemsPositions, event.playersPositions(playingAgent.id), event.itemsCollected(playingAgent.id).countByType(MonadType))
            playingAgent.makeMove(PartyMove(playingAgent.id, PartyMoveType.Movement, dir))
          case PartyPhase.DiceRoll =>
            playingAgent.makeMove(PartyMove(playingAgent.id, PartyMoveType.DiceRoll, None))
          case _ => // Ignore other phases


    private def chooseDirection(possibleDirections: Set[Direction],
                                board: Set[Point2D[Int]],
                                items: Map[Point2D[Int], Collectable],
                                opponentPosition: Point2D[Int],
                                monadsOwned: Int): Option[Direction] =
      //var strBoard = ""
      //board.foreach(
      //  box => items.foreach(
      //    item => if box == item._1 then {
      //      strBoard = strBoard++s"cell((${item._1.x}, ${item._1.y}), ${item._2}).\n"
      //    } else strBoard = strBoard++s"cell((${item._1.x}, ${item._1.y}), Empty).\n"))
      var strBoard = board.map { pos =>
        val content = items.getOrElse(pos, "empty")
        s"cell((${pos.x}, ${pos.y}), $content)."
      }.mkString("\n")
      val strOpponent = s"opponent_pos((${opponentPosition.x}, ${opponentPosition.y})).\n"
      val strMonads = s"monads_owned($monadsOwned).\n"
      strBoard = strBoard.replace("Empty", "empty")
      strBoard = strBoard.replace("Monad()", "monad")
      strBoard = strBoard.replace("Rung(5)", "rung")
      val prologTheory =
        s"""
$strBoard
$strOpponent
$strMonads
valid_position(Pos) :- cell(Pos, _).

adjacent((X,Y), (X2,Y)) :-
    X2 is X + 1,
    valid_position((X2,Y)).
adjacent((X,Y), (X2,Y)) :-
    X2 is X - 1,
    valid_position((X2,Y)).
adjacent((X,Y), (X,Y2)) :-
    Y2 is Y + 1,
    valid_position((X,Y2)).
adjacent((X,Y), (X,Y2)) :-
    Y2 is Y - 1,
    valid_position((X,Y2)).

manhattan((X1,Y1), (X2,Y2), D) :-
    DX is abs(X1 - X2),
    DY is abs(Y1 - Y2),
    D is DX + DY.

insert_into_sorted((D, X, Y), [], [(D, X, Y)]).
insert_into_sorted((D_new, X_new, Y_new), [(D_head, X_head, Y_head)|T], [(D_new, X_new, Y_new), (D_head, X_head, Y_head)|T]) :-
    D_new =< D_head.
insert_into_sorted((D_new, X_new, Y_new), [(D_head, X_head, Y_head)|T], [(D_head, X_head, Y_head)|SortedT]) :-
    D_new > D_head,
    insert_into_sorted((D_new, X_new, Y_new), T, SortedT).

sort([], []).
sort([H|T], SortedList) :-
    sort(T, SortedTail),
    insert_into_sorted(H, SortedTail, SortedList).

closest_monad(Start, Target) :-
	findall((D, Pos), (
		cell(Pos, monad),
		manhattan(Start, Pos, D)
	), Distances),
	Distances \\= [],
	sort(Distances, [(_, Target)|_]).

path(Start, Goal, Path) :-
    path_aux(Start, Goal, [Start], RPath, 30),
    reverse(RPath, Path).

path_aux(Goal, Goal, Visited, Visited, _).
path_aux(Current, Goal, Visited, Path, Limit) :-
    Limit > 0,
    adjacent(Current, Next),
    \\+ member(Next, Visited),
    L1 is Limit - 1,
    path_aux(Next, Goal, [Next|Visited], Path, L1).

path_bfs(Start, Goal, Path) :-
    bfs_queue([[Start]], Goal, FinalPath),
    reverse(FinalPath, Path).

bfs_queue([[Goal|PathTail] | _], Goal, [Goal|PathTail]).
bfs_queue([[Current|PathTail] | QueueTail], Goal, Path) :-
    findall(
        [Next, Current | PathTail],
        (adjacent(Current, Next), \\+ member(Next, [Current|PathTail])),
        NewPaths
    ),
    append(QueueTail, NewPaths, UpdatedQueue),
    bfs_queue(UpdatedQueue, Goal, Path).

best_path(Path) :-
    opponent_pos(Start),
    monads_owned(N),
    closest_monad(Start, Target),
    path_bfs(Start, Target, Path).
"""
      val engine: Term => LazyList[Term] = mkPrologEngine(prologTheory)
      val input = Struct("best_path", Var("Path"))
      val results = engine(input)
      val strOutput = results.map(extractTerm(_, 0)).headOption.get.toString
      val pattern = """\((\d+),(\d+)\)""".r

      val result: List[(Int, Int)] = pattern.findAllMatchIn(strOutput).map { m =>
        (m.group(1).toInt, m.group(2).toInt)
      }.toList
      println(result)
      val nextPosition = result.drop(1).head
      val nextPoint2D = Point2D(nextPosition._1, nextPosition._2)
      if nextPoint2D - opponentPosition == Point2D(0, -1) then Some(Direction.Up)
      else if nextPoint2D - opponentPosition == Point2D(1, 0) then Some(Direction.Right)
      else if nextPoint2D - opponentPosition == Point2D(0, 1) then Some(Direction.Down)
      else if nextPoint2D - opponentPosition == Point2D(-1, 0) then Some(Direction.Left)
      else None



