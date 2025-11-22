/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.core.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.slf4j.MDC;
import org.springframework.http.MediaType;

/**
 * <p>
 * Constants class contains a collection of constant values used across the
 * application. These constants include patterns for validation, media types,
 * logging titles, and other fixed values.
 * </p>
 *
 * @author hoangtien2k3
 */
public final class Constants {

    /**
     * Regular expression pattern for validating names.
     * <p>
     * The pattern allows uppercase and lowercase letters, as well as Vietnamese
     * characters, and whitespace.
     * </p>
     */
    public static final String NAME_PATTERN =
            "^[a-zA-ZàáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\\s]+$";

    /**
     * Regular expression pattern for validating email addresses.
     * <p>
     * The pattern matches common email formats.
     * </p>
     */
    public static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    /** Constant <code>DATE_PATTERN="\\d{2}[/]\\d{2}[/]\\d{4}"</code> */
    public static final String DATE_PATTERN = "\\d{2}[/]\\d{2}[/]\\d{4}";

    /** Constant <code>ID_NO_PATTERN="^[0-9\\-]+$"</code> */
    public static final String ID_NO_PATTERN = "^[0-9\\-]+$";

    /** Constant <code>NUMBER_PATTERN="^[0-9]+$"</code> */
    public static final String NUMBER_PATTERN = "^[0-9]+$";

    /** Constant <code>USERNAME_PATTERN="^[A-Za-z0-9_-]+$"</code> */
    public static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]+$";

    /** Constant <code>IMAGE_EXTENSION_LIST</code> */
    public static final List<String> IMAGE_EXTENSION_LIST =
            Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff", "tif", "svg", "raw", "psd", "ai", "eps");

    /** Constant <code>MAX_FILE_SIZE_MB=3</code> */
    public static final int MAX_FILE_SIZE_MB = 3;

    /** Constant <code>EMPLOYEE_CODE_LENGTH=6</code> */
    public static final int EMPLOYEE_CODE_LENGTH = 6;

    /** Constant <code>EMPLOYEE_CODE_MIN="000001"</code> */
    public static final String EMPLOYEE_CODE_MIN = "000001";

    /** List of visible media types for API responses. */
    public static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.TEXT_XML,
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_JSON_UTF8,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_XML,
            MediaType.MULTIPART_FORM_DATA);

    private static final List<String> SENSITIVE_HEADERS = Arrays.asList("authorization", "proxy-authorization");

    /**
     * Retrieves a list of sensitive headers.
     *
     * @return a {@link java.util.List} of sensitive header names.
     */
    public static List<String> getSensitiveHeaders() {
        return SENSITIVE_HEADERS;
    }

    /**
     * Contains constants related to SOAP headers.
     */
    public interface SoapHeaderConstant {
        String X_B3_TRACEID = "X-B3-TRACEID";
        String X_B3_TRACEID_VALUE_SOAP = MDC.get("X-B3-TraceId");
        String TYPE_XML_CHARSET_UTF8 = "text/xml; charset=utf-8";
        String TYPE_XML = "text/xml";
        String XYZ = "xyz";
    }

    /**
     * Contains common HTTP header types.
     */
    public interface HeaderType {
        String CONTENT_TYPE = "Content-Type";
        String X_API_KEY = "x-api-key";
    }

    /**
     * Contains date and time format patterns.
     */
    public interface DateTimePattern {
        String DMY = "dd/MM/yyyy";
        String DMY_HMS = "dd/MM/yyyy HH:mm:ss";
        String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
        String YYYYMMDD = "yyyy-MM-dd";
    }

    /**
     * Contains activation status constants.
     */
    public interface Activation {
        Integer ACTIVE = 1;
        Integer INACTIVE = 0;
    }

    /**
     * Contains security-related constants.
     */
    public interface Security {
        String AUTHORIZATION = "Authorization";
        String BEARER = "Bearer";
        String DEFAULT_REGISTRATION_ID = "oidc";
    }

    /**
     * Contains properties related to token information.
     */
    public interface TokenProperties {
        String USERNAME = "preferred_username";
        String ID = "sub";
        String NAME = "name";
        String EMAIL = "email";
    }

    /**
     * Contains error constants for Keycloak.
     */
    public interface KeyCloakError {
        String INVALID_GRANT = "INVALID_GRANT";
        String DISABLED = "DISABLED";
        String INVALID = "INVALID";
    }

    /**
     * Contains XML-related constants.
     */
    public interface XmlConst {
        String TAG_OPEN_RETURN = "<return>";
        String TAG_CLOSE_RETURN = "</return>";
        String AND_LT_SEMICOLON = "&lt;";
        String AND_GT_SEMICOLON = "&gt;";
        String LT_CHARACTER = "<";
        String GT_CHARACTER = ">";
    }

    /**
     * Contains logging-related titles and constants.
     */
    public interface LoggingTitle {
        String REQUEST = "-- REQUEST --";
        String REQUEST_HEADER = "-- REQUEST HEADER --";
        String REQUEST_PARAM = "-- REQUEST PARAM --";
        String REQUEST_BODY = "-- REQUEST BODY --";
        String RESPONSE = "-- RESPONSE --";
        String PREFIX = "|>";
        Integer BODY_SIZE_REQUEST_MAX = 1000;
        Integer BODY_SIZE_RESPONSE_MAX = 1000;
    }

    /**
     * Contains constants for sorting.
     */
    public interface Sorting {
        String SPLIT_OPERATOR = ",";
        String MINUS_OPERATOR = "-";
        String PLUS_OPERATOR = "+";
        String DESC = "desc";
        String ASC = "asc";
        String FILED_DISPLAY = "$1_$2";
    }

    /**
     * <p>
     * STATUS class contains constants representing various status values used
     * throughout the application. These constants are used to indicate the current
     * state of entities such as users, orders, and other resources.
     * </p>
     */
    public interface STATUS {
        Integer ACTIVE = 1;
        Integer INACTIVE = 0;
        Integer DELETE = -1;
    }

    /**
     * <p>
     * STATE class contains constants representing various state values used in the
     * application. Similar to STATUS, these constants reflect the operational state
     * of different components or entities.
     * </p>
     */
    public interface STATE {
        Integer ACTIVE = 1;
        Integer INACTIVE = 0;
    }

    /**
     * <p>
     * EXCLUDE_LOGGING_ENDPOINTS is a constant HashSet that contains a list of API
     * endpoints that should be excluded from logging. This can be useful for
     * avoiding clutter in logs for health check endpoints or other non-essential
     * requests.
     * </p>
     */
    public static final HashSet<String> EXCLUDE_LOGGING_ENDPOINTS = new HashSet<>(List.of("/actuator/health"));

    /**
     * <p>
     * MAX_BYTE is a constant representing the maximum number of bytes allowed for
     * certain operations in the application. This can be used to set limits on data
     * size, such as in file uploads or data transfers.
     * </p>
     */
    public static final int MAX_BYTE = 4096;

    /**
     * <p>
     * POOL class contains constants related to connection pools used for managing
     * resource consumption efficiently. In this case, it specifies the name of the
     * connection pool for REST clients.
     * </p>
     */
    public interface POOL {
        String REST_CLIENT_POLL = "Rest-client-Pool";
    }
}
