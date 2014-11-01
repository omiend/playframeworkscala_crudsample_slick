package controllers

import play.api._
import play.api.mvc._

import models._

object Application extends Controller {

  def index(pageNum: Int) = Action { implicit request =>
  	val dummy: Option[Dummy] = Some(new Dummy)
  	val pager: Pager[Dummy] = Pager[Dummy]("トップ", 1, 0, dummy, Seq.empty)
    Ok(views.html.index(pager))
  }

}