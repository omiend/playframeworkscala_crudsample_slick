package models

import java.sql.Timestamp

import play.api.db.DB
import play.api.Play.current

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.{ MTable, createModel }
import scala.slick.driver.JdbcDriver


case class Parent(id: Long, name: String, createDate: Timestamp, updateDate: Timestamp)

/**
 * ③ テーブルスキーマの定義
 */
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
  def findAll()(implicit s: Session): List[Parent] = {
    query.list
  }
	// 全件取得
  def count()(implicit s: Session): Int = {
    query.list.size
  }
}