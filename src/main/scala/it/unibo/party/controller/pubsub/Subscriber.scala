package it.unibo.party.controller.pubsub

trait Subscriber[T]:
  def notify(event: T): Unit
