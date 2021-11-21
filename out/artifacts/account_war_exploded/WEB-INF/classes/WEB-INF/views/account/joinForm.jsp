<%--
  Created by IntelliJ IDEA.
  User: yong
  Date: 2021-11-15
  Time: 오후 1:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../layout/header.jsp"%>

<form action="/joinProc" method="post">
    <sec:csrfInput/>

    <div class="form-group">
        <label for="username">username :</label>
        <input type="text" name="username" id="username" class="form-control" placeholder="Enter username" placeholder="아이디">
    </div>

    <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" class="form-control" id="password" name="password" placeholder="Enter password" placeholder="비밀번호">
    </div>

    <div class="form-group">
        <label for="email">email:</label>
        <input type="text" class="form-control" id="email" name="email" placeholder="Enter email" placeholder="이메일">
    </div>
    <%-- <div class="form-group form-check">
         <label class="form-check-label">
             <input class="form-check-input" type="checkbox"> Remember me
         </label>
     </div>--%>
    <button id="btn-save" class="btn btn-primary">회원가입</button>
</form>


<script>
    /*let index  = {
        init : function (){
            $('btn-save').on('click', ()=>{
                this.save();
            });
        },

        save: function(){
            let data = {
                username: $('#username').val(),
                password: $('#password').val(),
                email: $('#email').val()
            }

            $.ajax({
                type: 'POST',
                url: '/joinProc',
                data: JSON.stringify(data),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json'
            }).done(function(){
                alert('회원가입이 완료되었습니다.');
                location.href='/login'
            }).fail(function(error){
                alert(JSON.stringify(error));
            });
        }
    }*/
</script>

<%@include file="../layout/footer.jsp"%>