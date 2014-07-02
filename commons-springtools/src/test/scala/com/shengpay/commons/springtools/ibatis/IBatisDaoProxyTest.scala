package com.shengpay.commons.springtools.ibatis

import java.util.List
import scala.collection.JavaConverters.mapAsJavaMapConverter
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.orm.ibatis.SqlMapClientTemplate
import com.shengpay.commons.springtools.annotation._
import com.shengpay.commons.springtools.base._

trait DAO extends BaseDAO[EO] {
  def query0(): Object
  def query1(id: Long): List[_]
  def query2(id: Long, @ParamName("myName") name: String, @PageSize pageSize: Int, @PageNum pageNum: Int): List[_]
  def update(id: Long)
  def insertEO(eo: Any)
  def delete(id: Long)
}

class EO(id: Long, name: String) extends BaseEO {}

class IBatisDaoProxyTest {

  var template: SqlMapClientTemplate = null
  var dao: DAO = null

  @Before def init() {
    template = mock(classOf[SqlMapClientTemplate])
    dao = new DAOFactory[DAO, EO](template, classOf[DAO]).getObject
  }

  @Test def testSelect() {
    dao.select(1l)
    verify(template).queryForObject("DAOAbstract.select", Map("noWait" -> false, "isLock" -> false, "id" -> 1))
  }

  @Test def testQuery1() {
    dao.query0()
    verify(template).queryForObject("DAO.query0", null)
  }
  @Test def testQuery2() {
    dao.query1(1l)
    verify(template).queryForList("DAO.query1", 1l)
  }

  @Test def testQuery3() {
    dao.query2(1l, "lindongcheng", 10, 2)
    verify(template).queryForList("DAO.query2", Map("p0" -> 1l, "p1" -> "lindongcheng", "p2" -> 10, "p3" -> 2, "P0" -> 1l, "P1" -> "lindongcheng", "P2" -> 10, "P3" -> 2, "myName" -> "lindongcheng").asJava, 10, 10)
  }

//  @Test 
  def testPress {
	  val times = 10000
    val t1 = System.currentTimeMillis()
    for (_ <- 1 to times) yield dao.query2(1l, "lindongcheng", 10, 2)
    val t2 = System.currentTimeMillis()
    for (_ <- 1 to times) dao.query1(1l)
    val t3 = System.currentTimeMillis()
    for (_ <- 1 to times) dao.query0
    val t4 = System.currentTimeMillis()
    
    println((t2-t1)/times.asInstanceOf[Double])
    println((t3-t2)/times.asInstanceOf[Double])
    println((t4-t3)/times.asInstanceOf[Double])

  }

  @Test def testUpdate() {
    dao.update(1l)
    verify(template).update("DAO.update", 1l)
  }

  @Test def testInsert() {
    val eo = new EO(1l, "lindongcheng")
    dao.insertEO(eo)
    verify(template).insert("DAO.insertEO", eo)
  }

  @Test def testDelete() {
    dao.delete(1l)
    verify(template).delete("DAO.delete", 1l)
  }
}




