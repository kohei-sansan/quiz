package servlet;

import java.io.IOException;
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
import entity.User;

/**
 * Servlet implementation class userQuestionsServlet
 */
@WebServlet("/userQuestionsServlet")
public class userQuestionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public userQuestionsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッション取得
		HttpSession session = request.getSession();
		// ユーザー投稿問題抽出のためユーザーid取得
		String userId = ((User)(session.getAttribute("currentUser")))
										.getUserName();
		// ユーザー投稿問題リスト生成
		QuestionsDao qDao = new QuestionsDao();
		List<Question> qList = qDao.userQuestionSelect(userId);

		request.setAttribute("userQuestionsList", qList);

		// register.jsp にページ遷移
		RequestDispatcher dispatch = request.getRequestDispatcher("userQuestions.jsp");
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
