package forms

import java.time.{LocalDate, Month}

import generators.Generators
import models.{Open, Todo}
import org.scalacheck.Arbitrary._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{MustMatchers, WordSpec}
import play.api.data.Form
import services.DateService

class TodoFormProviderSpec extends WordSpec
  with MustMatchers
  with PropertyChecks
  with Generators {

  class FakeDateService(override val now: LocalDate) extends DateService

  def form(now: LocalDate = now) = new TodoFormProvider(new FakeDateService(now))

  val now = LocalDate.of(2018, Month.OCTOBER, 19)

  def error(form: Form[_], key: String): Option[String] =
    form.error(key).flatMap(_.messages.lastOption)

  "form" should {

    "bind" when {

      "valid values are bound" in {

        forAll { todo: Todo =>

          form()().fillAndValidate(todo).fold(
            _ => fail("form should not fail"),
            t => {
              t.name mustBe todo.name
              t.description mustBe todo.description
              t.createdOn mustBe todo.createdOn
              t.state mustBe todo.state
            }
          )
        }
      }
    }

    "fail" when {

      "description is longer than 512 characters" in {

        val badDataGen =
          for {
            todo <- arbitrary[Todo]
            desc <- minStringSize(513)
          } yield {
            todo.copy(description = desc)
          }

        forAll(badDataGen) { badData =>

          form()().fillAndValidate(badData).fold(
            error(_, "description") mustBe Some("error.too-long"),
            _ => fail("form should fail")
          )
        }
      }
    }
  }
}