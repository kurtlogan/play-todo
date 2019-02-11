package forms

import models.NonEmptyString
import play.api.data._
import play.api.data.format._

trait Formatters {

  implicit val nonEmptyStringFormatter = new Formatter[NonEmptyString] {

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], NonEmptyString] =
      data
        .get(key)
        .flatMap(NonEmptyString(_))
        .map(Right(_))
        .getOrElse(Left(List(FormError(key, "error.required"))))

    override def unbind(key: String, value: NonEmptyString): Map[String, String] =
      Map(key -> value.value)
  }
}