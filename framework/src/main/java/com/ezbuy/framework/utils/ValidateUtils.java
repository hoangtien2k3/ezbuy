package com.ezbuy.framework.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ezbuy.framework.constants.Regex.LINK;
import static com.ezbuy.framework.constants.Regex.NUMBER_REGEX;

@Slf4j
public class ValidateUtils {

    public static boolean validateRegex(String input, String regexPattern) {
        return !DataUtil.isNullOrEmpty(input) && input.matches(regexPattern);
    }

    public static boolean validateRegexV2(String input, String regexPattern) {
        if (DataUtil.isNullOrEmpty(input)) {
            return true;
        }
        return input.matches(regexPattern);
    }

    public static boolean validatePhone(String phone) {
        if (DataUtil.isNullOrEmpty(phone)) {
            return true;
        }
        if (!phone.matches(NUMBER_REGEX)) {
            return false;
        }

        return phone.length() >= 9 && phone.length() <= 11;
    }

    public static boolean validateUuid(String uuidValue) {
        if (DataUtil.isNullOrEmpty(uuidValue)) {
            return false;
        }
        try {
            return DataUtil.safeEqual(UUID.fromString(uuidValue).toString(), uuidValue);
        } catch (Exception ex) {
            log.error("invalid uuid: ", ex);
            return false;
        }
    }

    public static boolean validateLink(String link) {
        if (DataUtil.isNullOrEmpty(link)) {
            return false;
        }

        Pattern pattern = Pattern.compile(LINK);
        Matcher matcher = pattern.matcher(link);

        return matcher.matches();
    }
}
