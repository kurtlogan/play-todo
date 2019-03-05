package controllers

import java.time.Clock
import java.util.UUID

import forms.TodoFormProvider
import javax.inject._
import models.Complete
import play.api.mvc._
import services.TodoRepository

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
                                cc: ControllerComponents,
                                formProvider: TodoFormProvider,
                                repo: TodoRepository,
                                clock: Clock)
                              (implicit assetsFinder: AssetsFinder)
  extends TodoBaseController(cc) {

  val form = formProvider()

  def display: Action[AnyContent] = Action.async {
    repo.getAll.map(all => Ok(all.mkString("\n")))
  }

  def create: Action[AnyContent] = Action.async { implicit request =>

    form.bindFromRequest().fold(
      errors => Future.successful(BadRequest(errors.errors.map(_.message).mkString("\n"))),
      todo   => repo.insert(todo).map(_ => Ok("Created new todo"))
    )
  }

  def complete(id: UUID): Action[AnyContent] = Action.async { implicit request =>

    repo.modify(id, { todo =>
      todo.copy(state = Complete(java.time.LocalDate.now(clock)))
    }).map {
      case Some(_) => Ok("Todo marked as completed")
      case None    => BadRequest("Unable to mark todo as completed")
    }
  }
}
