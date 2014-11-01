package controllers

import play.api._
import play.api.mvc._

/**
 * Ajax用 処理
 */
object AjaxController extends Controller {

  def ajaxHelloWorld = Action { request =>
      Ok
  }

}
