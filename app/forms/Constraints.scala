package forms

import play.api.data.validation.{Constraint, Invalid, Valid}

trait Constraints {

  def maxSize(max: Int): Constraint[String] =
    Constraint { t =>
      if (t.length <= max) Valid
      else Invalid("error.string.too-long")
    }

  val isAlpha: Constraint[String] =
    Constraint { t =>
      if(t.matches("^[A-Za-z]*$")) Valid
      else Invalid("error.string.non-alpha")
    }
}