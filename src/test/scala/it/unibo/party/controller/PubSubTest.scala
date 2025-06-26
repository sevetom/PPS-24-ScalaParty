package it.unibo.party.controller

import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PubSubTest extends AnyFlatSpec:
  case class Log[E](var memory: List[E]):
    def log(message: E): Unit = memory = memory :+ message
    def latest(): Option[E] = memory.lastOption
    def reset(): Unit = memory = List()

  val newValue = 1

  val logger: Log[Int] = Log(List())
  val publisher: Publisher[Int] = Publisher.emptyPublisher
  val subscriber: Subscriber[Int] = Subscriber(logger.log)

  "A publisher" should "allow adding a subscriber" in:
    val updatedPub = publisher.subscribe(subscriber)
    updatedPub.contains(subscriber) shouldBe true

  it should "allow unsubscribing a subscriber" in:
    val updatedPub = publisher.unsubscribe(subscriber)
    updatedPub.contains(subscriber) shouldBe false

  it should "notify subscribers when an event is published" in:
    logger.reset()
    val updatedPub = publisher.subscribe(subscriber)
    updatedPub.publish(newValue)
    logger.latest().get shouldBe newValue

  it should "not notify unsubscribed subscribers" in:
    logger.reset()
    var updatedPub = publisher.subscribe(subscriber)
    updatedPub = publisher.unsubscribe(subscriber)
    updatedPub.publish(newValue)
    logger.latest().isEmpty shouldBe true
