package org.fish.chat.common.utils;


import cn.techwolf.common.log.LoggerManager;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.util.List;

/**
 * jackson 使用工具
 * @author xiangan.meng
 * @version 2013-10-10 上午03:20:31
 */


public class JsonReader {
   // private static final LoggerManager LoggerManager = LoggerManagerFactory.getLoggerManager(JsonReader.class);
	public static <T> T fetchObject(String object, Class<T> objectType) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		T t = null;
		try {
			t = mapper.readValue(object, objectType);
		} catch (JsonParseException e) {
		    LoggerManager.error("fetchObject err" + object, e);
		} catch (JsonMappingException e) {
		    LoggerManager.error("fetchObject err" + object, e);
		} catch (Exception e) {
		    LoggerManager.error("fetchObject err" + object, e);
		}
		return t;
	}

	
	public static String fetchString(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY); 
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
		String result = "";
		try {
			result = mapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
		    LoggerManager.error("fetchStringByjackson err" + object, e);
		} catch (JsonMappingException e) {
		    LoggerManager.error("fetchStringByjackson err" + object, e);
		} catch (Exception e) {
		    LoggerManager.error("fetchStringByjackson err" + object, e);
		}
		return result;
	}

	public static <T> T fetchListObject(String object,Class<?> beanType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T t = null;
        try {
            t =mapper.readValue(object,mapper.getTypeFactory().constructParametricType(List.class,beanType));
        } catch (JsonParseException e) {
            LoggerManager.error("fetchListObject err" + object, e);
        } catch (JsonMappingException e) {
            LoggerManager.error("fetchListObject err" + object, e);
        } catch (Exception e) {
            LoggerManager.error("fetchListObject err" + object, e);
        }
        return t;
    }
	
}
