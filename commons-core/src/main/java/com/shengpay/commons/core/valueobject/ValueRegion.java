package com.shengpay.commons.core.valueobject;

import com.shengpay.commons.core.base.BaseObject;
import com.shengpay.commons.core.exception.SystemException;

public class ValueRegion <T extends Comparable<T>> extends BaseObject{
	
	/**
	 * 生效时间
	 */
	private T begin;
	
	/**
	 * 失效时间
	 */
	private T end;
	
	/**
	 * 开始时间是否闭区间 
	 */
	private boolean closeBegin;

	/**
	 * 结束时间是否闭区间
	 */
	private boolean closeEnd;
	
	public ValueRegion(T begin, T end, boolean closeBegin, boolean closeEnd) {
		if (begin != null && end != null && begin.compareTo(end) > 0) {
			throw new SystemException("起始值【"+begin+"】不能大于结束值【"+end+"】！");
		}
		
		
		this.begin = begin;
		this.closeBegin = closeBegin;
		this.closeEnd = closeEnd;
		this.end = end;
	}

	public ValueRegion(T begin, T end) {
		this(begin, end, true, false);
	}

	public ValueRegion() {
		this(null, null, true, false);
	}

	/* (non-Javadoc)
	 * @see com.shengpay.bgw.commons.valueobject.ValueRegion#getBegin()
	 */
	public T getBegin() {
		return begin;
	}

	/* (non-Javadoc)
	 * @see com.shengpay.bgw.commons.valueobject.ValueRegion#getEnd()
	 */
	public T getEnd() {
		return end;
	}
	
	/* (non-Javadoc)
	 * @see com.shengpay.bgw.commons.valueobject.ValueRegion#isOverlap(com.shengpay.bgw.commons.valueobject.ValueRegionImpl)
	 */
	public boolean isOverlap(ValueRegion<T> otherValidDate) {
		return this.isOverlapByOneWay(otherValidDate) || ((ValueRegion<T>)otherValidDate).isOverlapByOneWay(this);
	}
	
	private boolean isOverlapByOneWay(ValueRegion<T> otherValidDate1) {
		if(otherValidDate1==null) {
			return true;
		}
		
		//对方有效期包含了全部时段,所以一定重合
		ValueRegion<T> otherValidDate=(ValueRegion<T>) otherValidDate1;
		if(otherValidDate.begin==null && otherValidDate.end==null) {
			return true;
		}
		
		//对方开始时间为空时,只有其结束时间在当前有效期开始时间之前时,才不重合
		if(otherValidDate.begin==null) {
			return otherEndInMyRegion(otherValidDate); 
		}
		
		//对方结束时间为空时,只有其开始时间在当前有效期开始时间之后时,才不重合
		if(otherValidDate.end==null) {
			return otherBeginInMyRegion(otherValidDate);
		}

		//其开始时间和结束时间不在本有效期内,且本有效期也不在对方有效期内时,才不重叠
		return otherBeginInMyRegion(otherValidDate) || otherEndInMyRegion(otherValidDate);
	}

	private boolean otherEndInMyRegion(ValueRegion<T> otherValidDate) {
		if(equals(this.begin,otherValidDate.end)) {
			return this.closeBegin && otherValidDate.closeEnd;
		}else {
			return inRegion(otherValidDate.end);
		}
	}

	private boolean otherBeginInMyRegion(ValueRegion<T> otherValidDate) {
		if(equals(this.end,otherValidDate.begin)) {
			return this.closeEnd && otherValidDate.closeBegin;
		}else {
			return inRegion(otherValidDate.begin);
		}
	}
	
	private boolean equals(T a,T b) {
		if(a==b) {
			return true;
		}
		
		if(a==null || b==null) {
			return false;
		}
		
		return a.equals(b);
	}

	/* (non-Javadoc)
	 * @see com.shengpay.bgw.commons.valueobject.ValueRegion#inRegion(T)
	 */
	public boolean inRegion(T aDate) {
		if(aDate==null) {
			throw new RuntimeException("用于有效期判断的日期不能为空！");
		}
		
		return afterBeginDate(aDate) && beforeEndDate(aDate);
	}

	/**
	 * 判断指定日期在结束时间之前
	 * @param aDate
	 * @return
	 */
	private boolean beforeEndDate(T aDate) {
		//开始日期为空，标示不限定开始时间，所以直接返回true
		if(end==null) {
			return true;
		}
		
		if(closeEnd && aDate.compareTo(end)<=0) {
			return true;
		}
		
		if(!closeEnd && aDate.compareTo(end)<0) {
			return true;
		}
		
		return false;
	}
	/**
	 * 判断指定日期在结束时间之前
	 * @param aDate
	 * @return
	 */
	public boolean afterEndDate(T aDate) {
		if(end==null) {
			return false;
		}
		
		if(closeEnd && aDate.compareTo(end)>0) {
			return true;
		}
		
		if(!closeEnd && aDate.compareTo(end)>=0) {
			return true;
		}
		
		return false;
	}

	/**
	 * 判断指定日期在开始时间之后
	 * @param aDate
	 * @return
	 */
	private boolean afterBeginDate(T aDate) {
		//开始日期为空，标示不限定开始时间，所以直接返回true
		if(begin==null) {
			return true;
		}
		
		if(closeBegin && aDate.compareTo(begin)>=0) {
			return true;
		}
		
		if(!closeBegin && aDate.compareTo(begin)>0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判断指定日期在开始时间之后
	 * @param aDate
	 * @return
	 */
	public boolean beforeBeginDate(T aDate) {
		if(begin==null) {
			return false;
		}
		
		if(closeBegin && aDate.compareTo(begin)<0) {
			return true;
		}
		
		if(!closeBegin && aDate.compareTo(begin)<=0) {
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("unused")
	private void setBegin(T begin) {
		this.begin = begin;
	}

	@SuppressWarnings("unused")
	private void setEnd(T end) {
		this.end = end;
	}
}