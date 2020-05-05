package models

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Editors(
  editorId: String,
  editorName: String,
  password: String,
  createTimestamp: ZonedDateTime,
  updateTimestamp: Option[ZonedDateTime] = None) {

  def save()(implicit session: DBSession = Editors.autoSession): Editors = Editors.save(this)(session)

  def destroy()(implicit session: DBSession = Editors.autoSession): Int = Editors.destroy(this)(session)

}


object Editors extends SQLSyntaxSupport[Editors] {

  override val tableName = "editors"

  override val columns = Seq("editor_id", "editor_name", "password", "create_timestamp", "update_timestamp")

  def apply(e: SyntaxProvider[Editors])(rs: WrappedResultSet): Editors = apply(e.resultName)(rs)
  def apply(e: ResultName[Editors])(rs: WrappedResultSet): Editors = new Editors(
    editorId = rs.get(e.editorId),
    editorName = rs.get(e.editorName),
    password = rs.get(e.password),
    createTimestamp = rs.get(e.createTimestamp),
    updateTimestamp = rs.get(e.updateTimestamp)
  )

  val e = Editors.syntax("e")

  override val autoSession = AutoSession

  def find(editorId: String)(implicit session: DBSession = autoSession): Option[Editors] = {
    withSQL {
      select.from(Editors as e).where.eq(e.editorId, editorId)
    }.map(Editors(e.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Editors] = {
    withSQL(select.from(Editors as e)).map(Editors(e.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Editors as e)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Editors] = {
    withSQL {
      select.from(Editors as e).where.append(where)
    }.map(Editors(e.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Editors] = {
    withSQL {
      select.from(Editors as e).where.append(where)
    }.map(Editors(e.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Editors as e).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    editorId: String,
    editorName: String,
    password: String,
    createTimestamp: ZonedDateTime,
    updateTimestamp: Option[ZonedDateTime] = None)(implicit session: DBSession = autoSession): Editors = {
    withSQL {
      insert.into(Editors).namedValues(
        column.editorId -> editorId,
        column.editorName -> editorName,
        column.password -> password,
        column.createTimestamp -> createTimestamp,
        column.updateTimestamp -> updateTimestamp
      )
    }.update.apply()

    Editors(
      editorId = editorId,
      editorName = editorName,
      password = password,
      createTimestamp = createTimestamp,
      updateTimestamp = updateTimestamp)
  }

  def batchInsert(entities: collection.Seq[Editors])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("editorId") -> entity.editorId,
        Symbol("editorName") -> entity.editorName,
        Symbol("password") -> entity.password,
        Symbol("createTimestamp") -> entity.createTimestamp,
        Symbol("updateTimestamp") -> entity.updateTimestamp))
    SQL("""insert into editors(
      editor_id,
      editor_name,
      password,
      create_timestamp,
      update_timestamp
    ) values (
      {editorId},
      {editorName},
      {password},
      {createTimestamp},
      {updateTimestamp}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Editors)(implicit session: DBSession = autoSession): Editors = {
    withSQL {
      update(Editors).set(
        column.editorId -> entity.editorId,
        column.editorName -> entity.editorName,
        column.password -> entity.password,
        column.createTimestamp -> entity.createTimestamp,
        column.updateTimestamp -> entity.updateTimestamp
      ).where.eq(column.editorId, entity.editorId)
    }.update.apply()
    entity
  }

  def destroy(entity: Editors)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Editors).where.eq(column.editorId, entity.editorId) }.update.apply()
  }

}
