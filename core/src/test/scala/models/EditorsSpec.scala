package models

import java.time.ZonedDateTime

import org.scalatest._
import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback
import org.scalatest.matchers.should.Matchers
import scalikejdbc.config.DBs

class EditorsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  DBs.setup()
  override def fixture(implicit session: DBSession): Unit = {
    SQL("insert into editors values (?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)").bind("testuser","testuser","abc").update().apply()
    SQL("insert into editors values (?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)").bind("moduser","moduser","def").update().apply()
    SQL("insert into editors values (?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)").bind("deluser","deluser","ghi").update().apply()
  }

  override def withFixture(test: OneArgTest): Outcome = {
    using(db()) { db =>
      try {
        db.begin()
        db.withinTx { implicit session =>
          fixture(session)
        }
        withFixture(test.toNoArgTest(db.withinTxSession()))
      } finally {
        db.rollbackIfActive()
      }
    }
  }

  val e = Editors.syntax("e")

  behavior of "Editors"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Editors.find("testuser")
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Editors.findBy(sqls.eq(e.editorId, "testuser"))
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
    val results = Editors.findAllBy(sqls.eq(e.editorId, "testuser"))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Editors.countBy(sqls.eq(e.editorId, "testuser"))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Editors.create(editorId = "createuser", editorName = "createuser", password = "ghi", createTimestamp = ZonedDateTime.now())
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Editors.findAll().head
    val modified = entity.copy(editorId = "modifieduser")
    val updated = Editors.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Editors.findAll().head
    val deleted = Editors.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Editors.find("deluser")
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Editors.findAll()
    entities.foreach(e => Editors.destroy(e))
    val batchInserted = Editors.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}