package models

import org.scalatest._
import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback


class EditorsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val e = Editors.syntax("e")

  behavior of "Editors"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Editors.find("MyString")
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Editors.findBy(sqls.eq(e.editorId, "MyString"))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Editors.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Editors.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Editors.findAllBy(sqls.eq(e.editorId, "MyString"))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Editors.countBy(sqls.eq(e.editorId, "MyString"))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Editors.create(editorId = "MyString", editorName = "MyString", password = "MyString", createTimestamp = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Editors.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Editors.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Editors.findAll().head
    val deleted = Editors.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Editors.find("MyString")
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Editors.findAll()
    entities.foreach(e => Editors.destroy(e))
    val batchInserted = Editors.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
