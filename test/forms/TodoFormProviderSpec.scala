package forms

import generators.ModelsGenerators
import models.{NonEmptyString, Open, Todo}
import org.scalatest.prop.PropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}

class TodoFormProviderSpec extends WordSpec
  with MustMatchers
  with OptionValues
  with PropertyChecks
  with ModelsGenerators {

  val testForm = new TodoFormProvider()

  "apply" should {

    "successfully bind values" when {

      "valid values are provided" in {

        forAll { testTodo: Todo =>

          testForm().fillAndValidate(testTodo).fold(
            error => fail(s"form should succeed : $error"),
            todo => todo mustBe testTodo
          )
        }
      }
    }
  }
}