<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="registerConfirm.css">
<title>登録内容確認</title>
</head>
<body>
<header>
  　<div class="wrapper">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
    </div>
 </header>
 <p>この内容でよろしいですか</p>
<form action="regiCompServlet" method="post">
    <p>
      ユーザーID<input type="text" name="userId" value="${requestScope.toRegisterId }" readonly>
    </p>
    <p>
      パスワード<input type="password" name="password" value="${requestScope.toRegisterPassword }" readonly>
    </p>
    <p>
      ユーザー名<input type="text" name="userName" value="${requestScope.toRegisterName }" readonly>
    </p>
    <p>
      <button type="submit" name="btn" value="register">はい</button>
      <button type="submit" name="btn" value="back">戻る</button>
    </p>
  </form>
</body>
</html>