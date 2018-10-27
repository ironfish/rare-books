package com.rarebooks.library

import akka.actor.{ Actor, ActorRef, ActorLogging, Props, Stash }
import scala.concurrent.duration.FiniteDuration

object Librarian {

  import Catalog._
  import RareBooksProtocol._

  final case class Done(e: Either[BookNotFound, BookFound], customer: ActorRef)

  def props(findBookDuration: FiniteDuration): Props =
      Props(new Librarian(findBookDuration))

  /**
   * Convert option to either function.
   *
   * @param v input for function
   * @param f function to match against
   * @tparam T type for Either
   * @return on success return Right[BookFound] otherwise return Left[BookNotFound]
   */
  private def optToEither[T](v: T, f: T => Option[List[BookCard]]): Either[BookNotFound, BookFound] =
    f(v) match {
      case b: Some[List[BookCard]] => Right(BookFound(b.get))
      case _                       => Left(BookNotFound(s"Book(s) not found based on $v"))
    }

  /**
   * Convert option to either for validation.
   *
   * @param fb find book command
   * @return either list of books or error
   */
  private def findByIsbn(fb: FindBookByIsbn): Either[BookNotFound, BookFound] =
    optToEither[String](fb.isbn, findBookByIsbn)

  /**
   * Convert option to either for validation.
   *
   * @param fb find book command
   * @return either list of books or error
   */
  private def findByAuthor(fb: FindBookByAuthor): Either[BookNotFound, BookFound] =
    optToEither[String](fb.author, findBookByAuthor)

  /**
   * Convert option to either for validation.
   *
   * @param fb find book command
   * @return either list of books or error
   */
  private def findByTitle(fb: FindBookByTitle): Either[BookNotFound, BookFound] =
    optToEither[String](fb.title, findBookByTitle)

  /**
   * Convert option to either for validation.
   *
   * @param fb find book command
   * @return either list of books or error
   */
  private def findByTopic(fb: FindBookByTopic): Either[BookNotFound, BookFound] =
    optToEither[Set[Topic]](fb.topic, findBookByTopic)
}

class Librarian(findBookDuration: FiniteDuration) extends Actor with ActorLogging with Stash {

  import context.dispatcher
  import Librarian._
  import RareBooksProtocol._

  /**
   * Set the initial behavior.
   *
   * @return partial function ready
   */
  override def receive: Receive = ready
  /**
   * Behavior when ready to receive a find book request
   *
   * @return partial function for completing the request
   */
  private def ready: Receive = {
    case m: Msg => m match {
      case _: Complain =>
        log.info(s"Credit issued to customer $sender()")
        sender ! Credit()
      case f: FindBookByIsbn =>
        research(Done(findByIsbn(f), sender()))
      case f: FindBookByAuthor =>
        research(Done(findByAuthor(f), sender()))
      case f: FindBookByTitle =>
        research(Done(findByTitle(f), sender()))
      case f: FindBookByTopic =>
        research(Done(findByTopic(f), sender()))
    }
  }

  /**
   * Behavior simulating the need research the customer's request.
   *
   * @return partial function for completing request
   */
  private def busy: Receive = {
    case Done(e, s) =>
      process(e, s)
      unstashAll()
      context.become(ready)
    case _ =>
      stash()
  }

  /**
   * Simulate researching for information by scheduling completion of the task for a given duration.
   * @param d simulation is done
   */
  private def research(d: Done): Unit = {
    context.system.scheduler.scheduleOnce(findBookDuration, self, d)
    context.become(busy)
  }

  /**
   * Process messages for finding books by folding over either
   *
   * @param r on success return BookFound otherwise return BookNotFound
   */
  private def process(r: Either[BookNotFound, BookFound], sender: ActorRef): Unit = {
    r fold (
      f => {
        sender ! f
        log.info(f.toString)
      },
      s => sender ! s)
  }
}
