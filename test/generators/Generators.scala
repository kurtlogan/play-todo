package generators

import java.time.LocalDate
import java.util.Calendar

import models._
import org.scalacheck.Arbitrary._
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Gen._
import org.scalatest.OptionValues

trait Generators extends OptionValues {

  implicit val arbitraryDate: Arbitrary[LocalDate] =
    Arbitrary(calendar.map(c => LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))))

  private def completedStateGen: Gen[TodoState] =
    arbitrary[LocalDate].map(Complete)

  private def closedStateGen: Gen[TodoState] =
    arbitrary[LocalDate].map(Closed)

  implicit val arbitraryState: Arbitrary[TodoState] =
    Arbitrary(oneOf(const(Open), completedStateGen, closedStateGen))

  implicit val arbitraryNonEmptyString: Arbitrary[NonEmptyString] =
    Arbitrary(arbitrary[String].suchThat(_.nonEmpty).map(NonEmptyString(_).value))

  implicit val arbitraryTodo: Arbitrary[Todo] =
    Arbitrary {
      for {
        name    <- arbitrary[NonEmptyString]
        desc    <- arbitrary[String]
        created <- arbitrary[LocalDate]
        state   <- arbitrary[TodoState]
      } yield {
        Todo(name, desc, created, state)
      }
    }

  def minStringSize(min: Int): Gen[String] =
    for {
      i      <- chooseNum(min, min + 500)
      string <- listOfN(i, arbitrary[Char])
    } yield {
      string.mkString
    }
}