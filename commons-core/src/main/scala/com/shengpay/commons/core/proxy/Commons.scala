/**
 *
 */
package com.shengpay.commons.core.proxy
import java.lang.reflect.Field
import scala.Array.canBuildFrom

case class FieldAnnotationFinder(pkgNames: Iterable[String], antnClass: Class[_ <: java.lang.annotation.Annotation]) {
  private val clazz2FieldMap = scala.collection.mutable.Map[Class[_], Array[Field]]()

  def getFields(target: Any): Array[Field] = {
    if (target == null || !isClassNeedIoc(target.getClass)) return Array()

    var fields = clazz2FieldMap.getOrElse(target.getClass, null)
    if (fields == null) {
      fields = getFieldsByNew(target.getClass)
      clazz2FieldMap(target.getClass) = fields
    }
    fields
  }

  def getFieldsByNew(clazz: Class[_]): Array[Field] = {
    if (clazz == null || !isClassNeedIoc(clazz)) return Array()
    val fields = clazz.getDeclaredFields().filter(_.getAnnotation(antnClass) != null)
    fields ++ (getFieldsByNew(clazz.getSuperclass()))
  }

  def isClassNeedIoc(clazz: Class[_]) = pkgNames==null || pkgNames.filter(clazz.getPackage.getName.startsWith(_)).nonEmpty

}
