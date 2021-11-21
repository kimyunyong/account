<%--
  Created by IntelliJ IDEA.
  User: yong
  Date: 2021-11-16
  Time: 오후 3:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../layout/header.jsp"%>

<form action="/loginProc" method="post">
    <div class="form-group">
        <label for="username">username :</label>
        <input type="text" name="username" id="username" class="form-control" placeholder="Enter username" placeholder="아이디">
    </div>

    <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" class="form-control" id="password" name="password" placeholder="Enter password" placeholder="비밀번호">
    </div>

    <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
    <button type="submit" class="btn btn-primary">Submit</button>
   <%-- <div class="form-group form-check">
        <label class="form-check-label">
            <input class="form-check-input" type="checkbox"> Remember me
        </label>
    </div>--%>
</form>

<div class=text-center">
    <a href="${naver_url}"><img src="/img/naver_login.png" width="300" alt="naver login"></a>
    <a href="${google_url}"><img src="/img/google_login.png" width="300" alt="google login"></a>
    
</div>



<%--<form action="/loginProc" method="post">
    <input type="text" name="id" placeholder="아이디">
    <input type="password" name="pw" placeholder="비밀번호">
    <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
    <button type="submit">로그인</button>
</form>

<p><a href="/joinForm">회원가입</a>--%>

<%@include file="../layout/footer.jsp"%>
