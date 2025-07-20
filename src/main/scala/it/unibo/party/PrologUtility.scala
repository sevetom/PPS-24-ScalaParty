package it.unibo.party

import alice.tuprolog.*

import java.io.File
import scala.io.Source

object Scala2P:
  given Conversion[String, Term] = Term.createTerm(_)

  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")

/**
 * Extracts the i-th term from a Prolog term.
 * @param t the Prolog term, which is expected to be a Struct
 * @param i the index of the term to extract (0-based)
 * @return the i-th term from the Prolog term
 */
def extractTerm(t: Term, i: Integer): Term =
  t.asInstanceOf[Struct].getArg(i).getTerm

/**
 * Creates a Prolog engine with the given clauses.
 * @param clauses the Prolog clauses to be loaded into the engine
 * @return a function that takes a Prolog goal and returns a lazy list of solutions
 */
def mkPrologEngine(clauses: String*): Term => LazyList[Term] =
  val engine = Prolog()
  engine.setTheory(Theory(clauses mkString " "))
  goal=> new Iterable[Term]:
    override def iterator: Iterator[Term] = new Iterator[Term]:
      var solution: SolveInfo = engine.solve(goal)
      override def hasNext: Boolean =
        solution.isSuccess || solution.hasOpenAlternatives
      override def next(): Term =
        try solution.getSolution finally solution = engine.solveNext
  .to(LazyList)

/**
 * Opens a Prolog theory file.
 * @param path the path to the Prolog theory file
 * @return a File object representing the Prolog theory file
 */
def openTheoryFile(path: String) = path

/**
 * Executes a Prolog program with the given facts and input.
 * 
 * @param path the path to the Prolog theory file
 * @param facts the Prolog facts to be used in the program
 * @param input the input term for the Prolog program
 * @return a lazy list of solutions produced by the Prolog engine
 */
def executeProlog(path: String, facts: String, input: Struct): LazyList[Term] =
  var rules = ""
  openTheoryFile(path).read().foreach(line =>
    if !line.startsWith("%") || line.trim.nonEmpty then rules = rules.concat("\n" + line))
  val prologTheory = facts + rules
  val engine: Term => LazyList[Term] = mkPrologEngine(prologTheory)
  engine(input)

implicit class RichFile(filePath: String):
  private val stream = getClass.getResourceAsStream(filePath)
  require(stream != null, s"File not found: $filePath")
  private val source = Source.fromInputStream(stream)

  /**
   * Reads the contents of the file line by line.
   * @return an iterator over the lines of the file
   */
  def read(): Iterator[String] = source.getLines()
