package com.shengpay.commons.core.datacache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * escription 主键注释
 * @usage
 * @copyright Copyright 2010 SDOCorporation. All rights reserved.
 * @company SSDOorporation.
 * @author zhangxiaochuan <zhangxc@SDSDOm>
 * @version prikey.java,2010-2-5 zhangxiaochuan
 * @create 2010,2010-2-5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface PrimaryKey {

}
