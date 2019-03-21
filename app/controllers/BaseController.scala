package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

class BaseController(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val ec = cc.executionContext
}