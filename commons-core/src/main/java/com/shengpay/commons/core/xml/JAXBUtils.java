package com.shengpay.commons.core.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * xml操作类jaxb
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation
 * @author      lizhi<lizhi@snda.com>
 * @version		$Id: GgManageRO.java,v 1.0 2010-10-8 下午04:15:17 lizhi Exp $
 * @create		2010-10-21 下午04:15:17
 */
public class JAXBUtils {
	
	/**
	 * javabean转xml，并生成文件
	 * 
	 * @param t        xml映射的javabean
	 * @param filePath 生成的文件路径
	 * @param encoding 字符集编码
	 * @return
	 * @throws JAXBException,IOException
	 */
	public static <T> void bean2Xml(T t,String filePath,String encoding)throws JAXBException,IOException{
		
		JAXBContext context = JAXBContext.newInstance(t.getClass());
        //对象转变为xml
        Marshaller m = context.createMarshaller();
        //m.setProperty(Marshaller.JAXB_ENCODING, "GB2312");
        
        //Writer writer = new OutputStreamWriter(new FileOutputStream("d://orderInfo.xml"),"GB2312");
        OutputFormat outFormat  = new OutputFormat();
        outFormat.setEncoding(encoding);
        OutputStream out = new FileOutputStream(new File(filePath));
        XMLSerializer writer = new XMLSerializer(out,outFormat);
        m.marshal(t,writer);
	}
	
	/**
	 * xml转javabean
	 * 
	 * @param bytes       xml字节
	 * @param targetClazz javabean的class
	 * @param encoding    字符集编码
	 * @return
	 * @throws JAXBException,IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2Bean(byte[] bytes,Class<T> targetClazz,String encoding)throws JAXBException,IOException{
		
		//Class<T> entityClass = (Class<T>)((ParameterizedType)targetClazz.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		JAXBContext context = JAXBContext.newInstance(targetClazz);
	    Unmarshaller um = context.createUnmarshaller();
	    InputStream in = new ByteArrayInputStream(bytes);
	    Reader reader = new InputStreamReader(in,encoding);
	    return (T)um.unmarshal(reader);
	}
}
