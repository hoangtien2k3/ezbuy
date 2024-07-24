package com.ezbuy.framework.utils;

public class PageUtils {
    public static int getOffset(Integer page, Integer size) {
        if (page == null || page <= 0 || size == null || size <= 0) return 0;
        return (page - 1) * size;
    }
}
