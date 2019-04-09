package models

import java.time.LocalDate
import java.util.UUID

sealed abstract case class NonEmptyString(value: String) {
  override def toString: String = value
}

object NonEmptyString {

  def apply(value: String): Option[NonEmptyString] =
    if (value.nonEmpty) Some(new NonEmptyString(value) {})
    else None
}

case class Todo(
                 id: UUID,
                 name: NonEmptyString,
                 description: String,
                 state: TodoState)


sealed trait TodoState

case object Open extends TodoState
case class Completed(completedOn: LocalDate) extends TodoState
case class Closed(closedOn: LocalDate) extends TodoState
