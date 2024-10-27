package com.ezbuy.paymentservice.service;

import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SecurityUtils;

import java.security.SignatureException;

public class Test {
    public static void main(String[] args) {
        String data = DataUtil.sumListString("HUBSME", "HUBSME", "df3f9932-7157-4d35-bd10-99fd0f77934f", String.valueOf(10000).replace(",", ""));
        System.out.println(createChecksum(data, "01f897d6697fb63d089ca312cae2bbfe540ee4062aa791b3e2b14441bcf5df7d"));
    }

    private static String createChecksum(String data, String hashCode) {
        try {
            return SecurityUtils.hmac(data, hashCode, "HmacSHA1");
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}
