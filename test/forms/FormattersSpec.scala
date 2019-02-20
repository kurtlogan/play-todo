package forms

import models.NonEmptyString
import org.scalatest.prop.PropertyChecks
import org.scalatest.{MustMatchers, WordSpec}
import play.api.data.Form
import play.api.data.Forms.{of => fof, _}

class FormattersSpec extends WordSpec
  with MustMatchers
  with Formatters
  with PropertyChecks {

  case class Wrapper(value: NonEmptyString)

  val testForm = Form(
    mapping("prop" -> fof[NonEmptyString])(Wrapper.apply)(Wrapper.unapply)
  )

  "nonEmptyString" should {

    "successfully bind" when {

      "valid values are bound" in {

        forAll { s: String =>

          whenever(s.nonEmpty) {

            testForm.bind(Map("prop" -> s)).fold(
              error => fail(s"form shouldn't fail: $error"),
              wrapper => wrapper.value.value mustBe s
            )
          }
        }
      }
    }

    "fail binding" when {

      "an empty string is provided" in {

        testForm.bind(Map("prop" -> "")).fold(
          form    => form.error("prop").flatMap(_.messages.lastOption) mustBe Some("error.nonemptystring.empty"),
          wrapper => fail(s"form should not succeed: $wrapper")
        )
      }

      "no values are passed" in {

        testForm.bind(Map[String, String]()).fold(
          form    => form.error("prop").flatMap(_.messages.lastOption) mustBe Some("error.nonemptystring.empty"),
          wrapper => fail(s"form should not succeed: $wrapper")
        )
      }
    }
  }
}