package models

import scala.slick.driver.MySQLDriver.simple._

import org.joda.time.DateTime
import java.sql.Timestamp
// import JodaToSqlMapper._

/** Parent */
case class Parent(id: Option[Long] = None, name: String, createDate: org.joda.time.DateTime, updateDate: org.joda.time.DateTime)
object Parent {
	class ParentTable(tag: Tag) extends Table[Parent](tag, "parent") {
	  def id         = column[Long]("id", O.PrimaryKey, O.AutoInc)
	  def name       = column[String]("name", O.NotNull)
	  def createDate = column[DateTime]("create_date")
	  def updateDate = column[DateTime]("update_date")
	  def * = (id, name, createDate, updateDate) <> ((Parent.apply _).tupled, Parent.unapply)
	}
	// DDL  
  lazy val parentQuery = TableQuery[ParentTable]
	// 全件取得
  def findAll()(implicit s: Session): List[Parent] = parentQuery.list
	// 全件数取得
  def count()(implicit s: Session): Int = parentQuery.list.size
  // Offset取得
  def findOffset(offset: Int, limit: Int)(implicit s: Session): List[Parent] = parentQuery.drop(offset).take(limit).list
  def findOffsetAndChildList(offset: Int, limit: Int)(implicit s: Session): List[Parent] = parentQuery.drop(offset).take(limit).list
}

/** Child */
case class Child(id: Option[Long] = None, name: String, parentId: Option[Long] = None, createDate: org.joda.time.DateTime, updateDate: org.joda.time.DateTime)
object Child {
	class ChildTable(tag: Tag) extends Table[Child](tag, "child") {
	  def id         = column[Long]("id", O.PrimaryKey, O.AutoInc)
	  def name       = column[String]("name", O.NotNull)
	  def parentId   = column[Long]("parent_id")
	  def createDate = column[DateTime]("create_date")
	  def updateDate = column[DateTime]("update_date")
	  def * = (id, name, parentId, createDate, updateDate) <> ((Child.apply _).tupled, Child.unapply)
	  def parent = foreignKey("parent_id", parentId, Parent.parentQuery)(_.id)
	}
	// DDL  
  lazy val childQuery = TableQuery[ChildTable]
  lazy val parentQuery = TableQuery[Parent.ParentTable]
	// 全件取得
  def findAll()(implicit s: Session): List[Child] = childQuery.list
	// 全件数取得
  def count()(implicit s: Session): Int = childQuery.list.size
  // 親Idを指定して取得
  def findByParentId(parentId: Long)(implicit s: Session): List[Child] = childQuery.filter(_.parentId === parentId).list
  // 親レコードを結合して取得
  def findByIdWithParent(offset: Int, limit: Int)(implicit s: Session): List[(Child, Parent)] = {
  	(for {
		  c <- childQuery
		  p <- parentQuery if c.parentId === p.id
		} yield (c, p)).drop(offset).take(limit).list
  }
}

object JodaToSqlMapper {
  implicit val dateTimeToDate = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    timestamp => new DateTime(timestamp))
}