package com.est.mungpe.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class OAuthAttributes {
    private String name;
    private String email;
    private String provider;
    private String providerId;
    private String picture;

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return ofGoogle(attributes);
        }
        throw new IllegalArgumentException("Unknown provider: " + registrationId);
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return new OAuthAttributes(
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                "GOOGLE",
                (String) attributes.get("sub"),
                (String) attributes.get("picture")
        );
    }
}