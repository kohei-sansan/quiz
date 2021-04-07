<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>クイズに挑戦中</title>
</head>
<body>
<header>
  　<div class="wrapper">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
    </div>
 </header>
 <p style="text-align: center"><c:out value="${currentQuestion.word }" /></p>
 <form action="takingQuizServlet" method="post" style="text-align: center">
 <!-- 問題の選択肢をforEachで表示する -->
  <c:forEach var="obj" items="${currentQuestion.options}" varStatus="status">
    <!-- ユーザーの現在の問題に対する回答とオプションが一致→チェックをつける -->
    <c:if test="${sessionScope.userChoice == obj}" >
　　  <input type="radio" name="option" value="${obj }" checked="checked">
　　  <c:out value="${obj}"/><br>
    </c:if>
    <c:if test="${sessionScope.userChoice != obj}" >
　　  <input type="radio" name="option" value="${obj }">
　　  <c:out value="${obj}"/><br>
    </c:if>
  </c:forEach>
  <!-- 問題のカウンタに応じて、表示するリンクを分岐 -->
  <c:if test="${sessionScope.quizCount != 0}" >
    <button type="submit" name="btn" value="back">前の問題へ</button>
  </c:if>
  <c:if test="${sessionScope.quizCount == 9}" >
    <button type="submit" name="btn" value="finish">採点する</button>
  </c:if>
  <c:if test="${sessionScope.quizCount != 9}" >
    <button type="submit" name="btn" value="next">次の問題へ</button>
  </c:if>
  <button type="submit" name="btn" value="quit">クイズを中断する</button>
 </form>
</body>
</html>