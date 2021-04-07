<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <p>この内容でよろしいですか</p>
  <form action="postCompleteServlet" method="post" style="text-align: center;">
    <p>
      英単語<input type="text" name="word" value="${requestScope.toRegisterWord }" readonly>
    </p>
    <p>
      答え<input type="text" name="answer" value="${requestScope.toRegisterAnswer }" readonly>
    </p>
    <p>
      間違い用1<input type="text" name="option1" value="${requestScope.toRegisterOption1 }" readonly>
    </p>
    <p>
      間違い用2<input type="text" name="option2" value="${requestScope.toRegisterOption2 }" readonly>
    </p>
    <p>
      間違い用3<input type="text" name="option3" value="${requestScope.toRegisterOption3 }" readonly>
    </p>
    <p>
      <button type="submit" name="btn" value="register">登録</button>
      <button type="submit" name="btn" value="back">戻る</button>
    </p>
  </form>
</body>
</html>