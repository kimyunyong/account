package com.nandsoft.account.auth;

import lombok.Data;

@Data
public class KakaoOauthToken {
    private String access_token;
    private String token_type;
    private String scope;
    private String refresh_token;
    private String expires_in;
    private String refresh_token_expires_in;
}
