package com.ezbuy.framework.utils;

import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.factory.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import reactor.core.publisher.Mono;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Slf4j
public class DataUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataUtil.class);

    /**
     * Ham chuyen String sang boolean
     *
     * @param ob
     * @return
     */
    public static boolean isNullOrEmpty(Object ob) {
        return ob == null || ob.toString().trim().isEmpty();
    }

    /**
     * Ham chuyen String sang boolean
     *
     * @param cs
     * @return
     */
    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ham chuyen Collection sang boolean
     *
     * @param collection
     * @return
     */
    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Ham chuyen Object sang boolean
     *
     * @param collection
     * @return
     */
    public static boolean isNullOrEmpty(final Object[] collection) {
        return collection == null || collection.length == 0;
    }

    /**
     * Ham chuyen Map sang boolean
     *
     * @param map
     * @return
     */
    public static boolean isNullOrEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Ham chuyen Object sang String
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String safeToString(Object obj, String defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        return obj.toString();
    }

    /**
     * Ham chuyen Object sang String
     *
     * @param obj
     * @return
     */
    public static String safeToString(Object obj) {
        return safeToString(obj, "");
    }

    /**
     * Ham chuyen Object sang int
     *
     * @param obj1
     * @param defaultValue
     * @return
     */
    public static int safeToInt(Object obj1, int defaultValue) {
        int result = defaultValue;
        if (obj1 != null) {
            try {
                result = Integer.parseInt(obj1.toString());
            } catch (Exception ignored) {
                log.error("safeToInt error: ", ignored);
            }
        }
        return result;
    }

    /**
     * Ham chuyen Object sang Boolean
     *
     * @param obj1
     * @return
     */
    public static Boolean safeToBoolean(Object obj1) {
        Boolean result = null;
        try {
            result = obj1 == null ? null : (Boolean) obj1;
        } catch (Exception ex) {
            log.error("safeToBoolean error ", ex);
        }
        return result;
    }

    /**
     * Ham chuyen Object sang int
     *
     * @param obj1
     * @return
     */
    public static int safeToInt(Object obj1) {
        return safeToInt(obj1, 0);
    }

    /**
     * Ham chuyen Object sang Long
     *
     * @param obj1
     * @return
     */
    public static Long safeToLong(Object obj1) {
        return safeToLong(obj1, 0L);
    }

    /**
     * Ham chuyen Object sang Long
     *
     * @param obj1
     * @param defaultValue
     * @return
     */
    public static Long safeToLong(Object obj1, Long defaultValue) {
        Long result = defaultValue;
        if (obj1 != null) {
            switch (obj1) {
                case BigDecimal bigDecimal -> {
                    return bigDecimal.longValue();
                }
                case BigInteger bigInteger -> {
                    return bigInteger.longValue();
                }
                case Double v -> {
                    return v.longValue();
                }
                default -> {
                }
            }

            try {
                result = Long.parseLong(obj1.toString());
            } catch (Exception ignored) {
                log.error("safeToInt error: ", ignored);
            }
        }
        return result;
    }

    /**
     * Ham chuyen Object sang Double
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static Double safeToDouble(Object obj, Double defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception ex) {
            log.error("safeToDouble error: ", ex);
            return defaultValue;
        }
    }

    /**
     * Ham chuyen Object sang Double
     *
     * @param obj
     * @return
     */
    public static Double safeToDouble(Object obj) {
        return safeToDouble(obj, 0.0D);
    }

    /**
     * Ham chuyen string sang UUID
     *
     * @param input
     * @return
     */
    public static UUID safeToUUID(String input) {
        try {
            return UUID.fromString(input);
        } catch (Exception ex) {
            log.error("safeToUUID: ", ex);
            return null;
        }
    }

    /**
     * Ham chuyen string sang UUID
     *
     * @param input
     * @return
     */
    public static boolean isUUID(String input) {
        try {
            UUID uuid = UUID.fromString(input);
            return uuid.toString().equals(input);
        } catch (Exception ex) {
            log.error("isUUID: ", ex);
            return false;
        }
    }

    /**
     * Safe trim
     *
     * @param Object
     * @return
     */
    public static String safeTrim(Object input) {
        return safeToString(input).trim();
    }

    /**
     * Safe trim
     *
     * @param String
     * @return
     */
    public static String safeTrim(String input) {
        return safeToString(input).trim();
    }

    /**
     * Safe trim
     *
     * @param Object obj1
     * @param Object obj2
     * @return
     */
    public static boolean safeEqual(Object obj1, Object obj2) {
        return ((obj1 != null) && (obj2 != null) && obj2.toString().equals(obj1.toString()));
    }

    public static boolean safeEqual(String obj1, String obj2) {
        if (obj1 == obj2) return true;
        return ((obj1 != null) && (obj2 != null) && obj1.equals(obj2));
    }

    public static ObjectMapper convertObject() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();
    }

    /**
     * Ham chuyen string sang boolean
     *
     * @param obj
     * @return
     */
    public static boolean isTrue(Object obj) {
        return safeEqual(safeToString(obj), "true");
    }

    /**
     * Ham chuyen string sang object
     *
     * @param content
     * @param clz
     * @param defaultValue
     * @return
     */
    public static <T> T parseStringToObject(String content, Class clz) {
        if (isNullOrEmpty(content)) {
            return null;
        }
        try {
            return (T) ObjectMapperFactory.getInstance().readValue(safeToString(content), clz);
        } catch (JsonProcessingException e) {
            log.error("Parse json error", e);
        }
        try {
            return (T) clz.newInstance();
        } catch (Exception e) {
            log.error("cast object error: ", e);
            return (T) new Object();
        }
    }

    /**
     * Ham chuyen string sang object
     *
     * @param content
     * @param clz
     * @param defaultValue
     * @return
     */
    public static <T> T parseStringToObject(String content, TypeReference<T> clz, T defaultValue) {
        if (isNullOrEmpty(content)) {
            return null;
        }
        try {
            return ObjectMapperFactory.getInstance().readValue(safeToString(content), clz);
        } catch (JsonProcessingException e) {
            log.error("Parse json error: ", e);
            return defaultValue;
        }
    }

    /**
     * Ham chuyen object sang string
     *
     * @param obj
     * @return
     */
    public static String parseObjectToString(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return ObjectMapperFactory.getInstance().writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            log.error("parseObjectToString: ", ex);
            return "";
        }
    }

    /**
     * Ham chuyen convert string to local date time
     *
     * @param obj
     * @return
     */
    public static LocalDateTime convertStringToLocalDateTime(String input, String format) {
        if (isNullOrEmpty(input)) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(input, formatter);
        } catch (Exception ex) {
            log.error("convertStringToLocalDateTime error: ", ex);
            return null;
        }
    }

    /**
     * Ham chuyen format date
     *
     * @param obj
     * @return
     */
    public static String formatDate(TemporalAccessor date, String format, String fallbackValue) {
        if (isNullOrEmpty(date)) {
            return fallbackValue;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(date);
    }

    /**
     * Ham chuyen sum list string
     *
     * @param obj
     * @return
     */
    public static String sumListString(String... list) {
        StringBuilder result = new StringBuilder();
        for (String str : list) {
            result.append(str);
        }
        return result.toString();
    }

    /**
     * Ham chuyen get like string
     *
     * @param obj
     * @return
     */
    public static String getLikeStr(String str) {
        if (str == null) {
            str = "";
        }
        return "%" + str + "%";
    }

    /**
     * Ham validate json format
     *
     * @param obj
     * @return
     */
    public static boolean isValidFormatJson(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            LOGGER.info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Ham chuyen local date time sang string
     *
     * @param date
     * @param format
     * @return
     */
    public static String convertLocalDateToString(LocalDateTime date, String format) {
        try {
            if (ObjectUtils.isEmpty(date))
                return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return date.format(formatter);
        } catch (Exception e) {
            log.error("====> parse local date time to string ==> " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Ham chuyen string sang date
     *
     * @param dateString
     * @return
     */
    public static LocalDateTime convertStringToDateTime(String dateString) {
        try {
            // Define the date formatter for the input format
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Parse the date string to a LocalDate object
            LocalDate date = LocalDate.parse(dateString, dateFormatter);

            // Create a LocalTime object set to midnight (00:00:00)
            LocalTime time = LocalTime.MIDNIGHT;

            // Combine the date and time to create a LocalDateTime object
            return LocalDateTime.of(date, time);
        } catch (Exception ex) {
            log.error("Parse error: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Ham chuyen object sang json
     *
     * @param obj
     * @return
     */
    public static String appendLikeQuery(String field) {
        return "%" + field + "%";
    }

    /**
     * Ham chuyen object sang json
     *
     * @param obj
     * @return
     */
    public static <T> Mono<Optional<T>> optional(Mono<T> in) {
        return in.map(Optional::of).switchIfEmpty(Mono.just(Optional.empty()));
    }

    /**
     * Ham chuyen object to xml
     *
     * @param arg0
     * @param name
     * @return
     */
    public static String ConvertObjectToXMLString(Object arg0, String name) {
        String xml = "";
        try {
            StringWriter sw = new StringWriter();
            JAXB.marshal(arg0, sw);
            Document doc = convertStringToDocument(sw.toString());
            xml = convertDocumentToString(doc);
            if (!DataUtil.isNullOrEmpty(xml)) {
                xml = xml.replace("<" + name + ">", "");
                xml = xml.replace("</" + name + ">", "");
            }
        } catch (Exception ex) {
            return xml;
        }
        return xml;
    }

    /**
     * Ham chuyen xml to object
     *
     * @param xmlStr
     * @param clazz
     * @return
     */
    private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            return null;
        }
    }

    /**
     * Ham chuyen xml to object
     *
     * @param xmlStr
     * @param clazz
     * @return
     */
    private static Document convertStringToDocument(String xmlStr) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            InputSource inputSource = new InputSource(new StringReader(xmlStr));
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputSource);
            return doc;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Safe to float
     *
     * @param obj1
     * @return
     */
    public static Float safeToFloat(Object obj1) {
        return safeToFloat(obj1, 0F);
    }

    /**
     * Safe to float
     *
     * @param obj1
     * @param defaultValue
     * @return
     */
    public static Float safeToFloat(Object obj1, Float defaultValue) {
        Float result = defaultValue;
        if (obj1 != null) {
            if (obj1 instanceof BigDecimal) {
                return ((BigDecimal) obj1).floatValue();
            }
            if (obj1 instanceof BigInteger) {
                return ((BigInteger) obj1).floatValue();
            }

            if (obj1 instanceof Double) {
                return ((Double) obj1).floatValue();
            }

            try {
                result = Float.parseFloat(obj1.toString());
            } catch (Exception ignored) {
                log.error("safeToInt error: ", ignored);
            }
        }
        return result;
    }

    /**
     * Validate page size
     *
     * @param pageSize
     * @param defaultPageSize
     * @return
     */
    public static int validatePageSize(Integer pageSize, int defaultPageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        } else if (pageSize <= 0) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.pageSize.invalid");
        }
        return pageSize;
    }

    /**
     * Validate page index
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static int validatePageIndex(Integer pageIndex, Integer pageSize) {
        int offset = 1;
        if (pageIndex == null) {
            offset = 1;
        } else if (pageIndex < 0) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.pageIndex.invalid");
        } else {
            offset = (pageIndex - 1) * pageSize;
        }
        return offset;
    }

    /**
     * Convert date string to LocalDateTime
     *
     * @param input
     * @param format
     * @return
     * @throws Exception
     */
    public static LocalDateTime convertDateStrToLocalDateTime(String input, String format) {
        if (isNullOrEmpty(input) || DataUtil.isNullOrEmpty(format)) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);

        DateTimeFormatter convertDateFormatter = new DateTimeFormatterBuilder().append(dateFormatter)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();

        try {
            return LocalDateTime.parse(input, convertDateFormatter);
        } catch (Exception ex) {
            log.error("convertDateStrToLocalDateTime error: ", ex);
            return null;
        }
    }

    /**
     * Ham format date
     *
     * @param value
     * @return
     */
    public static String convertDate2yyyyMMddStringNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMMdd");
            return yyyymm.format(value);
        } else {
            return "";
        }
    }
}
