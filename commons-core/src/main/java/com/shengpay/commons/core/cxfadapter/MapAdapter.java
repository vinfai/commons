package com.shengpay.commons.core.cxfadapter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Map适配器
 * @author TEANA E-mail: mmz06@163.com
 * @version 创建时间：2011-7-1 下午11:25:11
 */

public class MapAdapter extends XmlAdapter<MapConvertor, Map<Object, Object>> {

	@Override
	public MapConvertor marshal(Map<Object, Object> map) throws Exception {
		MapConvertor convertor = new MapConvertor();
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			MapConvertor.MapEntry e = new MapConvertor.MapEntry(entry);
			convertor.addEntry(e);
		}
		return convertor;
	}

	@Override
	public Map<Object, Object> unmarshal(MapConvertor map) throws Exception {
		Map<Object, Object> result = new HashMap<Object, Object>();
		for (MapConvertor.MapEntry e : map.getEntries()) {
			result.put(e.getKey(), e.getValue());
		}
		return result;
	}
}
