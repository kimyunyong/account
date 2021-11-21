<%--
  Created by IntelliJ IDEA.
  User: yong
  Date: 2021-11-15
  Time: 오후 1:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../layout/header.jsp"%>
<div class="content">

    <form action="/joinProc" method="post">
        <sec:csrfInput/>
        <p class='text-info'>*** 모두 필수 입력하셔야 합니다. ***</p>
        <div class="form-group">
            <label for="username"><span class="text-info">*</span>아이디 <span id="username_confirm" style="color : red; margin-left: 20px;"></span></label>
            <input type="text" name="username" id="username" class="form-control" placeholder="Enter username">
        </div>

        <div class="form-group">
            <label for="password"><span class="text-info">*</span>패스워드<span id="password_confirm" style="color : red; margin-left: 20px;"></span></label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Enter password">
            <p class="password_confirm"></p>
        </div>

        <div class="form-group">
            <label for="passwordChk"><span class="text-info">*</span>패스워드 재 확인</label>
            <input type="password" class="form-control" id="passwordChk" placeholder="Enter password check">
        </div>

        <div class="form-group">
            <label for="name"><span class="text-info">*</span>이름</label>
            <input type="text" class="form-control" id="name" name="name" placeholder="Enter name">
        </div>

        <div class="form-group">
            <label for="nickname"><span class="text-info">*</span>닉네임 </label>
            <input type="text" class="form-control" id="nickname" name="nickname" placeholder="Enter nickname">
        </div>

        <div class="form-group">
            <label for="email"><span class="text-info">*</span>이메일</label>
            <input type="text" class="form-control" id="email" name="email" placeholder="Enter email">
        </div>

        <%-- <div class="form-group form-check">
             <label class="form-check-label">
                 <input class="form-check-input" type="checkbox"> Remember me
             </label>
         </div>--%>
    </form>

    <button id="btn-save" class="btn btn-primary">회원가입</button>

</div>

<%@include file="../layout/footer.jsp"%>

<script>
    function usernameValidate(){
        let data = {
            username: $("#username").val(),
            token: '${_csrf.token}',
            header: '${_csrf.headerName}'
        }

        let idReg = /^[a-z]+[a-z0-9]{5,19}$/g;
        if( !idReg.test( data.username ) ) {
            $('#username_confirm').text('*아이디는 영문자로 시작하는 6~20자 영문자 또는 숫자이어야 합니다.');
            return false;
        }

        $.ajax({
            type: 'POST',
            url: '/idConfirm',
            data:  JSON.stringify(data),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(data.header, data.token);
            }
        }).done(function(resp){
            if(resp.result > 0){
                $('#username_confirm').text('*사용할 수 없는 아이디 입니다.');
                return false;
            }else {
                $('#username_confirm').text('');
            }
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    }

    function passwordValidate(){
        let data = {
            password: $('#password').val(),
            passwordChk: $('#passwordChk').val()
        }

        if(data.password == ""){
            $('#password_confirm').text('패스워드를 입력하세요');
            return false;
        }

        if(data.password != "" || data.passwordChk != ""){
            if(data.password != data.passwordChk){
                $('#password_confirm').text('*비밀번호가 서로 다릅니다.');
                return false;
            }
        }

        let reg = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
        if(false === reg.test(data.password)) {

            $('#password_confirm').text('*비밀번호는 8자 이상이어야 하며, 숫자/대문자/소문자/특수문자를 모두 포함해야 합니다.');
            return false;
        }

        $('#password_confirm').text('');
    }

    $('#passwordChk').focusout(function(){
        passwordValidate();
    });

    $('#username').focusout(function() {
        usernameValidate();
    });

    let index  = {
        init : function (){
            $('#btn-save').on('click', ()=>{
                let x = usernameValidate();
                let y = passwordValidate();

                if(usernameValidate() != false && passwordValidate() != false){
                    this.save();
                }else{
                    return false;
                }
            });
        },

        save: function(){
            let data = {
                    username: $('#username').val(),
                    password: $('#password').val(),
                    name: $('#name').val(),
                    nickname:$('#nickname').val(),
                    email: $('#email').val(),
                    token: '${_csrf.token}',
                    header: '${_csrf.headerName}'
                }

            $.ajax({
                type: 'POST',
                url: '/joinProc',
                data: JSON.stringify(data),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                    xhr.setRequestHeader(data.header, data.token);
                }
            }).done(function(resp){
                if(resp.result){
                    alert('회원가입이 완료되었습니다.');
                    location.href='/login';
                }
            }).fail(function(error){
                alert(JSON.stringify(error));
            });
        }
    }
    index.init();

    /*function chkPwd(str){
        let pw = str;
        let num = pw.search(/[0-9]/g);
        let eng = pw.search(/[a-z]/ig);
        let spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

        if(pw.length < 8 || pw.length > 20){
            alert("8자리 ~ 20자리 이내로 입력해주세요.");
            return false;
        }

        if(pw.search(/₩s/) != -1){
            alert('비밀번호는 공백업이 입력해주세요.');
            return false;
        } if(num < 0 || eng < 0 || spe < 0 ){
            alert('영문,숫자, 특수문자를 혼합하여 입력해주세요.');
            return false;
        }

        return true;
    }

    $('#password').on('focusout', function () {
        var pw1 = $('#password').val();
        var pw2 = $('#passwordChk').val();

        if ( pw1 != '' && pw2 == '' ) {
            null;
        } else if (pw1 != "" || pw2 != "") {
            if (pw1 != pw2) {
                alert("비밀번호가 일치하지 않습니다. 비밀번호를 재확인해주세요.");
            }
        }
    });*/


</script>