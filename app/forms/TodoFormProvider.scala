package forms

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import models.{NonEmptyString, Open, Todo, TodoState}
import play.api.data.Forms._
import play.api.data.Form

@Singleton
class TodoFormProvider @Inject()() extends Formatters with Constraints {

  def apply(): Form[Todo] = Form(
    mapping(
      "id" -> ignored[UUID](UUID.randomUUID()),
      "name" -> of[NonEmptyString],
      "description" -> text.verifying(maxSize(512)),
      "state" -> ignored[TodoState](Open)
    )(Todo.apply)(Todo.unapply)
  )
}
