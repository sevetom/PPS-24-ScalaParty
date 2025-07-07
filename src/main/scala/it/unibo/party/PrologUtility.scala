package it.unibo.party

import alice.tuprolog.*

import java.io.File
import scala.io.Source

object Scala2P:
  given Conversion[String, Term] = Term.createTerm(_)

  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")

def extractTerm(t: Term, i: Integer): Term =
  t.asInstanceOf[Struct].getArg(i).getTerm
  
def mkPrologEngine(clauses: String*): Term => LazyList[Term] =
  val engine = Prolog()
  engine.setTheory(Theory(clauses mkString " "))
  goal => new Iterable[Term]:
    override def iterator: Iterator[Term] = new Iterator[Term]:
      var solution: SolveInfo = engine.solve(goal)
      override def hasNext: Boolean =
        solution.isSuccess || solution.hasOpenAlternatives
      override def next(): Term =
        try solution.getSolution finally solution = engine.solveNext
  .to(LazyList)

def openTheoryFile(path: String) = new File(path)

implicit class RichFile(file: File) {
  def read(): Iterator[String] = {
    Source.fromFile(file).getLines()
  }

}
