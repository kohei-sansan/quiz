<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ホーム</title>
</head>
<body>
<header>
  　<div class="wrapper">
       <h1 class="logo"><img src="pictures/ets-logo.jpg"></h1>
    </div>
 </header>
 変更用
  <div style="text-align: center">
  <c:if test="${currentUser.dailyMaxScore != null }">
     <p>本日の最高スコア：${currentUser.dailyMaxScore }</p>
     <p>全体の順位：${currentUser.dailyRank }</p>
  </c:if>
  <c:if test="${currentUser.dailyMaxScore == null }">
     <p>本日のスコア：N/A </p>
  </c:if>
  <c:if test="${currentUser.weeklyMaxScore != null }">
     <p>1週間以内の最高スコア：${currentUser.weeklyMaxScore }</p>
     <p>全体の順位：${currentUser.weeklyRank }</p>
  </c:if>
  <c:if test="${currentUser.weeklyMaxScore == null }">
     <p>1週間以内のスコア：N/A </p>
  </c:if>
  <p>マイページ</p>
  <p>ようこそ${sessionScope.currentUser.userName }さん</p>

  <p><a href="post.jsp">問題を投稿</a></p>
  <p><a href="userQuestionsServlet">投稿した問題一覧</a></p>
  <p><a href="preQuiz.jsp">クイズに挑戦</a></p>
  <p><a href="showDRankServlet">日間ランキング表示</a></p>
  <p><a href="showWRankServlet">週間ランキング表示</a></p>
  </div>
</body>
</html>