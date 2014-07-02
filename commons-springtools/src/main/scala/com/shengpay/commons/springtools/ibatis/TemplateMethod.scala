package com.shengpay.commons.springtools.ibatis

import java.lang.{ Integer => jInt }
import java.lang.reflect.Method

import scala.collection.JavaConverters.mutableMapAsJavaMapConverter
import scala.collection.mutable.Map
import com.shengpay.commons.springtools.annotation._

import org.springframework.orm.ibatis.SqlMapClientTemplate


import TemplateMethod._

class TemplateMethod(val template: SqlMapClientTemplate, val daoMethod: Method, val args: Array[Object]) {
  val sqlName = daoMethod.getDeclaringClass.getSimpleName + "." + daoMethod.getName

  val sqlParams = args match {
    case null => null
    case Array() => null
    case Array(x) => x
    case _ => argsToMap
  }

  def invoke() = daoMethod.getName match {
    case n if n.startsWith("query") || n.startsWith("select") => executeQuery
    case n if n.startsWith("update") => METHOD_UPDATE.invoke(template, sqlName, sqlParams)
    case n if n.startsWith("insert") => METHOD_INSERT.invoke(template, sqlName, sqlParams)
    case n if n.startsWith("delete") => METHOD_DELETE.invoke(template, sqlName, sqlParams)
    case _ => throw new RuntimeException("无法找到方法[" + daoMethod + "]对应的ibatis模板类调用方法!")
  }

  def executeQuery = daoMethod.getReturnType match {
    case CLASS_LIST => executeQueryList
    case _ => METHOD_QUERY_OBJECT.invoke(template, sqlName, sqlParams)
  }

  def executeQueryList = getPageInfo match {
    case (pageSize, pageNum) if pageSize > 0 && pageNum > 0 => METHOD_QUERY_LIST_PAGE.invoke(template, sqlName, sqlParams, new jInt((pageNum - 1) * pageSize), new jInt(pageSize))
    case _ => METHOD_QUERY_LIST.invoke(template, sqlName, sqlParams)
  }

  def argsToMap = {
    val m = Map[Object, Object]()
    for (idx <- 0 until args.length) {
      m += ("p" + idx -> args(idx))
      m += ("P" + idx -> args(idx))
    }

    val psas = daoMethod.getParameterAnnotations()
    for (i <- 0 until psas.length; j <- 0 until psas(i).length) {
      if (psas(i)(j).isInstanceOf[ParamName]) m += (psas(i)(j).asInstanceOf[ParamName].value() -> args(i))
    }

    m.asJava
  }

  def getPageInfo = {
    var pageSize: Int = 0
    var pageNum: Int = 0
    val psas = daoMethod.getParameterAnnotations()
    for (i <- 0 until psas.length; j <- 0 until psas(i).length) {
      if (psas(i)(j).isInstanceOf[PageSize]) pageSize = args(i).asInstanceOf[Int]
      if (psas(i)(j).isInstanceOf[PageNum]) pageNum = args(i).asInstanceOf[Int]
    }
    (pageSize, pageNum)
  }
}

object TemplateMethod {
  val CLASS_LIST = classOf[java.util.List[_]]
  val CLASS_TEMPLATE = classOf[SqlMapClientTemplate]
  val METHOD_QUERY_LIST = CLASS_TEMPLATE.getMethod("queryForList", classOf[String], classOf[Object])
  val METHOD_QUERY_LIST_PAGE = CLASS_TEMPLATE.getMethod("queryForList", classOf[String], classOf[Object], classOf[Int], classOf[Int])
  val METHOD_QUERY_OBJECT = CLASS_TEMPLATE.getMethod("queryForObject", classOf[String], classOf[Object])
  val METHOD_UPDATE = CLASS_TEMPLATE.getMethod("update", classOf[String], classOf[Object])
  val METHOD_INSERT = CLASS_TEMPLATE.getMethod("insert", classOf[String], classOf[Object])
  val METHOD_DELETE = CLASS_TEMPLATE.getMethod("delete", classOf[String], classOf[Object])

  def apply(template: SqlMapClientTemplate, daoMethod: Method, args: Array[Object]) = new TemplateMethod(template, daoMethod, args)
}
