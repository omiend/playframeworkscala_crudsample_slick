package controllers

// import java.sql.Timestamp
// val ts: Timestamp = new Timestamp(System.currentTimeMillis());

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

import models._

import play.api.db.slick._

object Application extends Controller {

  def parent(pageNum: Int) = DBAction { implicit request =>
  	val pager: Pager[Parent] = Pager[Parent]("トップ", pageNum, Parent.count, Seq.empty)
  	pager.dataList = Parent.findOffset((pager.pageNum * pager.maxListCount - pager.maxListCount), pager.maxListCount)
    Ok(views.html.parent(pager))
  }
  def child(pageNum: Int) = DBAction { implicit request =>
  	val pager: Pager[(Child, Parent)] = Pager[(Child, Parent)]("トップ", pageNum, Child.count, Seq.empty)
  	pager.dataList = Child.findByIdWithParent((pager.pageNum * pager.maxListCount - pager.maxListCount), pager.maxListCount)
    Ok(views.html.child(pager))
  }

  /** Parent form */
  val parentForm = Form(
    mapping(
      "id"         -> ignored(None: Option[Long]),
      "name"       -> nonEmptyText,
      "createDate" -> jodaDate,
      "updateDate" -> jodaDate
    )(Parent.apply)(Parent.unapply)
  )

  def createParent(pageNum: Int) = DBAction { implicit request =>
    val pager: Pager[Parent] = Pager[Parent]("トップ", pageNum, Parent.count, Seq.empty)
    Ok(views.html.createParent(pager, parentForm))
  }

  def insertParent(pageNum: Int) = DBAction { implicit request =>
    // val parent = parentForm.bindFromRequest.get
    parentForm.bindFromRequest.fold(
      formWithErrors => {
        val pager: Pager[Parent] = Pager[Parent]("トップ", pageNum, Parent.count, Seq.empty)
        BadRequest(views.html.createParent(pager, formWithErrors))
      },
      parent => {

        Parent.insert(parent)
        Redirect(routes.Application.parent(pageNum))
      }
    )
  }

  /** Child form */
  val childForm = Form(
    mapping(
      "id" -> ignored(None: Option[Long]),
      "name" -> nonEmptyText,
      "parentId" -> ignored(None: Option[Long]),
      "createDate" -> jodaDate,
      "updateDate" -> jodaDate
    )(Child.apply)(Child.unapply)
  )

  def pageNation(pageNum: Int, page: String) = Action { implicit request =>
    page match {
      case "parent" => Redirect(routes.Application.parent(pageNum))
      case "child"  => Redirect(routes.Application.child(pageNum))
      case _        => Redirect(routes.Application.parent(pageNum))
    }
  }
}