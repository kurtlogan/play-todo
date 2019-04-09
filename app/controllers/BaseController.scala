package controllers

import play.api.i18n.Messages
import play.api.mvc.{AbstractController, ControllerComponents, Request}

class BaseController(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val ec = cc.executionContext
  implicit def messages(implicit r: Request[_]) =
    cc.messagesApi.preferred(r)
}