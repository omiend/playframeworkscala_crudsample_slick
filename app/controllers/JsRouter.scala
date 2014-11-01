package controllers

import play.api._, mvc._

/**
 * Ajax用 javascriptRouter
 */
object JsRouter extends Controller {
  
  def javascriptRoutes = Action { implicit request =>
      import routes.javascript._
      Ok(
        Routes.javascriptRouter("jsRoutes")(
           routes.javascript.AjaxController.ajaxHelloWorld
        )
      ).as("text/javascript")
  }
}
