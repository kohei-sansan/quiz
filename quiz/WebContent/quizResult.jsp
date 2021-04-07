<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>スコア結果</title>
</head>
<body>
<header>
  　<div class="wrapper">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
    </div>
 </header>
     <!-- 途中で中断した場合のメッセージ -->
    <c:if test="${quizQuit == true}" >
　　  <p>途中でクイズが中断されました</p>
    </c:if>
<p>あなたのスコアは${resultScore }です</p>
    <!-- 本日日付、週間スコア更新の場合はメッセージ表示 -->
    <c:if test="${not empty dailyUpdated}" >
　　  <p>本日のスコアが更新されました</p>
    </c:if>
    <c:if test="${not empty weeklyUpdated}" >
　　  <p>週間のスコアが更新されました</p>
    </c:if>

  <p><a href="home.jsp">ホームへ</a></p>
</body>
</html>