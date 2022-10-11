package com.youtube.clone.youtubeclone.configurations;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final String audience;

    public AudienceValidator(String audience) {
        this.audience = audience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        System.out.println(token.getClaims());
//        System.out.println(token.getHeaders());
//        System.out.println(token.getId());
//        System.out.println(token.getExpiresAt());
//        System.out.println(token.getSubject());
        System.out.println(token.getTokenValue());
//        System.out.println(token.getAudience().contains(audience));
        if (token.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }

        return OAuth2TokenValidatorResult.failure(new OAuth2Error("Invalid audience for a given token."));
    }
}
