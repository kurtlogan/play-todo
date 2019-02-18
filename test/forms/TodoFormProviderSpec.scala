package forms

import java.time.{LocalDate, Month}

import models.{NonEmptyString, Open, Todo}
import org.scalatest.{MustMatchers, WordSpec}
import play.api.data.Form
import services.DateService

class TodoFormProviderSpec extends WordSpec with MustMatchers {

  class FakeDateService(override val now: LocalDate) extends DateService

  def form(now: LocalDate = now) = new TodoFormProvider(new FakeDateService(now))

  val now = LocalDate.of(2018, Month.OCTOBER, 19)

  def error(form: Form[_], key: String): Option[String] =
    form.error(key).flatMap(_.messages.lastOption)

  "form" should {

    "bind" when {

      "valid values are bound" in {

        form()().bind(Map("name" -> "foo", "description" -> "")).fold(
          _ => fail("form should not fail"),
          todo => Some(todo) mustBe NonEmptyString("foo").map(Todo(_, "", now, Open))
        )
      }
    }

    "fail" when {

      "description is longer than 512 characters" in {

        form()().bind(Map("name" -> "foo", "description" -> "a" * 513)).fold(
          error(_, "description") mustBe Some("error.too-long"),
          _ => fail("form should fail")
        )
      }

      "description contains non alpha numeric characters" in {

        form()().bind(Map("name" -> "foo", "description" -> "?")).fold(
          error(_, "description") mustBe Some("error.non-alpha-numeric"),
          _ => fail("form should fail")
        )
      }
    }
  }
}