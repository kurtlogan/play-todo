package models

import java.time.LocalDate

abstract case class NonEmptyString(value: String)

object NonEmptyString {

  def apply(value: String): Option[NonEmptyString] =
    if (value.nonEmpty) Some(new NonEmptyString(value) {})
    else None
}

case class Todo(
  name: NonEmptyString,
  description: String,
  createdOn: LocalDate,
  state: TodoState
)

sealed trait TodoState

case object Open extends TodoState
case class Complete(completedOn: LocalDate) extends TodoState
case class Closed(closedOn: LocalDate) extends TodoState
