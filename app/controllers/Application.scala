package controllers

import java.sql.Timestamp

import play.api._
import play.api.mvc._

import play.api.db.slick._

import models._

object Application extends Controller {

  def index(pageNum: Int) = DBAction { implicit request =>
  	val pager: Pager[Parent] = Pager[Parent]("トップ", pageNum, Parent.count, Parent.findAll())
  	val ts: Timestamp = new Timestamp(System.currentTimeMillis());
	// Parent.insert(new Parent(0, "TEST", ts, ts))
    Ok(views.html.index(pager))
  }
}