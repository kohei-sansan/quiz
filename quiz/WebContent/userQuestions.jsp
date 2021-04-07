<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー投稿問題一覧</title>
</head>
<body>
<header>
  　<div class="wrapper">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
    </div>
 </header>
<table border="1">
  <tr>
    <th>英単語</th><th>答え</th><th>不正解1</th><th>不正解2</th><th>不正解3</th><th>登録日時</th>
  </tr>
  <c:forEach var="obj" items="${userQuestionsList}" varStatus="status">
　　<tr>
　　　<td><c:out value="${obj.word}"/></td>
     <td><c:out value="${obj.answer}"/></td>
     <td><c:out value="${obj.option1}"/></td>
     <td><c:out value="${obj.option2}"/></td>
     <td><c:out value="${obj.option3}"/></td>
     <td><c:out value="${obj.createDate}"/></td>
　　</tr>
　</c:forEach>
</table>
</body>
</html>