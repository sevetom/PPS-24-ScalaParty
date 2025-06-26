package it.unibo.party.geometry

enum Direction:
  case Up, Down, Left, Right

  def offset: (Int, Int) = this match
    case Up    => (0, -1)
    case Down  => (0, 1)
    case Left  => (-1, 0)
    case Right => (1, 0)
