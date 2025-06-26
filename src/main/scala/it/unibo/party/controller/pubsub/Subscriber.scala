package it.unibo.party.controller.pubsub

trait Subscriber[T]:
  def notify(event: T): Unit
  
object Subscriber:
  def apply[T](f: T => Unit): Subscriber[T] = SubscriberImpl(f)
  
  private class SubscriberImpl[T](f: T => Unit) extends Subscriber[T]:
    
    override def notify(event: T): Unit = f(event)
