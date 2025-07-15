package it.unibo.party.controller.pubsub

/**
 * Subscriber is a trait that represents a subscriber in the pub-sub pattern.
 *
 * @tparam T the type of events that the subscriber can receive
 */
trait Subscriber[T]:
  /**
   * Notifies the subscriber of an event.
   *
   * @param event the event to be notified
   */
  def notify(event: T): Unit

object Subscriber:
  def apply[T](f: T => Unit): Subscriber[T] = SubscriberImpl(f)

  private class SubscriberImpl[T](f: T => Unit) extends Subscriber[T]:

    override def notify(event: T): Unit = f(event)
