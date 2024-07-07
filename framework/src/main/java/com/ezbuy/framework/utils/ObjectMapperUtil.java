package com.ezbuy.framework.utils;

import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.exception.UnRetryableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ObjectMapperUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateFormat dateFormatYMdHms = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static String convertObjectToJson(Object object) {
        if (DataUtil.isNullOrEmpty(object)) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Convert to Object error ", ex);
            return null;
        }
    }

    public <T> T convertStringToObject(String objectString, Class<T> valueType) {
        if (DataUtil.isNullOrEmpty(objectString)) {
            return null;
        }
        try {
            return objectMapper.readValue(objectString, valueType);
        } catch (Exception ex) {
            log.error("Convert to Object error ", ex);
            return null;
        }
    }

    public <T> List<T> convertStringListToObjectList(List<?> stringList, Class<T> targetClass) {

        if (DataUtil.isNullOrEmpty(stringList)) {
            return null;
        }
        List<T> objectList = new ArrayList<>();
        for (Object item : stringList) {
            T object = objectMapper.convertValue(item, targetClass);
            objectList.add(object);
        }
        return objectList;
    }

    public <T> T convertToObject(byte[] byteArray, Class<T> valueType) {
        try {
            return objectMapper.readValue(byteArray, valueType);
        } catch (Exception ex) {
            throw new UnRetryableException(CommonErrorCode.UN_DESERIALIZE, ex.getMessage());
        }
    }

    public <T> List<T> convertToObject(List<Object> input, Class<T> valueType) {
        try {
            List<T> results = new ArrayList<>();
            for (Object object : input
            ) {
                results.add(objectMapper.convertValue(object, valueType));
            }
            return results;
        } catch (Exception ex) {
            throw new UnRetryableException(CommonErrorCode.UN_DESERIALIZE, ex.getMessage());
        }
    }

    public <T> T convertToObject(Object input, Class<T> valueType) {
        try {
            return objectMapper.convertValue(input, valueType);
        } catch (Exception ex) {
            throw new UnRetryableException(CommonErrorCode.UN_DESERIALIZE, ex.getMessage());
        }
    }

    public static <T> T convertObject(Object input, Class<T> valueType){
        return objectMapper.convertValue(input, valueType);
    }

    public static String convertObjectToJsonForLocalDateTime(Object object) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(dateFormatYMdHms);
        if (DataUtil.isNullOrEmpty(object)) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Convert to Object error ", ex);
            return null;
        }
    }
}
