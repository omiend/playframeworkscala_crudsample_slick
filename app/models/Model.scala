package models

import org.joda.time.DateTime

import scala.slick.driver.MySQLDriver.simple._
import com.github.tototoshi.slick.MySQLJodaSupport._

/** Parent */
case class Parent(id: Option[Long] = None
                 ,var name: String
                 ,var gender    : String
                 ,var birthDate : DateTime
                 ,var createDate: Option[DateTime] = None
                 ,var updateDate: Option[DateTime] = None
                 )
class ParentTable(tag: Tag) extends Table[Parent](tag, "parent") {
  def id         = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name", O.NotNull)
  def gender     = column[String]("gender", O.NotNull)
  def birthDate  = column[DateTime]("birth_date")
  def createDate = column[Option[DateTime]]("create_date")
  def updateDate = column[Option[DateTime]]("update_date")
  def * = (id, name, gender, birthDate, createDate, updateDate) <> ((Parent.apply _).tupled, Parent.unapply)
}
object Parent {
  // DDL  
  lazy val parentQuery = TableQuery[ParentTable]
  lazy val childQuery = TableQuery[ChildTable]
  // 全件取得
  def findAll()(implicit s: Session): List[Parent] = parentQuery.list
  // 全件数取得
  def count()(implicit s: Session): Int = parentQuery.list.size
  // Idを指定して１件取得
  def findById(id: Long)(implicit s: Session): Option[Parent] = parentQuery.filter(_.id === id).firstOption
  // Idを指定して１件取得(紐づく子レコードも取得)
  def findByIdWithChildList(id: Long)(implicit s: Session): Option[(Parent, List[Child])] = {
    Parent.findById(id).map { parent =>
      (parent, Child.findByParentId(id))
    }
  }
  // Offset取得
  def findOffset(offset: Int, limit: Int)(implicit s: Session): List[Parent] = parentQuery.drop(offset).take(limit).list
  // Offset取得(紐づく子レコードも取得)
  def findOffsetWithChildList(offset: Int, limit: Int)(implicit s: Session): List[(Parent, List[Child])] = {
    (for {
      p <- parentQuery.drop(offset).take(limit).list
    } yield (p, Child.findByParentId(p.id.get)))
  }
  // 親レコードをInsert
  def insert(parent: Parent)(implicit s: Session) {
    parentQuery.insert(parent)
  }
  // 親レコードをUpdate
  def update(id: Long, parent: Parent)(implicit s: Session) {
    parentQuery.filter(_.id === id).update(parent)
  }
  // 親レコードをDelete
  def delete(id: Long)(implicit s: Session) {
    parentQuery.filter(_.id === id).delete
  }
}

/** Child */
case class Child(id            : Option[Long] = None
                ,parentId      : Option[Long] = None
                ,var name      : String
                ,var createDate: Option[DateTime] = None
                ,var updateDate: Option[DateTime] = None
                )
class ChildTable(tag: Tag) extends Table[Child](tag, "child") {
  def id         = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name", O.NotNull)
  def parentId   = column[Option[Long]]("parent_id")
  def createDate = column[Option[DateTime]]("create_date")
  def updateDate = column[Option[DateTime]]("update_date")
  def * = (id, parentId, name, createDate, updateDate) <> ((Child.apply _).tupled, Child.unapply)
  def parent = foreignKey("parent_id", parentId, Parent.parentQuery)(_.id)
}
object Child {
  // DDL  
  lazy val childQuery = TableQuery[ChildTable]
  lazy val parentQuery = TableQuery[ParentTable]
  // 全件取得
  def findAll()(implicit s: Session): List[Child] = childQuery.list
  // 全件数取得
  def count()(implicit s: Session): Int = childQuery.list.size
  // Idを指定して１件取得
  def findById(id: Long)(implicit s: Session): Option[Child] = childQuery.filter(_.id === id).firstOption
  // 親Idを指定して取得
  def findByParentId(parentId: Long)(implicit s: Session): List[Child] = childQuery.filter(_.parentId === parentId).list
  // 親レコードを結合して取得
  def findByIdWithParent(offset: Int, limit: Int)(implicit s: Session): List[(Child, Parent)] = {
    (for {
      c <- childQuery.drop(offset).take(limit)
      p <- parentQuery if c.parentId === p.id
    } yield (c, p)).list
  }
  // 子レコードをInsert
  def insert(child: Child)(implicit s: Session) {
    childQuery.insert(child)
  }
  // 子レコードをUpdate
  def update(id: Long, child: Child)(implicit s: Session) {
    childQuery.filter(_.id === id).update(child)
  }
  // 子レコードをDelete
  def delete(id: Long)(implicit s: Session) {
    parentQuery.filter(_.id === id).delete
  }
}
