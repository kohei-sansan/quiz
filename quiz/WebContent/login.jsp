<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="login.css">
<title>ログイン画面</title>
</head>
<body>
<header>
  　<div class="wrapper">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
    </div>
 </header>
 <div class="error">
   <c:if test="${isIdBlank == true }">
     <p>idが空欄です</p>
   </c:if>
   <c:if test="${isPassBlank == true }">
     <p>パスワードが空欄です</p>
   </c:if>
   <c:if test="${notUserExist == true }">
     <p>id、またはパスワードが間違っています</p>
   </c:if>
 </div>
  <form action="loginServlet" method="post">
    <p>
      ユーザーID<input type="text" name="userId">
    </p>
    <p>
      パスワード<input type="password" name="password">
    </p>
    <p>
      <button type="submit">ログイン</button>
    </p>
  </form>
</body>
</html>