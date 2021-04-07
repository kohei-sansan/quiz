<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>週間ランキング表示</title>
</head>
<body>
<header>
  　<div class="wrapper">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
    </div>
 </header>
<table border="1">
  <tr>
    <th>順位</th><th>ユーザー名</th><th>スコア</th><th>更新日時</th>
  </tr>
  <c:forEach var="obj" items="${userList}" varStatus="status">
　　<tr>
　　　<td><c:out value="${obj.weeklyRank}"/></td>
     <td><c:out value="${obj.userName}"/></td>
     <td><c:out value="${obj.weeklyMaxScore}"/></td>
     <td><c:out value="${obj.weeklyUpdDt}"/></td>
　　</tr>
　</c:forEach>
</table>
<p><a href="home.jsp">ホームへ</a></p>
</body>
</html>