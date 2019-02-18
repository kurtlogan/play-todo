package forms

import com.google.inject.{Inject, Singleton}
import models.{NonEmptyString, Open, Todo, TodoState}
import play.api.data.Forms._
import play.api.data.Form

@Singleton
class TodoFormProvider @Inject()() extends Formatters {

  def apply(): Form[Todo] = Form(
    mapping(
      "name" -> of[NonEmptyString],
      "description" -> text,
      "state" -> ignored[TodoState](Open)
    )(Todo.apply)(Todo.unapply)
  )
}
