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

  def form = formProvider()

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
        Future.successful(BadRequest(createView(errors))),
      todo   =>
        repo.insert(todo).map(_ => Redirect(routes.HomeController.display()))
    )
  }

  def complete(id: UUID): Action[AnyContent] = Action.async {

    repo.modify(id, _.copy(state = Completed(java.time.LocalDate.now()))).map {
      case Some(_) => Redirect(routes.HomeController.display())
      case None    => BadRequest("Something has gone wrong")
    }
  }
}
