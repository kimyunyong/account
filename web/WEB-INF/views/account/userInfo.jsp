<%--
  Created by IntelliJ IDEA.
  User: yong
  Date: 2021-11-20
  Time: 오후 2:12
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../layout/header.jsp"%>
<div class="content">
  <form action="/userInfoProc" method="post">
    <sec:csrfInput/>

    <div class="form-group">
      <label for="username">아이디</label>
      <input type="text" id="username" class="form-control" placeholder="" value="${userInfo.username}" readonly>
    </div>

    <div class="form-group">
      <label for="name">이름</label>
      <input type="text" class="form-control" id="name" placeholder="" value="${userInfo.name}" readonly>
    </div>

    <div class="form-group">
      <label for="nickname">닉네임</label>
      <input type="text" class="form-control" id="nickname" name="nickname" value="${userInfo.nickname}" placeholder="Enter nickname">
    </div>

    <div class="form-group">
      <label for="email">이메일</label>
      <input type="text" class="form-control" id="email" name="email" value="${userInfo.email}" placeholder="Enter email">
    </div>
  </form>

  <button id="btn-save" class="btn btn-primary">수정</button>
</div>
<%@include file="../layout/footer.jsp"%>

<script>

  let index  = {
    init : function (){
      $('#btn-save').on('click', ()=>{
        this.save();
      });
    },

    save: function(){
      let data = {
        username:$('#username').val(),
        nickname:$('#nickname').val(),
        email: $('#email').val(),
        token: '${_csrf.token}',
        header: '${_csrf.headerName}'
      }

      $.ajax({
        type: 'PUT',
        url: '/userInfoProc',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
          xhr.setRequestHeader(data.header, data.token);
        }
      }).done(function(resp){
        alert('수정이 완료되었습니다.');
        location.href='/userInfo';
      }).fail(function(error){
        alert(JSON.stringify(error));
      });
    }
  }

  index.init();

</script>
