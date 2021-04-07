package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;

/**
 * Servlet implementation class regiCompServlet
 */
@WebServlet("/regiCompServlet")
public class regiCompServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public regiCompServlet() {
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
		// ボタンの値取得
		String btnValue = request.getParameter("btn");
		//各パラメータ取得
		String id = request.getParameter("userId");
		String password = request.getParameter("password");
		String name = request.getParameter("userName");
		//Dao生成
		UserDao uDao = new UserDao();
		// 登録→はいが押下された場合の処理
		if(btnValue.equals("register")) {
			int result = uDao.userInsert(id, password, name);
			if(result == 1) {
				// regiSucceed.jsp にページ遷移
				RequestDispatcher dispatch = request.getRequestDispatcher("regiSucceed.jsp");
				dispatch.forward(request, response);
			}else {
				//エラーフラグを立てて、register.jspに戻る
				request.setAttribute("registerErr", true);
				// register.jsp にページ遷移
				RequestDispatcher dispatch = request.getRequestDispatcher("register.jsp");
				dispatch.forward(request, response);
			}
		}
		// 戻るボタンが押下された場合の処理
		if(btnValue.equals("back")) {
			request.setAttribute("toRegisterId", id);
			request.setAttribute("toRegisterPassword", password);
			request.setAttribute("toRegisterName", name);
			// register.jsp にページ遷移
			RequestDispatcher dispatch = request.getRequestDispatcher("register.jsp");
			dispatch.forward(request, response);
		}
	}

}
