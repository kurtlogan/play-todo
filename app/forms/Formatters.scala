package forms

import models.NonEmptyString
import play.api.data.FormError
import play.api.data.format.Formatter

trait Formatters {

  implicit val nonEmptyString: Formatter[NonEmptyString] =
    new Formatter[NonEmptyString] {
      override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], NonEmptyString] =
        data.get(key)
          .flatMap(NonEmptyString(_))
          .map(Right(_))
          .getOrElse(Left(Seq(FormError(key, "error.nonemptystring.empty"))))

      override def unbind(key: String, value: NonEmptyString): Map[String, String] =
        Map(key -> value.value)
    }
}