package forms

import play.api.data.validation._

trait Constraints {

  def maxSize(max: Int): Constraint[String] =
    Constraint { s =>
      if (s.length <= max) Valid
      else Invalid("error.too-long")
    }

  val isAlpha: Constraint[String] =
    Constraint { s =>
      if (s.matches("^[a-zA-Z]*$")) Valid
      else Invalid("error.non-alpha")
    }

  val isAlphaNumeric: Constraint[String] =
    Constraint { s =>
      if (s.matches("^[a-zA-Z0-9]*$")) Valid
      else Invalid("error-non-alpha-numeric")
    }
}