package com.shengpay.commons.core.constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.core.utils.StringUtils;

/**
 * 
 * @description
 * @usage
 */
@SuppressWarnings("unchecked")
public class ConstantsUtil {

    /**
     * 系统日志输出句柄
     */
    private static Logger logger = Logger.getLogger(ConstantsUtil.class);

    /**
     * 常量值定义的顺序列表
     */
    private static Map<String, List<Object>> constantsOrderMap = new LinkedHashMap();

    /**
     * 常量映射<常量KEY，常量对象>
     */
    private static Map<String, ConstantsTO> constantsMap = new LinkedHashMap();

    /**
     * 常量映射
     */
    private static Map<String, String> constantsType2ClassMap = new LinkedHashMap();

    /**
     * 常量映射
     */
    private static Map<String, ConstantsTO> filedName2ValueMap = new LinkedHashMap();

    /**
     * 常量映射
     */
    private static Map<String, Map<Object, String>> constantsTypeMap = new LinkedHashMap();

    static {
//        // 取得包含常量类信息
        Set<Class<?>> allClass = ClassUtils.getClassSetByPackageName("com.shengpay");
        for (Class aClass : allClass) {
            loadConstantsNameMap(aClass);
        }

        loadParentMap();

        // 常量临时数据
        constantsType2ClassMap.clear();
    }

	/**
	 * 加载常量父子关系信息(未作清理操作)
	 */
	public static void loadParentMap() {
		// 组织级联常量关系
        for (ConstantsTO ct : constantsMap.values()) {
            pubCtToParentMap(ct);
        }
	}

    /**
     * 加载常量注释信息
     * 
     * @param hasConstantsClass
     *            含有常量定义的类型名称
     */
    private static void loadConstantsNameMap(Class<?> aType) {

        Field[] fields = aType.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            // 通过域信息构造常量对象
            ConstantsTO newCt = makeConstantsTO(fields[i]);
            if (newCt == null) {
                continue;
            }

            addConstantsTO(newCt);
        }
    }

    /**
     * 添加常量到缓存中
     * 
     * @param newCt
     */
    public static void addConstantsTO(ConstantsTO newCt) {
        // 验证当前常量类型在其他类型中是否也存在
        validateConstantsType(newCt);

        // 将常量对象存放到常量映射中
        putCtToConstantsMap(newCt);

        // 将常量信息存放到顺序列表中
        putCtToOrderMap(newCt);

        // 将常量放到类型映射中
        putCtToTypeMap(newCt);

        // 更新域名到域值的映射
        putCtToFieldMap(newCt);
    }

    /**
     * @param newCt
     */
    private static void putCtToConstantsMap(ConstantsTO newCt) {
        String constantsKey = getConstantsKey(newCt);
        ConstantsTO oldCt = constantsMap.get(constantsKey);
        if (oldCt != null) {
            logger.info("存在两个具有相同键值的常量【" + newCt + "】和【" + oldCt + "】");
        }
        constantsMap.put(constantsKey, newCt);
        System.out.println(constantsKey+":"+constantsMap.get(constantsKey));
    }

    /**
     * 组织常量级联关系
     * 
     * @param ct
     */
    private static void pubCtToParentMap(ConstantsTO ct) {
        // 如果当前常量没有父常量信息，则直接返回
        String parentFieldName = ct.getParentFieldName();
        if (StringUtils.isBlank(parentFieldName)) {
            return;
        }

        // 取得父常量信息
        ConstantsTO parentConstantsTO = filedName2ValueMap.get(parentFieldName);
        if (parentConstantsTO == null) {
            throw new SystemException("未找到域名为【" + parentFieldName + "】的常量信息！");
        }
        ct.setParentConstantsTO(parentConstantsTO);
        parentConstantsTO.addClient(ct);
        
        //将账户类型,资金类型放到常量映射中
        if(!StringUtils.isBlank(ct.getFromAbTypeFieldName())){
        	ct.setFromAbType(getConstantsValueByFieldName(ct.getFromAbTypeFieldName()));
        }
        if(!StringUtils.isBlank(ct.getFromFundTypeFieldName())){
        	ct.setFromFundType(getConstantsValueByFieldName(ct.getFromFundTypeFieldName()));
        }
        if(!StringUtils.isBlank(ct.getToAbTypeFieldName())){
        	ct.setToAbType(getConstantsValueByFieldName(ct.getToAbTypeFieldName()));
        }
        if(!StringUtils.isBlank(ct.getToFundTypeFieldName())){
        	ct.setToFundType(getConstantsValueByFieldName(ct.getToFundTypeFieldName()));
        }
    }

    /**
     * 取得父常量键
     * 
     * @param parentConstantsTO
     * @return
     */
    private static String getConstantsKey(ConstantsTO parentConstantsTO) {
        return getConstantsKey(parentConstantsTO.getConstantsType(), parentConstantsTO.getFieldValue());
    }

    /**
     * 取得父常量键
     * 
     * @param parentConstantsType
     * @param parentFieldValue
     * @return
     */
    private static String getConstantsKey(String parentConstantsType, Object parentFieldValue) {
        return parentConstantsType + "-" + parentFieldValue;
    }

    /**
     * 更新域名到域值的映射
     * 
     * @param ct
     */
    private static void putCtToFieldMap(ConstantsTO ct) {
        Object fieldValue = filedName2ValueMap.get(ct.getFieldName());
        if (fieldValue != null) {
            throw new SystemException("域名为【" + ct.getFieldName() + "】的常量存在重复的定义！");
        }
        filedName2ValueMap.put(ct.getFieldName(), ct);
    }

    /**
     * 将常量信息存放到顺序列表中
     * 
     * @param ct
     */
    private static void putCtToOrderMap(ConstantsTO ct) {
        // 将常量值添加到顺序列表中,起码可以为资金明细的转账金额正负号做出判断
        List<Object> constantsValueList = constantsOrderMap.get(ct.getConstantsType());
        if (constantsValueList == null) {
            constantsValueList = new ArrayList<Object>();
            constantsOrderMap.put(ct.getConstantsType(), constantsValueList);
        }

        // 判断顺序列表中是否已经有相同的值
        if (constantsValueList.indexOf(ct.getFieldValue()) != -1) {
            logger.info("类型为【" + ct.getConstantsType() + "】，值为【" + ct.getFieldValue() + "】的常量已经在顺序列表中存在，现在又添加该值到顺序列表中，应该有问题 ！");
        } else {
            constantsValueList.add(ct.getFieldValue());
        }
    }

    /**
     * 将常量信息存放到类型映射中
     * 
     * @param ct
     */
    private static void putCtToTypeMap(ConstantsTO ct) {
        Map<Object, String> constantsNameMap = constantsTypeMap.get(ct.getConstantsType());
        if (constantsNameMap == null) {
            constantsNameMap = new LinkedHashMap();
            constantsTypeMap.put(ct.getConstantsType(), constantsNameMap);
        }

        String oldConstantsName = constantsNameMap.get(ct.getFieldValue());
        if (StringUtils.notBlank(oldConstantsName)) {
            logger.info("类型为【" + ct.getConstantsType() + "】，值为【" + ct.getFieldValue() + "】的常量已经有名字【" + oldConstantsName + "】，又有新的常量名称【" + ct.getConstantsName() + "】，应该是定义有问题！");
        } else {
            // 将常量值对应的常量名存放到映射中
            constantsNameMap.put(ct.getFieldValue(), ct.getConstantsName());
        }
    }

    /**
     * 根据类属性，取得域名称
     * 
     * @param field
     * @return
     */
    private static ConstantsTO makeConstantsTO(Field field) {
        // 查看当前域是否存在常量定义标签
        ConstantTag constantsTag = field.getAnnotation(ConstantTag.class);
        if (constantsTag == null) {
            return null;
        }

        // 判断域是否为静态类型
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new SystemException("类型[" + field.getDeclaringClass() + "]的域[" + field + "]不是静态类型,无法取得其值!");
        }

        // 取得域的值
        Object constantsValue;
        try {
            constantsValue = field.get(null);
        } catch (Exception e) {
            throw new SystemException("取得静态域[" + field + "]的值时发生异常:", e);
        }

        // 构造常量对象
        ConstantsTO ct = new ConstantsTO();
        ct.setClassName(field.getDeclaringClass().getName());
        ct.setFieldName(field.getName());
        ct.setFieldValue(String.valueOf(constantsValue));
        ct.setConstantsType(constantsTag.type());
        ct.setConstantsName(constantsTag.name());
        ct.setConstantsReverseName(constantsTag.reverseName());
        ct.setParentFieldName(constantsTag.parentConstants());
        ct.setFromQueryFlag(constantsTag.fromQueryFlag());
        ct.setFromAbTypeFieldName(constantsTag.fromAbTypeFieldName());
        ct.setFromFundTypeFieldName(constantsTag.fromFundTypeFieldName());
        ct.setToAbTypeFieldName(constantsTag.toAbTypeFieldName());
        ct.setToFundTypeFieldName(constantsTag.toFundTypeFieldName());
        ct.setToQueryFlag(constantsTag.toQueryFlag());
        return ct;
    }

    /**
     * 取得常量值在类中定义的索引号
     * 
     * @param constantsType
     * @param constantsValue
     * @return
     */
    public static int getConstantsValueOrder(String constantsType, Object constantsValue) {
        List<Object> list = constantsOrderMap.get(constantsType);
        if (list == null) {
            throw new SystemException("无类型名为【" + constantsType + "】的常量登记！");
        }

        int indexOf = list.indexOf(constantsValue);
        return indexOf != -1 ? indexOf : Integer.MAX_VALUE;
    }

    /**
     * 验证当前常量类型在其他类型中是否存在
     * 
     * @param constantsType
     * @return
     */
    private static void validateConstantsType(ConstantsTO ct) {
        String constantsType = ct.getConstantsType();
        String newHasConstantsTypeOfClassName = ct.getClassName();
        String hasConstantsTypeOfClassName = constantsType2ClassMap.get(constantsType);
        if (hasConstantsTypeOfClassName == null) {
            constantsType2ClassMap.put(constantsType, newHasConstantsTypeOfClassName);
            return;
        }

        if (!hasConstantsTypeOfClassName.equals(newHasConstantsTypeOfClassName)) {
            throw new SystemException("类型[" + hasConstantsTypeOfClassName + "]和[" + newHasConstantsTypeOfClassName + "]均包含有常量类型[" + constantsType + "],该做法不被允许!");
        }
    }

    /**
     * 取得所有常量的集合
     * 
     * @return
     */
    public static Collection<ConstantsTO> getAllConstantsTOList() {
        return constantsMap.values();
    }

    /**
     * 取得子常量列表
     * 
     * @param parentConstantsType
     * @param parentConstantsValue
     * @return
     */
    public static List<ConstantsTO> getClientConstantsList(String parentConstantsType, Object parentConstantsValue) {
        return getConstantsTO(parentConstantsType, parentConstantsValue).getClientList();
    }

    /**
     * 取得父常量信息
     * 
     * @param clientConstantsType
     * @param clientConstantsValue
     * @return
     */
    public static ConstantsTO getParentConstants(String clientConstantsType, Object clientConstantsValue) {
        ConstantsTO constantsTO = constantsMap.get(getConstantsKey(clientConstantsType, clientConstantsValue));
        if (constantsTO == null) {
            throw new SystemException("未找到类型为【" + clientConstantsType + "】，值为【" + clientConstantsValue + "】的常量的信息!");
        }
        return constantsTO.getParentConstantsTO();
    }

    /**
     * 取得常量名称
     * 
     * @param constantsValue
     *            常量值(可以是多个使用逗号分隔的常量值)
     * @param constantsType
     *            常量类型
     * @return 常量名称
     */
    public static String getConstantsName(String constantsValue, String constantsType) {
        if (StringUtils.isBlank(constantsValue)) {
            return "";
        }

        return getConstantsTO(constantsType, constantsValue).getConstantsName();
    }

    /**
     * 取得常量的反向名称
     * 
     * @param constantsValue
     *            常量值(可以是多个使用逗号分隔的常量值)
     * @param constantsType
     *            常量类型
     * @return 常量名称
     */
    public static String getConstantsReverseName(String constantsValue, String constantsType) {
        if (StringUtils.isBlank(constantsValue)) {
            return "";
        }

        return getConstantsTO(constantsType, constantsValue).getConstantsReverseName();
    }

    /**
     * 取得常量信息
     * 
     * @param constantsType
     * @param constantsValue
     * @return <不会为空>
     */
    public static ConstantsTO getConstantsTO(String constantsType, Object constantsValue) {
        String constantsKey = getConstantsKey(constantsType, constantsValue);
		ConstantsTO constantsTO = constantsMap.get(constantsKey);
        if (constantsTO == null) {
            throw new SystemException("未找到类型为【" + constantsType + "】，值为【" + constantsValue + "】的常量信息未找到！");
        }
        return constantsTO.clone();
    }

    /**
     * 取得指定常量类型的<值-名称>映射
     * 
     * @param constantsType
     * @return <可能为空>
     */
    public static Map<Object, String> getConstantsMap(String constantsType) {
        // 判断目标映射是否存在
        Map<Object, String> aimMap = constantsTypeMap.get(constantsType);
        if (aimMap == null) {
            return null;
        }

        // 构造一个新的映射进行返回
        Map<Object, String> cloneMap = new LinkedHashMap();
        cloneMap.putAll(aimMap);
        return cloneMap;
    }

    /**
     * 根据域名取得常量信息
     * 
     * @param fieldName
     * @return (不为空)
     */
    public static ConstantsTO getConstantsTOByFieldName(String fieldName) {
        ConstantsTO constantsTO = filedName2ValueMap.get(fieldName);
        if (constantsTO == null) {
            throw new SystemException("未找到域名为【" + fieldName + "】的常量信息！");
        }
        return constantsTO;
    }

    /**
     * 根据域名取得常量信息
     * 
     * @param fieldName
     * @return
     */
    public static String getConstantsValueByFieldName(String fieldName) {
        if (StringUtils.isBlank(fieldName)) {
            return null;
        }
        return getConstantsTOByFieldName(fieldName).getFieldValue();
    }

    /**
     * 取得子常量列表
     * 
     * @param parentConstantsType
     * @param parentConstantsValue
     * @return
     */
    public static Map<String, String> getClientConstantsMap(String parentConstantsType, Object parentConstantsValue) {
        List<ConstantsTO> clientList = getConstantsTO(parentConstantsType, parentConstantsValue).getClientList();
        if (clientList == null) {
            return null;
        }
        Map<String, String> clientMap = new LinkedHashMap();
        for (ConstantsTO ct : clientList) {
            clientMap.put(ct.getFieldValue(), ct.getConstantsName());
        }
        return clientMap;
    }

    public static void main(String[] args) {
		System.out.println(getConstantsMap("SL_STATUS"));
	}

	public static Set<String> getAllConstantsType() {
		return constantsTypeMap.keySet();
	}
}
