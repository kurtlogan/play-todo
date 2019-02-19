package forms

import generators.Generators
import models.NonEmptyString
import org.scalatest.prop.PropertyChecks
import org.scalatest.{MustMatchers, WordSpec}
import play.api.data.Forms.{of => fof, _}
import play.api.data.Form

class FormattersSpec extends WordSpec
  with MustMatchers
  with Formatters
  with PropertyChecks
  with Generators {

  case class Wrapper(value: NonEmptyString)

  val testForm = Form(mapping("prop" -> fof[NonEmptyString])(Wrapper.apply)(Wrapper.unapply))

  def error(form: Form[_], key: String): Option[String] =
    form.error(key).flatMap(_.messages.lastOption)

  "nonEmptyString" should {

    "bind" when {

      "valid values are bound" in {

        forAll { nes: NonEmptyString =>
          testForm.fillAndValidate(Wrapper(nes)).fold(
            _ => fail("form should succeed"),
            _.value mustBe nes
          )
        }
      }
    }

    "fail" when {

      "an empty string is bound" in {

        testForm.bind(Map("prop" -> "")).fold(
          error(_, "prop") mustBe Some("error.required"),
          _ => fail("form should fail")
        )
      }

      "no values are bound" in {

        testForm.bind(Map[String, String]()).fold(
          error(_, "prop") mustBe Some("error.required"),
          _ => fail("form should fail")
        )
      }
    }
  }
}