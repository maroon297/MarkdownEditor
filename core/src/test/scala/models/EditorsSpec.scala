package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{ZonedDateTime}


class EditorsSpec extends Specification {

  "Editors" should {

    val e = Editors.syntax("e")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Editors.find("MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Editors.findBy(sqls.eq(e.editorId, "MyString"))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Editors.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Editors.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Editors.findAllBy(sqls.eq(e.editorId, "MyString"))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Editors.countBy(sqls.eq(e.editorId, "MyString"))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Editors.create(editorId = "MyString", editorName = "MyString", password = "MyString", createTimestamp = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Editors.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Editors.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Editors.findAll().head
      val deleted = Editors.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Editors.find("MyString")
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Editors.findAll()
      entities.foreach(e => Editors.destroy(e))
      val batchInserted = Editors.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
