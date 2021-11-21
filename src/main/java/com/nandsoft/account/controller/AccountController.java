package com.nandsoft.account.controller;

import com.nandsoft.account.auth.*;
import com.nandsoft.account.model.ResponseDto;
import com.nandsoft.account.model.User;
import com.nandsoft.account.security.PrincipalDetail;
import com.nandsoft.account.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private SnsValue naverSns;

    @Autowired
    private SnsValue googleSns;

    @Autowired
    private GoogleConnectionFactory googleConnectionFactory;

    @Autowired
    private OAuth2Parameters googleOAuth2Parameters;

    @GetMapping("/login")
    public String login(Model model){
        log.debug("로그인 페이지 이동!!");

        SNSLogin snsLogin = new SNSLogin(naverSns);


        /*SNSLogin googleLogin = new SNSLogin(googleSns);
        model.addAttribute("google_url", snsLogin.getNaverAuthURL());*/

        /* 구글code 발행을 위한 URL 생성 */
        /*OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
        String url = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);*/

        String google_url = "https://accounts.google.com/o/oauth2/v2/auth?" +
        "scope=openid https://www.googleapis.com/auth/userinfo.email&" +
            "access_type=offline&" +
            "include_granted_scopes=true&state=state_parameter_passthrough_value&" +
            "redirect_uri=http://localhost:8080/auth/google/callback&" +
            "response_type=code&" +
            "client_id=972153800025-6fm2rh4h06plcim96llh8c2qbj8s7pqm.apps.googleusercontent.com";

        model.addAttribute("naver_url", snsLogin.getNaverAuthURL());
        model.addAttribute("google_url", google_url);
        model.addAttribute("kakao_url", "https://kauth.kakao.com/oauth/authorize?client_id=48638d8683d005d9987b5672c32a9bd3&redirect_uri=http://localhost:8080/auth/kakao/callback&response_type=code");
        return "/account/login";
    }

    @RequestMapping(value="/auth/{snsService}/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public String snsLoginCallback(@PathVariable String snsService, Model model, @RequestParam String code, HttpSession session) throws Exception {
        log.info("test :: ==========================>", snsService);
        log.info("test :: ==========================>", code);

        // 1. code를 이용해서 access_token을 받는다.
        // 2. access_token을 이용해서 사용자 profile 정보를 가지고 온다.
        SnsValue sns = null;
        User snsUser = null;
        if(StringUtils.equals("naver", snsService)){
            sns = naverSns;
            SNSLogin snsLogin = new SNSLogin(sns);
            snsUser = snsLogin.getUserProfile(code);   // 1, 2번을 실행
        }else if((StringUtils.equals("kakao", snsService))){
            // Retrofit2, OkHttp, RestTemplate
            RestTemplate rt = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.add("grant_type", "authorization_code");
            parameters.add("client_id", "48638d8683d005d9987b5672c32a9bd3");
            parameters.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
            parameters.add("code", code);

            HttpEntity<MultiValueMap<String,String>> kakoTokenRequest = new HttpEntity<>(parameters,headers);
            URI uri = URI.create("");
            ResponseEntity<String> reponse = rt.exchange(
                    "https://kauth.kakao.com//oauth/token",
                    HttpMethod.POST,
                    kakoTokenRequest,
                    String.class
            );

            log.info("kakao token body : ===================================>>>>>" + reponse.getBody());
            ObjectMapper mapper = new ObjectMapper();
            KakaoOauthToken oauthToken = mapper.readValue(reponse.getBody(), KakaoOauthToken.class);

            log.info("kakao token : =========================================>>>>>>>" + oauthToken.getAccess_token());
            // 받은 토큰으로 회원 정보 요청
            RestTemplate rt2 = new RestTemplate();
            HttpHeaders headers2 = new HttpHeaders();
            headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());

            HttpEntity<MultiValueMap<String,String>> kakoProfileRequest = new HttpEntity<>(headers2);
            ResponseEntity<String> reponse2 = rt2.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakoProfileRequest,
                    String.class
            );
            log.info("kakao user profile body : ===================================>>>>>" + reponse2.getBody());
            try {
                ObjectMapper mapper2 = new ObjectMapper();
                KakaoProfile kakaoProfile = mapper2.readValue(reponse2.getBody(), KakaoProfile.class);

                log.info("카카오 아이디 번호 " + kakaoProfile.getId());
                log.info("카카오 이메일 " + kakaoProfile.getEmail());
                log.info("카카오 닉네임" + kakaoProfile.getNickName());
                snsUser = new User();
                snsUser.setUsername(String.valueOf(kakaoProfile.getId()));   // username
                snsUser.setNaverid(String.valueOf(kakaoProfile.getId()));
                snsUser.setNickname(kakaoProfile.getNickName());
                snsUser.setName("");                                        // 카카오는 이름을 제공하지 않음..
                snsUser.setEmail(kakaoProfile.getEmail());
                snsUser.setOauthType("kakao");
            }catch (JsonMappingException e){
                e.printStackTrace();
            }catch (JsonProcessingException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }


            model.addAttribute("result", reponse2.getBody());

            // 구글 진행중 (403 error 해결 필요..)
        } else {
            sns = googleSns;
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.add("code", code);
            parameters.add("client_id", sns.getClientId());
            parameters.add("client_secret", sns.getClientSecret());
            parameters.add("redirect_uri", sns.getRedirectUrl());
            parameters.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String,String>> rest_request = new HttpEntity<>(parameters,headers);
            URI uri = URI.create("https://www.googleapis.com/oauth2/v4/token");
            ResponseEntity<String> rest_reponse;
            rest_reponse = restTemplate.postForEntity(uri, rest_request, String.class);
            String bodys = rest_reponse.getBody();

            ObjectMapper mapper = new ObjectMapper();

            GoogleOauthToken googleToken = mapper.readValue(bodys, GoogleOauthToken.class);
            log.info("bodys :: ==================================>>>" + bodys);
            System.out.println("access_token :: " + googleToken.getAccess_token());

            // 받아온 토큰으로 사용자 정보 get
            HttpHeaders headers2 = new HttpHeaders();
            RestTemplate restTemplate2 = new RestTemplate();
            headers2.add("Content-type", "application/x-www-form-urlencoded; charset=utf8");
            headers2.add("Authorization", "Bearer " + googleToken.getAccess_token());

            HttpEntity<MultiValueMap<String,String>> rest_request2 = new HttpEntity<>(headers2);
            URI uri2 = URI.create("https://people.googleapis.com/v1/contactGroups");
            ResponseEntity<String> rest_reponse2;
            rest_reponse2 = restTemplate2.postForEntity(uri2, rest_request2, String.class);
            String bodys2 = rest_reponse2.getBody();

            log.info("result ====================================================>>" + bodys2);
        }

        // 3. 미존재시 가입, 존재시 강제 security 로그인
        userService.insertUser(snsUser);

        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>(1);
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        PrincipalDetail principalDetail = new PrincipalDetail(snsUser);
        // UsernamePasswordAuthenticationToken 의 함수를 확인해야 함.. customAuthenticationProvider 객체에서는  principalDetail이 아닌 username을 넣었음..
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal() 여기서 꺼낸값이 principalDetail객체가 아니여서 에러가 나서 이렇게 해봤는데 잘됨..
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principalDetail, null, roles);
        SecurityContextHolder.getContext().setAuthentication(auth);
        auth.setDetails(principalDetail);

        /*Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrincipalDetail principalDetails = (PrincipalDetail)principal;*/

        /*model.addAttribute("result", snsUser.getName() + "님 반갑습니다.");*/

        return "redirect:/index";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
       /* User user = new User("kyy8", "1234", "email@gmail.com", "Role_User", null);
        userService.join(user);*/
        return "/account/joinForm";
    }

    @PostMapping("/idConfirm")
    @ResponseBody
    public Map<String, Integer> idConfirm(@RequestBody User user){
        log.debug("test :: =============================> 아이디 체크");
        int result = userService.idConfirm(user);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("result", result);

        return map;
    }

    @PostMapping("/joinProc")
    @ResponseBody
    public Map<String, Integer> joinProc(@RequestBody User user){
        log.debug("test :: ======================>" + user.toString());

        int result = userService.insertUser(user);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("result", result);

        return map;
    }

    @GetMapping("/userInfo")
    public String userInfo(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrincipalDetail principalDetails = (PrincipalDetail)principal;

        User getUser = userService.getUser(principalDetails.getUser());
        log.debug("test ::: ===========================>" + getUser.toString());

        model.addAttribute("userInfo", getUser);
        return "/account/userInfo";
    }

    @PutMapping("/userInfoProc")
    @ResponseBody
    public ResponseDto<Integer> userInfoProc(@RequestBody User user){
        log.debug("test :: ============================>" + user.getEmail() + ", " + user.getUsername() );
        userService.updateUser(user);
        return new ResponseDto<Integer>(HttpStatus.OK, 1);
    }

    /*시큐리티가 가로챈다.*/
  /*  @PostMapping("/loginProc")
    public String loginProc(String username, String password){

        logger.debug("로그인 작동!!");
        return "/index";
    }*/

    @GetMapping("/logoutProc")
    public String loginOut(){

        log.debug("로그아웃 작동!!");
        return "/index";
    }
}
