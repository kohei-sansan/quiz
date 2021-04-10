
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="register.css">
<title>登録内容入力</title>
</head>
<body>
<header>
  　<div class="wrapper">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
    </div>
 </header>
 <div class="error">
   <c:if test="${not empty userExists }">
     <p>入力されたidは使用できません</p>
   </c:if>
   <c:if test="${not empty isPassBlank }">
     <p>パスワードが空欄です</p>
   </c:if>
   <c:if test="${not empty isUserNameBlank }">
     <p>ユーザー名が空欄です</p>
   </c:if>
 </div>
  <form action="regiServlet" method="post">
    <p>
      ユーザーID<input type="text" name="userId" value="${requestScope.toRegisterId }">
    </p>
    <p>
      パスワード<input type="password" name="password" value="${requestScope.toRegisterPassword }">
    </p>
    <p>
      ユーザー名<input type="text" name="userName" value="${requestScope.toRegisterName }">
    </p>
    <p>
      <button type="submit">登録</button>
    </p>
  </form>
</body>
</html>