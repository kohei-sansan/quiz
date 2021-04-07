package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.QuestionsDao;
import util.MyUtil;

/**
 * Servlet implementation class postServlet
 */
@WebServlet("/postServlet")
public class postServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public postServlet() {
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

		// 入力チェック用のqDao生成
		QuestionsDao qDao = new QuestionsDao();
		// 入力パラメータ取得　
		String word = request.getParameter("word");
		String answer = request.getParameter("answer");
		String option1 = request.getParameter("option1");
		String option2 = request.getParameter("option2");
		String option3 = request.getParameter("option3");

		// 入力チェック
		if(MyUtil.isNullOrEmpty(word)||MyUtil.isNullOrEmpty(answer)||
		   MyUtil.isNullOrEmpty(option1)||MyUtil.isNullOrEmpty(option2)||
		   MyUtil.isNullOrEmpty(option3)||qDao.questionExists(word)) {
			if(MyUtil.isNullOrEmpty(word)) {
				request.setAttribute("wordEmpty", true);
			}
			if(MyUtil.isNullOrEmpty(answer)) {
				request.setAttribute("answerEmpty", true);
			}
			if(MyUtil.isNullOrEmpty(option1)||
			   MyUtil.isNullOrEmpty(option2)||
			   MyUtil.isNullOrEmpty(option3)) {
				request.setAttribute("optionEmpty", true);
			}
			if(qDao.questionExists(word)) {
				request.setAttribute("wordExists", true);
			}
			// register.jsp にページ遷移
			RequestDispatcher dispatch = request.getRequestDispatcher("post.jsp");
			dispatch.forward(request, response);
		}
		// requestスコープに値をセット
		request.setAttribute("toRegisterWord", word);
		request.setAttribute("toRegisterAnswer", answer);
		request.setAttribute("toRegisterOption1", option1);
		request.setAttribute("toRegisterOption2", option2);
		request.setAttribute("toRegisterOption3", option3);
		// postConfirm.jsp にページ遷移
		RequestDispatcher dispatch = request.getRequestDispatcher("postConfirm.jsp");
		dispatch.forward(request, response);
	}

}
