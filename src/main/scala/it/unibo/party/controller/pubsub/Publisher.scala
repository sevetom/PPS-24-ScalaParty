package it.unibo.party.controller.pubsub

import it.unibo.party.controller.pubsub.Subscriber

trait Publisher[T]:
  def publish(event: T): Unit

  def subscribe(subscriber: Subscriber[T]): Publisher[T]

  def unsubscribe(subscriber: Subscriber[T]): Publisher[T]
  
  def contains(subscriber: Subscriber[T]): Boolean

object Publisher:
  def apply[T](subscribers: Seq[Subscriber[T]]): Publisher[T] = new PublisherImpl[T](subscribers)

  private class PublisherImpl[T](private val subscribers: Seq[Subscriber[T]]) extends Publisher[T]:

    override def publish(event: T): Unit = {
      println(s"Publishing event: $event to ${subscribers.size} subscribers")
      subscribers.foreach(_.notify(event))
    }

    override def subscribe(subscriber: Subscriber[T]): Publisher[T] = PublisherImpl(subscribers :+ subscriber)

    override def unsubscribe(subscriber: Subscriber[T]): Publisher[T] = PublisherImpl(subscribers.filterNot(_ == subscriber))
    
    override def contains(subscriber: Subscriber[T]): Boolean = subscribers.contains(subscriber)
    
  def emptyPublisher[T]: Publisher[T] = Publisher[T](Seq.empty)

