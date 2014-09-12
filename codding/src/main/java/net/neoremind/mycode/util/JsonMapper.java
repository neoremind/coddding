package net.neoremind.mycode.util;

import java.io.IOException;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;


public class JsonMapper {
    private static final Logger LOG = LoggerFactory.getLogger(JsonMapper.class);

    private ObjectMapper mapper;

    public JsonMapper(Inclusion inclusion) {
        mapper = new ObjectMapper(); //设置输出时包含属性的风格               
        mapper.setSerializationInclusion(inclusion);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性     
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //禁止使用int代表Enum的order()来反序列化Enum,非常危险 
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    }

    /**
     * contains all fields.
     */
    public static JsonMapper buildNormalMapper() {
        return new JsonMapper(Inclusion.ALWAYS);
    }

    /**
     * ignore null fields.
     */
    public static JsonMapper buildNonNullMapper() {
        return new JsonMapper(Inclusion.NON_NULL);
    }

    /**
     * contains default changed fields.
     */
    public static JsonMapper buildNonDefaultMapper() {
        return new JsonMapper(Inclusion.NON_DEFAULT);
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.trim().equals("")) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            LOG.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    public <T> T fromJsonBytes(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0)
            return null;
        else {
            try {
                return mapper.readValue(bytes, clazz);
            } catch (Exception e) {
                LOG.warn("parse json bytes error:", e);
                return null;
            }
        }
    }
    
    public JsonNode fromJson(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return null;
        else {
            try {
                return mapper.readTree(bytes);
            } catch (Exception e) {
                LOG.warn("parse json bytes error:", e);
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(JsonNode jsonString, JavaType javaType) {
        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            LOG.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(JsonNode jsonString, Type t) {
        return (T) fromJson(jsonString, mapper.constructType(t));
    }

    public byte[] toJsonBytes(Object object) {
        //这里有作用,如果object==null.
        //mapper会用json处理成"null"字符串,导致歧义
        if (object == null) {
            return null;
        }
        try {
            return mapper.writeValueAsBytes(object);
        } catch (Exception e) {
            LOG.warn("write to json bytes error:" + object, e);
            return null;
        }
    }

    public String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            LOG.warn("write to json string error:" + object, e);
            return null;
        }
    }

    public void setEnumUseToString(boolean value) {
        mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, value);
        mapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, value);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
    
}