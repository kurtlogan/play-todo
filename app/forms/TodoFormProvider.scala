package forms

import java.time.LocalDate
import java.util.UUID

import com.google.inject.{Inject, Singleton}
import models.{NonEmptyString, Open, Todo, TodoState}
import play.api.data.Forms._
import play.api.data._
import services.DateService

@Singleton
class TodoFormProvider @Inject()(date: DateService) extends Formatters with Constraints {

  def apply(): Form[Todo] = Form(
      mapping(
        "id"        -> ignored[UUID](UUID.randomUUID()),
      "name"        -> of[NonEmptyString],
      "description" -> text.verifying(maxSize(512)),
      "createdOn"   -> ignored[LocalDate](date.now),
      "todoState"   -> ignored[TodoState](Open)
    )(Todo.apply)(Todo.unapply)
  )
}