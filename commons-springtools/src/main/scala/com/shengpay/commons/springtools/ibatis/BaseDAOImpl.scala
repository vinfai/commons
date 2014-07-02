/**
 *
 */
package com.shengpay.commons.springtools.ibatis;

import java.util.{ Map => jMap, List => jList }
import java.lang.{ Long => jLong }
import javax.annotation.Resource
import org.springframework.dao.DataAccessException
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.orm.ibatis.SqlMapClientTemplate
import org.springframework.orm.ibatis.SqlMapClientCallback
import java.sql.SQLException
import com.ibatis.sqlmap.client.SqlMapExecutor
import collection.mutable._
import collection.JavaConverters._
import java.util.Date
import com.shengpay.commons.springtools.base._

class BaseDAOImpl[EO <: BaseEO](val ibatisTemplete: SqlMapClientTemplate, val daoInterface: Class[_]) extends BaseDAO[EO] {
  val SQLNAME_PREFIX = daoInterface.getSimpleName + "Abstract."
  val SQLNAME_INSERT_NOID_DYNAMIC = SQLNAME_PREFIX + "insert"
  val SQLNAME_INSERT_NOID_NOTDYNAMIC = SQLNAME_PREFIX + "insert_notdynamic"
  val SQLNAME_INSERT_HASID_DYNAMIC = SQLNAME_PREFIX + "insertHasId"
  val SQLNAME_INSERT_HASID_NOTDYNAMIC = SQLNAME_PREFIX + "insertHasId_notdynamic"
  val SQLNAME_SELECT_BYID = SQLNAME_PREFIX + "select"
  val SQLNAME_SELECT_BYIDLIST = SQLNAME_PREFIX + "selectListByIdList"
  val SQLNAME_SELECT_BYEO = SQLNAME_PREFIX + "selectEOByEO"
  val SQLNAME_COUNT_BYEO = SQLNAME_PREFIX + "countByEO"
  val SQLNAME_UPDATE_ALLFIELD = SQLNAME_PREFIX + "update"
  val SQLNAME_UPDATE_BYFIELD = SQLNAME_PREFIX + "updateByField"
  val SQLNAME_DELETE = SQLNAME_PREFIX + "delete"

  def insert(eo: EO) = {
    eo.getId match {
      case id if id != null => ibatisTemplete.insert(SQLNAME_INSERT_NOID_DYNAMIC, eo)
      case _ => eo.id = ibatisTemplete.insert(SQLNAME_INSERT_NOID_DYNAMIC, eo).asInstanceOf[jLong]
    }
    eo.id
  }

  def insertBatchHasId(eoList: jList[EO]): Unit =
    ibatisTemplete.execute[Object](new SqlMapClientCallback[Object] {
      @throws(classOf[SQLException]) def doInSqlMapClient(executor: SqlMapExecutor): Object = {
        executor.startBatch();
        eoList.asScala.foreach(executor.insert(SQLNAME_INSERT_HASID_NOTDYNAMIC, _))
        executor.executeBatch()
        null
      }
    })

  def insertBatch(eoList: jList[EO]) =
    ibatisTemplete.execute[jList[jLong]](new SqlMapClientCallback[jList[jLong]] {
      @throws(classOf[SQLException]) def doInSqlMapClient(executor: SqlMapExecutor): jList[jLong] = {
        executor.startBatch
        val insertReturnList = for (eo <- eoList.asScala) yield executor.insert(SQLNAME_INSERT_NOID_NOTDYNAMIC, eo).asInstanceOf[jLong]
        executor.executeBatch
        insertReturnList.asJava
      }
    })

  def select(id: jLong) = select(id, false, false)

  def selectAndLock(id: jLong) = select(id, true, false)

  def selectEOByEO(eo: EO): EO = ibatisTemplete.queryForObject(SQLNAME_SELECT_BYEO, eo).asInstanceOf[EO]

  def selectListByEO(eo: EO) = ibatisTemplete.queryForList(SQLNAME_SELECT_BYEO, eo).asInstanceOf[jList[EO]]

  def selectListByEO(eo: EO, pageNO: Integer, pageSize: Integer) = ibatisTemplete.queryForList(SQLNAME_SELECT_BYEO, eo, (pageNO - 1) * pageSize, pageSize).asInstanceOf[jList[EO]]

  def selectListByIdList(idList: jList[jLong], lockEO: Boolean) = ibatisTemplete.queryForList(SQLNAME_SELECT_BYIDLIST, Map("idList" -> idList, "isLock" -> lockEO).asJava).asInstanceOf[jList[EO]]

  def select(id: jLong, isLock: Boolean, noWait: Boolean): EO = {
    val paramMap = Map("id" -> id, "isLock" -> isLock, "noWait" -> noWait)
    try {
      return ibatisTemplete.queryForObject(SQLNAME_SELECT_BYID, paramMap.asJava).asInstanceOf[EO]
    } catch {
      case e: DataAccessException =>
        if (noWait) {
          return null.asInstanceOf[EO]
        } else {
          throw e;
        }
    }
  }

  def countByEO(eo: EO): Integer = {
    val count = ibatisTemplete.queryForObject(SQLNAME_COUNT_BYEO, eo).asInstanceOf[Integer]
    return if (count != null) count else 0
  }

  def update(eo: EO) = update(eo, SQLNAME_UPDATE_ALLFIELD)

  def updateByField(eo: EO) = update(eo, SQLNAME_UPDATE_BYFIELD)

  def update(eo: EO, sqlName: String): Integer =
    ibatisTemplete.update(sqlName, eo) match {
      case c if c == 0 => throw new OptimisticLockingFailureException("更新纪录[" + eo + "]时发生乐观锁并发异常")
      case c => eo.version += 1; eo.updateTime = new Date; c
    }

  def updateBatch(eoList: jList[EO]) = updateBatch(eoList, SQLNAME_UPDATE_ALLFIELD)

  def updateBatch(eoList: jList[EO], sqlName: String) = ibatisTemplete.execute[jList[Integer]](new SqlMapClientCallback[jList[Integer]] {
    @throws(classOf[SQLException]) def doInSqlMapClient(executor: SqlMapExecutor): jList[Integer] = {
      executor.startBatch
      val insertReturnList = for (eo <- eoList.asScala) yield executor.update(sqlName, eo).asInstanceOf[Integer]
      executor.executeBatch
      insertReturnList.asJava
    }
  })

  def updateBatchByField(eoList: jList[EO]) = updateBatch(eoList, SQLNAME_UPDATE_BYFIELD)

  def delete(eo: EO): Integer =
    ibatisTemplete.delete(SQLNAME_DELETE, eo) match {
      case c if c == 0 => throw new OptimisticLockingFailureException("删除纪录[" + eo + "]时发生乐观锁并发异常")
      case c => c
    }
}
