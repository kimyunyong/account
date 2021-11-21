<%--
  Created by IntelliJ IDEA.
  User: yong
  Date: 2021-11-16
  Time: 오후 3:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../layout/header.jsp"%>
<div class="content">
    <form action="/loginProc" method="post">
        <div class="form-group">
            <label for="username">아이디</label>
            <input type="text" name="username" id="username" class="form-control" placeholder="Enter username" placeholder="아이디">
        </div>

        <div class="form-group">
            <label for="password">패스워드</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Enter password" placeholder="비밀번호">
        </div>

        <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">로그인</button>
       <%-- <div class="form-group form-check">
            <label class="form-check-label">
                <input class="form-check-input" type="checkbox"> Remember me
            </label>
        </div>--%>
    </form>
    <div class=text-center">
        <a href="${naver_url}"><img src="http://bwithmag.com/wp-content/uploads/2018/06/2%E1%84%82%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%87%E1%85%A5-N-%E1%84%85%E1%85%A9%E1%84%80%E1%85%A9.jpg" width="40" alt="naver login"></a>
        <%--<a href="${google_url}"><img src="https://cdn-teams-slug.flaticon.com/google.jpg" width="40" alt="google login"></a>--%>
        <a href="${kakao_url}"><img src="https://blog.kakaocdn.net/dn/Sq4OD/btqzlkr13eD/dYwFnscXEA6YIOHckdPDDk/img.jpg" width="40" alt="kakao login"></a>
    </div>
</div>
<%--<form action="/loginProc" method="post">
    <input type="text" name="id" placeholder="아이디">
    <input type="password" name="pw" placeholder="비밀번호">
    <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
    <button type="submit">로그인</button>
</form>

<p><a href="/joinForm">회원가입</a>--%>

<c:if test="${param.error eq true}">
    <script>

    </script>
</c:if>

<%@include file="../layout/footer.jsp"%>
