package com.nandsoft.account.auth;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.nandsoft.account.model.User;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Iterator;

public class SNSLogin {
    private OAuth20Service oAuthService;
    private SnsValue sns;

    public SNSLogin(SnsValue sns){
        this.oAuthService = new ServiceBuilder(sns.getClientId())
                .apiSecret(sns.getClientSecret())
                .callback(sns.getRedirectUrl())
                .scope("profile")
                .build(sns.getApi20Instance());
        this.sns = sns;
    }

    public String getNaverAuthURL(){
        return this.oAuthService.getAuthorizationUrl();
    }

    public User getUserProfile(String code) throws Exception{
        OAuth2AccessToken accessToken = oAuthService.getAccessToken(code);  // 토큰을 가지고 온다.

        OAuthRequest request = new OAuthRequest(Verb.GET, this.sns.getProfileUrl());
        oAuthService.signRequest(accessToken, request);

        Response response = oAuthService.execute(request);

        return parseJson(response.getBody());
    }

    private User parseJson(String body) throws Exception {
        System.out.println("test :: ====================> " + body);
        User user = new User();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(body);
        /*if(sns.isGoogle()) {
            *//*String id = rootNode.get("id").getValueAsText();*//*
            *//*user.setGoogleid(id);*//*

            *//* String displayName = rootNode.get("displayName").getValueAsText();*//*
            user.setNickname(rootNode.get("displayName").getValueAsText());
            JsonNode nameNode = rootNode.path("name");  // 자식노드를 가지고 오려면 JsonNode타입으로 받는다.
            *//*String name = nameNode.get("familyName").getValueAsText() + nameNode.get("givenName").getValueAsText();*//* // 자식노드를 사용한다.
            user.setName(nameNode.get("familyName").getValueAsText() + nameNode.get("givenName").getValueAsText());
            Iterator<JsonNode> iterEmails = rootNode.path("emails").getElements();
            while (iterEmails.hasNext()) {
                JsonNode emailNode = iterEmails.next();
                String type = emailNode.get("type").getValueAsText();
                if (StringUtils.equals(type, "account")) {
                    user.setEmail(emailNode.get("value").getValueAsText());
                    break;
                }
            }
        }*/
        if(sns.isNaver()){
            JsonNode resNode = rootNode.get("response");
            user.setUsername(resNode.get("id").getValueAsText());   // username
            user.setNaverid(resNode.get("id").getValueAsText());
            user.setNickname(resNode.get("nickname").getValueAsText());
            user.setName(resNode.get("name").getValueAsText());
            user.setEmail(resNode.get("email").getValueAsText());
            user.setOauthType("naver");
        }

        return user;
    }

}
