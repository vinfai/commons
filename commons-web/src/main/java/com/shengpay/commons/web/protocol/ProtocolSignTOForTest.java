package com.shengpay.commons.web.protocol;

import java.util.Date;
import java.util.List;

import com.shengpay.commons.core.base.BaseObject;

public class ProtocolSignTOForTest extends BaseObject {

	private String merchantId;

	private String merchantName;

	private String merchantSeqNO;

	@ProtocolFieldAnn(paramName="goods_num")
	private Integer goodsNUM;
	
	@ProtocolFieldAnn(paramName="item")
	private List<ProtocolSignTOClientForTest> goods;
	
	@ProtocolFieldAnn(dateFormat="yyyyMMddHHmmss",paramName="mer_date")
	private Date merchantDate;

	public String getMerchantSeqNO() {
		return merchantSeqNO;
	}

	public void setMerchantSeqNO(String itemName) {
		this.merchantSeqNO = itemName;
	}

	public Integer getGoodsNUM() {
		return goodsNUM;
	}

	public void setGoodsNUM(Integer itemCount) {
		this.goodsNUM = itemCount;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public List<ProtocolSignTOClientForTest> getGoods() {
		return goods;
	}

	public void setGoods(List<ProtocolSignTOClientForTest> clientList) {
		this.goods = clientList;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Date getMerchantDate() {
		return merchantDate;
	}

	public void setMerchantDate(Date merchantDate) {
		this.merchantDate = merchantDate;
	}
}
