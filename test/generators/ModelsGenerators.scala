package generators

import models.{NonEmptyString, Open, Todo}
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalatest.OptionValues

trait ModelsGenerators extends OptionValues {

  implicit val arbitraryNonEmptyString: Arbitrary[NonEmptyString] =
    Arbitrary {
      arbitrary[String].suchThat(_.nonEmpty).map(NonEmptyString(_).value)
    }

  implicit val arbitraryTodo: Arbitrary[Todo] =
    Arbitrary {
      for {
        name  <- arbitrary[NonEmptyString]
        desc  <- arbitrary[String]
        state <- const(Open)
      } yield {
        Todo(name, desc, state)
      }
    }

}