package it.unibo.party.common

import it.unibo.party.geometry.{Direction, Point2D}
import it.unibo.party.model.items.Collectable
import it.unibo.party.model.memory.Memory
import it.unibo.party.model.partyGame.PartyGame
import it.unibo.party.model.player.Pocket

trait State:
  /**
   * The phase of the game.
   * @return the current phase of the game.
   */
  def phase: Phase

trait MinigameState extends State:
  override def phase: MinigamePhase
  /**
   * The winner of the minigame, if any.
   * @return an Option containing the Player who won, or None if there is no winner yet.
   */
  def winner: Option[Player]
