<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="Top.css">
<title>Top</title>
</head>
<body>
  <header>
 　<div class="wrapper">
     <div class="left">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
     </div>
     <div class="right">
       <nav>
         <ul class="list">
           <li><a href="login.jsp">ログイン</a></li>
           <li><a href="register.jsp">会員登録</a></li>
         </ul>
      </nav>
    </div>
    </div>
 </header>
  <c:if test="${logoutFlg == true }">
     <p>ログアウトしました</p>
  </c:if>
 <footer></footer>
</body>
</html>