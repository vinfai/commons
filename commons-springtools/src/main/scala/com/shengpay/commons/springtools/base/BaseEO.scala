package com.shengpay.commons.springtools.base

import com.shengpay.commons.core.base.BaseObject
import java.lang.{ Long => jLong }
import java.util.Date
import scala.beans.BeanProperty
import java.lang.{Long => jLong}

class BaseEO(@BeanProperty var id: jLong, @BeanProperty var version: jLong=0, @BeanProperty var createTime: Date=new Date, @BeanProperty var updateTime: Date=new Date) extends BaseObject {
  def this()=this(null,null,null,null)

}