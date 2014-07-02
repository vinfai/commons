package com.shengpay.commons.springtools.base

import java.lang.{Long=>jLong}
import java.util.{List=>jList}

trait BaseDAO[EO <: BaseEO] {
  def insert(eo: EO): jLong
  def insertBatchHasId(eoList: jList[EO]): Unit
  def insertBatch(eoList: jList[EO]): jList[jLong]

  def select(id: jLong): EO
  def selectAndLock(id: jLong): EO
  def selectEOByEO(eo: EO): EO
  def selectListByEO(eo: EO): jList[EO]
  def selectListByEO(eo: EO, pageNO: Integer, pageSize: Integer): jList[EO]
  def selectListByIdList(idList: jList[jLong], lockEO: Boolean): jList[EO]
  def select(id: jLong, isLock: Boolean, noWait: Boolean): EO
  def countByEO(eo: EO): Integer

  def update(eo: EO): Integer
  def updateByField(eo: EO): Integer
  def updateBatch(eoList: jList[EO]): jList[Integer]
  def updateBatchByField(eoList: jList[EO]): jList[Integer]

  def delete(eo: EO): Integer
}