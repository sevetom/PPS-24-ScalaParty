package it.unibo.party

import alice.tuprolog.{Struct, Term, Var}
import it.unibo.party.{extractTerm, mkPrologEngine}

object TryScala2P extends App:

  import it.unibo.party.Scala2P.{*, given}

  val engine: Term => LazyList[Term] = mkPrologEngine(
    """
    member([H|T], H, T).
    member([H|T], E,[H|T2]) :- member(T, E, T2).
    permutation([], []).
    permutation(L, [H|TP ]) :- member(L,H,T), permutation(T,TP).
  """)

  engine(" permutation([1 ,2 ,3] ,L)") foreach (println(_))
  // permutation ([1 ,2 ,3] ,[1 ,2 ,3]) ... permutation ([1 ,2 ,3] ,[3 ,2 ,1])

  val input = Struct("permutation ", (1 to 20), Var())
  engine(input) map (extractTerm(_, 1)) foreach (println(_))
// [1 ,2 ,3 ,4 ,.. ,20] ... [1 ,2 ,.. ,15 ,20 ,16 ,18 ,19 ,17]
