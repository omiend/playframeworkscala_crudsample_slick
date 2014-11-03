package models

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

/** Parent */
case class Parent(id: Long, name: String, createDate: Timestamp, updateDate: Timestamp)
class ParentTable(tag: Tag) extends Table[Parent](tag, "parent") {
  def id         = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name", O.NotNull)
  def createDate = column[Timestamp]("create_date")
  def updateDate = column[Timestamp]("update_date")
  def * = (id, name, createDate, updateDate) <> ((Parent.apply _).tupled, Parent.unapply)
}
object Parent {
	// DDL  
  lazy val query = TableQuery[ParentTable]
	// 全件取得
  def findAll()(implicit s: Session): List[Parent] = query.list
	// 全件数取得
  def count()(implicit s: Session): Int = query.list.size
}

/** Child */
case class Child(id: Long, name: String, parentId: Long, createDate: Timestamp, updateDate: Timestamp)
class ChildTable(tag: Tag) extends Table[Child](tag, "child") {
  def id         = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name", O.NotNull)
  def parentId   = column[Long]("parent_id")
  def createDate = column[Timestamp]("create_date")
  def updateDate = column[Timestamp]("update_date")
  def * = (id, name, parentId, createDate, updateDate) <> ((Child.apply _).tupled, Child.unapply)
  def parent = foreignKey("parent_id", parentId, Parent.query)(_.id)
}
object Child {
	// DDL  
  lazy val query = TableQuery[ChildTable]
	// 全件取得
  def findAll()(implicit s: Session): List[Child] = query.list
	// 全件数取得
  def count()(implicit s: Session): Int = query.list.size
}
