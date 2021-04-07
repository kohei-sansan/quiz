package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.QuestionsDao;
import entity.Question;

/**
 * Servlet implementation class takeQuizServlet
 */
@WebServlet("/takeQuizServlet")
public class takeQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public takeQuizServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッション取得
		HttpSession session = request.getSession();
		QuestionsDao qDao = new QuestionsDao();
		// ランダムに問題を10個生成し、セッションに格納
		// 問題格納用リスト生成、セッション保存
		List<Question> qList = qDao.get10Questions();
		session.setAttribute("currentQList", qList);
		// ユーザーの選択した回答リスト、現在の回答をセッション保存
		List<String> userChoiceList =
				new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null,null));
		session.setAttribute("userChoiceList", userChoiceList);
		session.setAttribute("userChoice", null);
		// (回答した)問題カウンタ生成、セッション保存
		int count = 0;
		session.setAttribute("quizCount", count);
		// 現在の問題をセッションに保存
		session.setAttribute("currentQuestion", qList.get(count));
		// クイズ実施中フラグ
		session.setAttribute("quizQuit", false);
		// takeQuiz.jspに遷移
		RequestDispatcher dispatch = request.getRequestDispatcher("takeQuiz.jsp");
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
