package com.nandsoft.account.auth;

import com.fasterxml.jackson.annotation.JacksonInject;
import lombok.Data;

@Data
public class KakaoProfile {
    private Integer id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    public String getEmail(){
        return this.kakao_account.getEmail();
    }

    public String getNickName(){
        return this.properties.getNickname();
    }
}

@Data
class Properties {
    private String nickname;
}

@Data
class KakaoAccount {
    private Boolean profile_nickname_needs_agreement;
    private Profile profile;
    private Boolean has_email;
    private Boolean email_needs_agreement;
    private Boolean is_email_valid;
    private Boolean is_email_verified;
    private String email;
}

@Data
class Profile {
    private String nickname;
}
