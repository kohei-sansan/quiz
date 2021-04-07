package servlet;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.QuestionsDao;
import entity.User;

/**
 * Servlet implementation class postCompleteServlet
 */
@WebServlet("/postCompleteServlet")
public class postCompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public postCompleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 文字化け対策
		response.setCharacterEncoding("UTF-8");
		// セッション取得
		HttpSession session = request.getSession();

		// 入力パラメータ取得　
		String word = request.getParameter("word");
		String answer = request.getParameter("answer");
		String option1 = request.getParameter("option1");
		String option2 = request.getParameter("option2");
		String option3 = request.getParameter("option3");

		// ログインユーザーを受け取って、idを取得(問題テーブルのカラムにインサートするため)
		User currentUser = (User)session.getAttribute("currentUser");
		// 問題のインサート処理
		QuestionsDao qDao = new QuestionsDao();
		int result =
				qDao.registerQestion(currentUser.getId(),
									 word, answer,
									 option1, option2,
									 option3,
									 new Timestamp(System.currentTimeMillis()));
		if(result == 1) {
		// postSucceed.jsp にページ遷移
		RequestDispatcher dispatch = request.getRequestDispatcher("postSucceed.jsp");
		dispatch.forward(request, response);
		}
	}

}
