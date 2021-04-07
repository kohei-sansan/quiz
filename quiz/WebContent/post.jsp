<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>問題登録</title>
</head>
<body>
<header>
  　<div class="wrapper">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
    </div>
 </header>
 <div class="error">
   <c:if test="${not empty wordEmpty }">
     <p>英単語を記入してください</p>
   </c:if>
   <c:if test="${not empty answerEmpty }">
     <p>答えを記入してください</p>
   </c:if>
   <c:if test="${not empty optionEmpty }">
     <p>間違い用単語を全て記入してください</p>
   </c:if>
   <c:if test="${not empty wordExists }">
     <p>すでに登録されている単語です</p>
   </c:if>
 </div>
  <form action="postServlet" method="post" style="text-align: center;">
    <p>
      英単語<input type="text" name="word" value="${requestScope.toRegisterWord }">
    </p>
    <p>
      答え<input type="text" name="answer" value="${requestScope.toRegisterAnswer }">
    </p>
    <p>
      間違い用1<input type="text" name="option1" value="${requestScope.toRegisterOption1 }">
    </p>
    <p>
      間違い用2<input type="text" name="option2" value="${requestScope.toRegisterOption2 }">
    </p>
    <p>
      間違い用3<input type="text" name="option3" value="${requestScope.toRegisterOption3 }">
    </p>
    <p>
      <button type="submit">登録</button>
    </p>
  </form>
</body>
</html>