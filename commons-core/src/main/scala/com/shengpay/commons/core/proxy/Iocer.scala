/**
 *
 */
package com.shengpay.commons.core.proxy

import java.lang.reflect.Field
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import Iocer._
import javax.annotation.Resource
import com.shengpay.commons.core.base.Registry
import javax.annotation.Resource
import com.shengpay.commons.core.base.Property

object Iocer {
  private lazy val resourceFinder = FieldAnnotationFinder(null, classOf[Resource])
  private lazy val valueFinder = FieldAnnotationFinder(null, classOf[Property])

  def ioc(target: Object) {
    resourceFinder.getFields(target).foreach(setResourceValue(target, _))
    valueFinder.getFields(target).foreach(setEnvValue(target, _))
  }

  def setResourceValue(single: Any, field: Field) {
    val value = Registry.queryBean(field.getType())
    field.setAccessible(true)
    field.set(single, value)
  }

  def setEnvValue(single: Any, field: Field) {
    val value = Registry.queryProperty(field.getAnnotation(classOf[Property]).value())
    field.setAccessible(true)
    field.set(single, value)
  }
}
