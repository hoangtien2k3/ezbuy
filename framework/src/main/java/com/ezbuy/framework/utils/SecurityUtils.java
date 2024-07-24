package com.ezbuy.framework.utils;

import java.security.SignatureException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;

import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.SignedJWT;

import com.ezbuy.framework.constants.Constants;
import com.ezbuy.framework.factory.ObjectMapperFactory;
import com.ezbuy.framework.model.TokenUser;
import com.ezbuy.framework.model.UserDTO;

import reactor.core.publisher.Mono;

public class SecurityUtils {
    public static Mono<TokenUser> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.justOrEmpty(extractUser(authentication)));
    }

    public static Mono<String> getTokenUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.justOrEmpty(extractToken(authentication)));
    }

    public static String extractToken(Authentication authentication) {
        if (authentication == null) return null;
        Jwt jwt = (Jwt) authentication.getPrincipal();
        if (jwt == null) return null;
        return jwt.getTokenValue();
    }

    public static TokenUser extractUser(Authentication authentication) {
        if (authentication == null) return null;
        Jwt jwt = (Jwt) authentication.getPrincipal();
        if (jwt == null) return null;
        Map<String, Object> claims = jwt.getClaims();
        if (claims == null) return null;
        TokenUser user = TokenUser.builder()
                .username((String) claims.get(Constants.TokenProperties.USERNAME))
                .id((String) claims.get(Constants.TokenProperties.ID))
                .email((String) claims.get(Constants.TokenProperties.EMAIL))
                .name((String) claims.get(Constants.TokenProperties.NAME))
                //                .individualId((String) claims.get(Constants.TokenProperties.INDIVIDUAL_ID))
                .organizationId(DataUtil.safeToString(claims.get(Constants.TokenProperties.ORGANIZATION_ID)))
                .build();
        return user;
    }

    public static String hmac(String data, String key, String algorithm) throws SignatureException {
        String result;

        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = String.valueOf(Base64.encode(rawHmac));
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e);
        }
        return result;
    }

    public static Mono<Boolean> isAuthorized() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    if (authentication == null || authentication.getPrincipal() == null) {
                        return Mono.just(false);
                    }
                    return Mono.just(true);
                })
                .switchIfEmpty(Mono.just(false));
    }

    public static UserDTO getUserByAccessToken(String accessToken) {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(accessToken);
            String data = signedJWT.getPayload().toString();
            return ObjectMapperFactory.getInstance().readValue(data, UserDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}
