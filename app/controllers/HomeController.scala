package controllers

import java.util.UUID

import forms.TodoFormProvider
import javax.inject._
import models.{Completed, Todo}
import play.api.mvc._
import repositories.TodoRepository
import views.html.main

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
                                cc: ControllerComponents,
                                formProvider: TodoFormProvider,
                                repo: TodoRepository,
                                displayView: views.html.display,
                                createView: views.html.create
                              )
  extends BaseController(cc) {

  val form = formProvider()

  def display: Action[AnyContent] = Action.async { implicit request =>
    repo.getAll.map { all =>
      println(all)

      Ok(displayView(all))
    }
  }

  def createV = Action { implicit request =>
    Ok(createView(form))
  }

  def create: Action[AnyContent] = Action.async { implicit request =>

    form.bindFromRequest().fold(
      errors =>
        Future.successful(BadRequest(errors.errors.map(_.message).mkString("\n"))),
      todo   =>
        repo.insert(todo).map(_ => Ok("Todo created"))
    )
  }

  def complete(id: UUID): Action[AnyContent] = Action.async {

    repo.modify(id, _.copy(state = Completed(java.time.LocalDate.now()))).map {
      case Some(_) => Ok(s"Todo $id has been set to complete")
      case None    => BadRequest("Something has gone wrong")
    }
  }
}
