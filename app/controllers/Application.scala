package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._

import play.api.db.slick._

import org.joda.time.DateTime

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index("トップ"))
  }

  def parent(pageNum: Int) = DBAction { implicit request =>
  	val pager: Pager[(Parent, List[Child])] = Pager[(Parent, List[Child])]("トップ", pageNum, Parent.count, Seq.empty)
  	pager.dataList = Parent.findOffsetWithChildList((pager.pageNum * pager.maxListCount - pager.maxListCount), pager.maxListCount)
    Ok(views.html.parent(pager))
  }

  val parentForm = Form(
    mapping(
      "id"         -> ignored(None: Option[Long]),
      "name"       -> nonEmptyText,
      "gender"     -> nonEmptyText,
      "birthDate"  -> jodaDate,
      "createDate" -> optional(jodaDate),
      "updateDate" -> optional(jodaDate)
    )(Parent.apply)(Parent.unapply)
  )

  def getGenderOptions: Seq[(String, String)] = Seq("0" -> "男", "1" -> "女")

  def createParent(pageNum: Int) = Action { implicit request =>
    val pager: Pager[Parent] = Pager[Parent]("Create Parent Record", pageNum, 0, Seq.empty)
    Ok(views.html.createParent(pager, parentForm, getGenderOptions))
  }

  def insertParent(pageNum: Int) = DBAction { implicit request =>
    parentForm.bindFromRequest.fold(
      formWithErrors => {
        val pager: Pager[Parent] = Pager[Parent]("Create Parent Record", pageNum, 0, Seq.empty)
        BadRequest(views.html.createParent(pager, formWithErrors, getGenderOptions))
      },
      parent => {
        val today: DateTime = new DateTime
        Parent.insert(new Parent(None, parent.name, parent.gender, parent.birthDate, Some(today), Some(today)))
        Redirect(routes.Application.parent(pageNum))
      }
    )
  }

  def editParent(pageNum: Int, id: Long) = DBAction { implicit request =>
    val pager: Pager[Parent] = Pager[Parent]("親レコード更新", pageNum, 0, Seq.empty)
    Parent.findById(id).map { parent =>
      Ok(views.html.editParent(pager, id, parentForm.fill(parent), getGenderOptions))
    }.getOrElse(NotFound)
  }

  def updateParent(pageNum: Int, id: Long) = DBAction { implicit request =>
    parentForm.bindFromRequest.fold(
      formWithErrors => {
        val pager: Pager[Parent] = Pager[Parent]("親レコード更新", pageNum, 0, Seq.empty)
        BadRequest(views.html.editParent(pager, id, formWithErrors, getGenderOptions))
      },
      parent => {
        Parent.findById(id).map { targetParent =>
          targetParent.name = parent.name
          targetParent.gender = parent.gender
          targetParent.birthDate = parent.birthDate
          targetParent.updateDate = Some(new DateTime)
          Parent.update(id, targetParent)
          Redirect(routes.Application.parent(pageNum))
        }.getOrElse(NotFound)
      }
    )
  }

  def deleteParent(pageNum: Int, id: Long) = DBAction { implicit request =>
    Parent.delete(id)
    Redirect(routes.Application.parent(pageNum))
  }



  def child(pageNum: Int) = DBAction { implicit request =>
    val pager: Pager[(Child, Parent)] = Pager[(Child, Parent)]("トップ", pageNum, Child.count, Seq.empty)
    pager.dataList = Child.findByIdWithParent((pager.pageNum * pager.maxListCount - pager.maxListCount), pager.maxListCount)
    Ok(views.html.child(pager))
  }

  val childForm = Form(
    mapping(
      "id"         -> ignored(None: Option[Long]),
      "parentId"   -> ignored(None: Option[Long]),
      "name"       -> nonEmptyText,
      "createDate" -> optional(jodaDate),
      "updateDate" -> optional(jodaDate)
    )(Child.apply)(Child.unapply)
  )

  def createChild(pageNum: Int, parentId: Long) = Action { implicit request =>
    val pager: Pager[Child] = Pager[Child]("子レコード作成", pageNum, 0, Seq.empty)
    Ok(views.html.createChild(pager, parentId, childForm))
  }

  def insertChild(pageNum: Int, parentId: Long) = DBAction { implicit request =>
    childForm.bindFromRequest.fold(
      formWithErrors => {
        val pager: Pager[Child] = Pager[Child]("子レコード作成", pageNum, 0, Seq.empty)
        BadRequest(views.html.createChild(pager, parentId, formWithErrors))
      },
      child => {
        val today: DateTime = new DateTime
        Child.insert(new Child(None, Some(parentId), child.name, Some(today), Some(today)))
        Redirect(routes.Application.parent(pageNum))
      }
    )
  }

  def editChild(pageNum: Int, id: Long, parentId: Long) = DBAction { implicit request =>
    val pager: Pager[Child] = Pager[Child]("子レコード更新", pageNum, 0, Seq.empty)
    Child.findById(id).map { child =>
      Ok(views.html.editChild(pager, id, parentId, childForm.fill(child)))
    }.getOrElse(NotFound)
  }

  def updateChild(pageNum: Int, id: Long, parentId: Long) = DBAction { implicit request =>
    childForm.bindFromRequest.fold(
      formWithErrors => {
        val pager: Pager[Child] = Pager[Child]("子レコード更新", pageNum, 0, Seq.empty)
        BadRequest(views.html.editChild(pager, id, parentId, formWithErrors))
      },
      child => {
        Child.findById(id).map { targetChild =>
          targetChild.name = child.name
          targetChild.updateDate = Some(new DateTime)
          Child.update(id, targetChild)
          Redirect(routes.Application.child(pageNum))
        }.getOrElse(NotFound)
      }
    )
  }

  def deleteChild(pageNum: Int, id: Long) = DBAction { implicit request =>
    Child.delete(id)
    Redirect(routes.Application.child(pageNum))
  }




  def pageNation(pageNum: Int, page: String) = Action { implicit request =>
    page match {
      case "parent" => Redirect(routes.Application.parent(pageNum))
      case "child"  => Redirect(routes.Application.child(pageNum))
      case _        => Redirect(routes.Application.parent(pageNum))
    }
  }
}