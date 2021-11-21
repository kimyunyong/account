<%--
  Created by IntelliJ IDEA.
  User: yong
  Date: 2021-11-16
  Time: 오후 4:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Index</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="_csrf" th:content="${_csrf.token}"/>
    <meta name="_csrf_header" th:content="${_csrf.headerName}"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<style>
    .content{
        height: 680px;
    }
</style>
</head>
<body>

<nav class="navbar navbar-expand-md bg-dark navbar-dark">
    <a class="navbar-brand" href="/index">계정관리</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="navbar-nav">
            <sec:authorize access="isAnonymous()">
                <li class="nav-item">
                    <a class="nav-link" href="/login">로그인</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link" href="/joinForm">회원가입</a>
                </li>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <li class="nav-item">
                    <a href="/userInfo" class="nav-link">MY PAGE</a>
                </li>
                <li class="nav-item">
                    <a href="#" onclick="document.getElementById('logout').submit();" class="nav-link">로그아웃</a>
                </li>
            </sec:authorize>
        </ul>

        <form id="logout" action="/logoutProc" method="POST">
            <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
        </form>
    </div>
</nav>
<br>
