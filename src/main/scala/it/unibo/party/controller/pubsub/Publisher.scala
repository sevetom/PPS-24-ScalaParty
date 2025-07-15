package it.unibo.party.controller.pubsub

/**
 * Publisher is a trait that represents a publisher in the pub-sub pattern.
 *
 * @tparam T the type of events that the publisher can publish
 */
trait Publisher[T]:
  /**
   * Publishes an event to all subscribers.
   *
   * @param event the event to be published
   */
  def publish(event: T): Unit

  /**
   * Subscribes a new subscriber to the publisher.
   *
   * @param subscriber the subscriber to be added
   * @return a new Publisher instance with the added subscriber
   */
  def subscribe(subscriber: Subscriber[T]): Publisher[T]

  /**
   * Unsubscribes a subscriber from the publisher.
   *
   * @param subscriber the subscriber to be removed
   * @return a new Publisher instance without the removed subscriber
   */
  def unsubscribe(subscriber: Subscriber[T]): Publisher[T]

  /**
   * Checks if the publisher contains a specific subscriber.
   *
   * @param subscriber the subscriber to check
   * @return true if the publisher contains the subscriber, false otherwise
   */
  def contains(subscriber: Subscriber[T]): Boolean

object Publisher:
  /**
   * Creates a new Publisher instance with the given subscribers.
   *
   * @param subscribers the initial list of subscribers
   * @tparam T the type of events that the publisher can publish
   * @return a new Publisher instance
   */
  def apply[T](subscribers: Seq[Subscriber[T]]): Publisher[T] = new PublisherImpl[T](subscribers)

  private class PublisherImpl[T](val subscribers: Seq[Subscriber[T]]) extends Publisher[T]:

    override def publish(event: T): Unit = subscribers.foreach(_.notify(event))

    override def subscribe(subscriber: Subscriber[T]): Publisher[T] = PublisherImpl(subscribers :+ subscriber)

    override def unsubscribe(subscriber: Subscriber[T]): Publisher[T] = PublisherImpl(subscribers.filterNot(_ == subscriber))

    override def contains(subscriber: Subscriber[T]): Boolean = subscribers.contains(subscriber)

  def emptyPublisher[T]: Publisher[T] = Publisher[T](Seq.empty)

  extension [T](publisher: Publisher[T])
    /**
     * Subscribes a new subscriber to the publisher at the beginning of the subscriber list.
     *
     * @param subscriber the subscriber to be added
     * @return a new Publisher instance with the added subscriber at the beginning
     */
    def subscribeFirst(subscriber: Subscriber[T]): Publisher[T] =
      publisher match
        case impl: PublisherImpl[T] =>
          Publisher(Seq(subscriber) ++ impl.subscribers)
        case _ => publisher.subscribe(subscriber)

    /**
     * Subscribes a new subscriber to the publisher at the end of the subscriber list.
     *
     * @param subscriber the subscriber to be added
     * @return a new Publisher instance with the added subscriber
     */
    def subscribeLast(subscriber: Subscriber[T]): Publisher[T] =
      publisher.subscribe(subscriber)

