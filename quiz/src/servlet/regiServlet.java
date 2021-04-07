package servlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDao;
import util.MyUtil;

/**
 * Servlet implementation class regiServlet
 */
@WebServlet("/regiServlet")
public class regiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public regiServlet() {
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

		UserDao uDao = new UserDao();
		// 入力されたid
		String id = request.getParameter("userId");
		// 入力されたパスワード
		String password = request.getParameter("password");
		// 入力されたユーザー名
		String userName = request.getParameter("userName");
		// 入力チェック
		if(MyUtil.isNullOrEmpty(id)||MyUtil.isNullOrEmpty(password)
		 ||MyUtil.isNullOrEmpty(userName)||uDao.UserExistsById(id)) {
			if(MyUtil.isNullOrEmpty(id)) {
				request.setAttribute("idIsBlank", true);
			}
			if(MyUtil.isNullOrEmpty(password)) {
				request.setAttribute("passIsBlank", true);
			}
			if(MyUtil.isNullOrEmpty(userName)) {
				request.setAttribute("nameIsBlank", true);
			}
			if(uDao.UserExistsById(id)) {
				request.setAttribute("userExists", true);
			}
			// register.jsp にページ遷移
			RequestDispatcher dispatch = request.getRequestDispatcher("register.jsp");
			dispatch.forward(request, response);
		}
		//確認ページ用パラメータセット、確認ページへ遷移　
		request.setAttribute("toRegisterId", id);
		request.setAttribute("toRegisterPassword", password);
		request.setAttribute("toRegisterName", userName);
		// registerConfirm.jsp にページ遷移
		RequestDispatcher dispatch = request.getRequestDispatcher("registerConfirm.jsp");
		dispatch.forward(request, response);
	}

}
