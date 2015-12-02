package org.amoyiki.oa.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * <p>Title:JacksonUtil</p>
 * <p>Description:Json转换工具 </p>
 * @author amoyiki
 * @date 2015年12月1日 下午2:54:45
 *
 */
public final class JacksonUtil {
	private static ObjectMapper objectMapper;
	
	/**
	 * 使用泛型方法吧json字符串转换成相应的JavaBean对象
	 * 1.转换为普通的JavaBean:readValue(json,User.class)
	 * 2.转换为List。如List<User>,第二个参数传递为User[].class
	 * 然后使用Arrays.asList();把得到的数组转换为特定的List
	 * @param jsonStr
	 * @param valueType
	 * @return
	 */
	public static <T> T readValue(String jsonStr, Class<T> valueType){
		if (objectMapper == null){
			objectMapper = new ObjectMapper();
		}
		try {
			return objectMapper.readValue(jsonStr, valueType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 把JavaBean转换成json字符串
	 * @param object
	 * @return
	 */
	public static String toJson(Object object){
		if(objectMapper == null){
			objectMapper = new ObjectMapper();
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
}
