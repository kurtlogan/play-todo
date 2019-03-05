package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class TodoBaseController(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val ec: ExecutionContext = cc.executionContext
}