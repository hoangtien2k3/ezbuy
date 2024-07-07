package com.ezbuy.framework.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.util.WebUtils;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class Translators {
    private static MessageSource messageSource;
    private static LocaleResolver localeResolver;
    private static final Locale defaultLocale = Locale.forLanguageTag("vi");

    public Translators(MessageSource messageSource) {
        Translators.messageSource = messageSource;
    }

    public static String toLocaleVi(String messageKey, Object... objects) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey, objects, defaultLocale);
    }
}
