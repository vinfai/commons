package com.shengpay.commons.springtools.ibatis

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import org.springframework.beans.factory.FactoryBean
import java.lang.reflect.Proxy
import scala.beans.BeanProperty
import collection.mutable._
import collection.JavaConverters._
import org.springframework.orm.ibatis.SqlMapClientTemplate
import java.lang.{ Long => jLong, Integer => jInt }
import com.shengpay.commons.springtools.base._
import com.ibatis.sqlmap.client.SqlMapClient

class DAOFactory[DAO, EO <: BaseEO](val template: SqlMapClientTemplate,val daoInterface: Class[DAO]) extends InvocationHandler with FactoryBean[DAO] {
  def this(sqlMapClient:SqlMapClient,daoInterface: Class[DAO])=this(new SqlMapClientTemplate(sqlMapClient),daoInterface)

  val CLASS_BASEDAO = classOf[BaseDAO[EO]]

  @throws(classOf[Throwable]) def invoke(proxy: Object, daoMethod: Method, args: Array[Object]) =
    daoMethod.getDeclaringClass match {
      case CLASS_BASEDAO => daoMethod.invoke(new BaseDAOImpl(template, daoInterface), args:_*)
      case _ => TemplateMethod(template, daoMethod, args).invoke
    }

  @throws(classOf[Exception]) def getObject(): DAO = Proxy.newProxyInstance(getClass().getClassLoader(), Array(daoInterface), DAOFactory.this).asInstanceOf[DAO]

  def getObjectType(): Class[DAO] = daoInterface

  def isSingleton(): Boolean = true

}
