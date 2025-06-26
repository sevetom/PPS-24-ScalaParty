package it.unibo.party.controller

import it.unibo.party.controller.pubsub.{Publisher, Subscriber}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class PubSubTest extends AnyFlatSpec:
  val startingValue: Int = 0
  val incrementAmount: Int = 1

  case class testCounter(var value: Int):
    def increment(amount: Int): Unit = value += amount
    def reset(): Unit = value = startingValue

  val counter: testCounter = testCounter(startingValue)
  val publisher: Publisher[Int] = Publisher.emptyPublisher
  val subscriber: Subscriber[Int] = Subscriber(counter.increment)

  "A publisher" should "allow adding a subscriber" in:
    val updatedPub = publisher.subscribe(subscriber)
    updatedPub.contains(subscriber) shouldBe true

  it should "allow unsubscribing a subscriber" in:
    val updatedPub = publisher.unsubscribe(subscriber)
    updatedPub.contains(subscriber) shouldBe false

  it should "notify subscribers when an event is published" in:
    counter.reset()
    var updatedPub = publisher.subscribe(subscriber)
    updatedPub.publish(incrementAmount)
    counter.value shouldBe startingValue + incrementAmount

  it should "not notify unsubscribed subscribers" in:
    counter.reset()
    var updatedPub = publisher.subscribe(subscriber)
    updatedPub = publisher.unsubscribe(subscriber)
    updatedPub.publish(incrementAmount)
    counter.value shouldBe startingValue
