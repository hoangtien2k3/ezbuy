package com.ezbuy.framework.utils;

import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class Translator {
    private static final Locale defaultLocale = Locale.forLanguageTag("vi");
    private static ReloadableResourceBundleMessageSource messageSource;

    @Qualifier("localeContextResolver2")
    private static LocaleContextResolver localeContextResolver;

    public Translator(
            ReloadableResourceBundleMessageSource messageSource, LocaleContextResolver localeContextResolver) {
        Translator.messageSource = messageSource;
        Translator.localeContextResolver = localeContextResolver;
    }

    public static String toLocaleVi(String msgCode, Object... params) {
        if (msgCode == null) return "";

        return messageSource.getMessage(msgCode, params, defaultLocale);
    }

    public static String toLocale(String msgCode, ServerWebExchange exchange, Object... params) {
        if (msgCode == null) return "";
        Locale locale;
        if (exchange == null) {
            locale = defaultLocale;
        } else {
            locale = localeContextResolver.resolveLocaleContext(exchange).getLocale();
        }
        return messageSource.getMessage(
                msgCode, params, locale == null ? Objects.requireNonNull(defaultLocale) : locale);
    }

    public static Mono<String> toLocaleMono(String msgCode, ServerWebExchange exchange, Object... params) {
        if (msgCode == null) return Mono.just("");
        Locale locale;
        if (exchange == null) {
            locale = defaultLocale;
        } else {
            locale = localeContextResolver.resolveLocaleContext(exchange).getLocale();
        }
        return Mono.fromSupplier(() -> messageSource.getMessage(
                        msgCode, params, locale == null ? Objects.requireNonNull(defaultLocale) : locale))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public static String toLocale(String msgCode, Object... params) {
        if (msgCode == null) return "";
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, params, locale);
    }

    public static String toLocale(String msgCode) {
        return Translator.toLocale(msgCode, (Object) null);
    }

    public static Mono<String> toLocaleMono(String msgCode) {
        return Translator.toLocaleMono(msgCode, null);
    }
}
